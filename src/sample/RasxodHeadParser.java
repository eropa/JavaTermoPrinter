package sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class RasxodHeadParser {

    public String sPointName;
    public String sSumRasxod;
    public String sId;
    public String sDateRas;

    /**
     * Тест
     * @param s
     */
    public void TestName(String s){
        Object obj=JSONValue.parse(s);
        JSONObject jsonObject = (JSONObject) obj;
        String name = (String) jsonObject.get("name");
        //     double salary = (Double) jsonObject.get("salary");
        long id = (Long) jsonObject.get("id");
        System.out.println(s);
        System.out.println(id);
    }

    /**
     * Парсим тело
     * @param s
     */
    public void ParserVar(String s){
        // Парсем значение
        Object obj=JSONValue.parse(s);
        JSONObject jsonObject = (JSONObject) obj;
        // Запоминаем
        this.sSumRasxod = (String) jsonObject.get("sum").toString();
        this.sPointName = (String) jsonObject.get("sklad");
        this.sId        = (String) jsonObject.get("ras").toString();
        this.sDateRas   = (String) jsonObject.get("created_at");
    }

    public String ParserVarTeloRecord(String s){
        // Парсем значение
        Object obj=JSONValue.parse(s);
        JSONObject jsonObject = (JSONObject) obj;
        String sReturn="";
        String line=jsonObject.get("id_ass").toString();
        sReturn+=jsonObject.get("prname").toString()+"\n";
        sReturn+=jsonObject.get("count").toString()+" * "+(String) jsonObject.get("price").toString()+" руб = 2000 руб \n";
        // Запоминаем
       /* this.sSumRasxod = (String) jsonObject.get("sum_rasxod").toString();
        this.sPointName = (String) jsonObject.get("pname");
        this.sId        = (String) jsonObject.get("id").toString();
        this.sDateRas   = (String) jsonObject.get("created_at");
        */
        return sReturn;
    }

    public String ParserVarTeloRecord1(String s){
        // Парсем значение
        Object obj=JSONValue.parse(s);
        JSONObject jsonObject = (JSONObject) obj;
        String sReturn="";
        String line=jsonObject.get("txt").toString();
        sReturn+=line;

        // Запоминаем
       /* this.sSumRasxod = (String) jsonObject.get("sum_rasxod").toString();
        this.sPointName = (String) jsonObject.get("pname");
        this.sId        = (String) jsonObject.get("id").toString();
        this.sDateRas   = (String) jsonObject.get("created_at");
        */
        return sReturn;
    }

    /**
     *  Выводим шапку
     * @return
     */
    public String PrintHead(){
        String sReturn="";
        sReturn ="Расход № "+this.sId+" \n";
        sReturn +="Точка - "+this.sPointName+" \n";
        sReturn +="Дата- "+this.sDateRas+" \n";
        // sReturn +=this.sSumRasxod+" руб.\n";
        return sReturn;
    }


    /**
     * Выводим тело расхода
     * @param s
     * @return
     */
    public String ParserVarTelo(String s){
        String sReturn="";
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(s);
            JSONArray array = (JSONArray)obj;
            for (int i=0; i<array.size(); i++) {
                //   System.out.println(array.get(i));
                sReturn+=this.ParserVarTeloRecord(array.get(i).toString());
            }
        }catch(ParseException pe) {
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }
        // Запоминаем
        return sReturn;
    }

}
