package start;

import database.SqlOperationsForSearch;
import database.SqlOperationsForStat;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyParseJSON {

    private static List<String> json;

    public static List<String> getJson() {
        return json;
    }

    public static List<String> customJsonParse(File file) throws IOException {
        String jsonFileAsString = null;
        List<Integer> pointsOfJsons = new ArrayList<>();
        List<String> jsonData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while (br.ready())
                jsonFileAsString += br.readLine();
            Pattern pattern = Pattern.compile("([\\p{L}\\s]+)\":[^\\[{]\\s?\"?([-\\p{L}\\s\\d]+)");
            Matcher matcher = pattern.matcher(jsonFileAsString);
            while (matcher.find()) {
                pointsOfJsons.add(matcher.start());
                pointsOfJsons.add(matcher.end());
            }
            for (int i = 0; i < pointsOfJsons.size() - 1; i += 2) {
                jsonData.add(jsonFileAsString.substring(pointsOfJsons.get(i), pointsOfJsons.get(i + 1))
                        .replaceAll("\"", ""));
            }
        }
        return jsonData;
    }


    private static int getNumberOfOperation(String json){
        switch (json.split(":")[0].trim()){
            case "lastName": return 1;
            case "productName": return 2;
            case "minExpenses": return 3;
            case "badCustomers": return 4;
            default: return 0;
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        SqlOperationsForSearch operations = new SqlOperationsForSearch();
       json = customJsonParse(new File("income.JSON"));
       int checkJson = getNumberOfOperation(json.get(0));
       if(checkJson != 0) {
           for (int i = 0; i < json.size(); i++) {
               int operation = getNumberOfOperation(json.get(i));
               String elem = json.get(i);
               switch (operation) {
                   case 1:
                   case 4:
                       operations.getSqlDataForSearch(elem.split(":")[1].trim(), null, operation);
                       break;
                   case 2:
                   case 3:
                       operations.getSqlDataForSearch(elem.split(":")[1].trim(), getNextElemValue(i).split(":")[1].trim(), operation);
                       i++;
                       break;
                   default:
                       System.out.println("Некорректные данные");
               }
           }
       } else {

           SqlOperationsForStat sqlStat = new SqlOperationsForStat();
           java.sql.Date sqlDate1 = java.sql.Date.valueOf(json.get(0).split(":")[1].trim());
           java.sql.Date sqlDate2 = java.sql.Date.valueOf(json.get(1).split(":")[1].trim());
           sqlStat.getSqlDataForStat(sqlDate1, sqlDate2);
       }
        JsonMaker.writeToJsonFile();

    }

    private static String getNextElemValue(int i){
        return json.get(i+1);
    }

}

