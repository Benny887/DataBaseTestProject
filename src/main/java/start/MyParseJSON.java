package start;//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
//import com.fasterxml.jackson.databind.util.JSONPObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import criterias.stat.DateForStat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            Pattern pattern = Pattern.compile("[\\p{L}\\s]+\":\\s?\"?([\\p{L}\\s\\d]+)");
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
//        SqlSearchOperations operations = new SqlSearchOperations();
       json = customJsonParse(new File("income.JSON"));
       int checkJson = getNumberOfOperation(json.get(0));
       if(checkJson != 0) {
//           for (int i = 0; i < json.size(); i++) {
//               int operation = getNumberOfOperation(json.get(i));
//               String elem = json.get(i);
//               switch (operation) {
//                   case 1:
//                   case 4:
//                       operations.getSqlDataForSearch(elem.split(":")[1].trim(), null, operation);
//                       break;
//                   case 2:
//                   case 3:
//                       operations.getSqlDataForSearch(elem.split(":")[1].trim(), getNextElemValue(i).split(":")[1].trim(), operation);
//                       i++;
//                       break;
//                   default:
//                       System.out.println("Некорректные данные");
//               }
//           }
       } else {
           GsonBuilder gsonB = new GsonBuilder()
                   .setPrettyPrinting();
           Gson gson = gsonB.create();
           Reader reader = Files.newBufferedReader(Paths.get("income.JSON"));
           DateForStat dateInterval = gson.fromJson(reader,DateForStat.class);
           System.out.println(dateInterval);
           reader.close();
       }
//        JsonMaker.writeToJsonFile();
//        json = customJsonParse(new File("income.JSON"));
    }

    private static String getNextElemValue(int i){
        return json.get(i+1);
    }


}
//operations.getSqlDataForSearch(json.get(0).split(":")[1].trim(),null); поиск по фамилии
//operations.getSqlDataForSearch(json.get(1).split(":")[1].trim(),json.get(2).split(":")[1].trim()); поиск миним
//operations.getSqlDataForSearch(json.get(3).split(":")[1].trim(),json.get(4).split(":")[1].trim()); min и max покупки
// operations.getSqlDataForSearch(json.get(5).split(":")[1].trim(),null); пассивн покуп
