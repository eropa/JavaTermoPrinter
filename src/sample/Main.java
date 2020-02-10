package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import java.io.FileReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONValue;

import java.util.concurrent.TimeUnit;



public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        do {
            try {
                //--------------------------Читаем файл с опциями
                Map<String, String> assoc = new HashMap<String, String>();
                // Создается построитель документа
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                // Создается дерево DOM документа из файла
                Document document = documentBuilder.parse("D:\\serverprint\\option.xml");

                // Получаем корневой элемент
                Node root = document.getDocumentElement();

                System.out.println(" ***** Обновляем чеки **********");
                // Просматриваем все подэлементы корневого - т.е. книги
                NodeList books = root.getChildNodes();
                for (int i = 0; i < books.getLength(); i++) {
                    Node book = books.item(i);
                    // Если нода не текст, то это книга - заходим внутрь
                    if (book.getNodeType() != Node.TEXT_NODE) {
                        NodeList bookProps = book.getChildNodes();
                        for(int j = 0; j < bookProps.getLength(); j++) {
                            Node bookProp = bookProps.item(j);
                            // Если нода не текст, то это один из параметров книги - печатаем
                            if (bookProp.getNodeType() != Node.TEXT_NODE) {
                                assoc.put(bookProp.getNodeName(), bookProp.getChildNodes().item(0).getTextContent());
                            }
                        }

                    }
                }

                String point=assoc.get("point");
                String token=assoc.get("token");
                String url=assoc.get("url");

                //--------------------------Получаем список расходов для печати
                String stringUrl = url+"/api/ras/"+point+"/"+token;
                //  System.out.println(stringUrl);
                URL urlParser = new URL(stringUrl);
                String strJson="";
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlParser.openStream(), "UTF-8"))) {
                    for (String line; (line = reader.readLine()) != null;) {
                        //System.out.println(line);
                        strJson=line;
                    }
                }


                JSONParser parser = new JSONParser();

                try{
                    Object obj = parser.parse(strJson);
                    JSONArray array = (JSONArray)obj;

                    RasxodHeadParser par=new RasxodHeadParser();
                    String sHead="";
                    for (int i=0; i<array.size(); i++)
                    {
                        String s=array.get(i).toString();
                        par.ParserVar(s);

                        sHead=par.PrintHead();

                        sHead+=par.ParserVarTeloRecord1(s);
                        sHead+="Итого "+par.sSumRasxod+" руб. \n";
                        sHead+= "*** Спасибо за покупку *** \n\n\n\n\n\n\n";
                        System.out.println(sHead);
                        posprint printerService = new posprint();
                        byte[] cutP = new byte[] { 0x1B, 0x74, 0x11 };
                        byte[] cutP1 = new byte[] { 0x1d, 'V', 1 };
                        printerService.printBytes("XP-58", cutP);
                        printerService.printString("XP-58", sHead);
                        printerService.printBytes("XP-58", cutP1);
                        printerService.printBytes("XP-58", cutP);
                        printerService.printString("XP-58", sHead);
                        printerService.printBytes("XP-58", cutP1);

                        String stringUrlEnd = url+"/api/rasprint/"+point+"/"+token+"/"+par.sId;
                         System.out.println(stringUrl);
                        URL urlEnd = new URL(stringUrlEnd);
                        urlEnd.openStream();
                        System.out.println(" ***** Чек распечатан **********");
                    }
                }catch(ParseException pe) {
                    System.out.println("position: " + pe.getPosition());
                    System.out.println(pe);
                }
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace(System.out);
            } catch (SAXException ex) {
                ex.printStackTrace(System.out);
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
            TimeUnit.SECONDS.sleep(1);
        } while (1==1);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
