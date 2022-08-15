package json;

import criterias.Error;
import start.IncomeHandler;
import support.ErrorMessage;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyParseJSON {

    public static List<String> customJsonParse(File file) throws IOException {
        String jsonFileAsString = null;
        List<Integer> pointsOfJsons = new ArrayList<>();
        List<String> jsonData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while (br.ready())
                jsonFileAsString += br.readLine();
            Pattern pattern = Pattern.compile("([\\p{L}\\s]+)\"\\s?:[^\\[{]\\s?\"?([-\\p{L}\\s\\d]+)");
            Matcher matcher = pattern.matcher(jsonFileAsString);
            while (matcher.find()) {
                pointsOfJsons.add(matcher.start());
                pointsOfJsons.add(matcher.end());
            }
            for (int i = 0; i < pointsOfJsons.size() - 1; i += 2) {
                jsonData.add(jsonFileAsString.substring(pointsOfJsons.get(i), pointsOfJsons.get(i + 1))
                        .replaceAll("\"", ""));
            }
        } catch (NullPointerException e){
            Error.setCause(ErrorMessage.INCORRECT_INPUT_FILES);
            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDstFile(), "error");
        }
        return jsonData;
    }


}

