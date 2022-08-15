package start;

import criterias.Error;
import database.SqlOperationsForSearch;
import database.SqlOperationsForStat;
import json.JsonWriter;
import support.ErrorMessage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static json.MyParseJSON.customJsonParse;

public class IncomeHandler {

    private static List<String> json;
    private static Path writeDstFile;

    public static Path getWriteDstFile() {
        return writeDstFile;
    }

    public static List<String> getJson() {
        return json;
    }

    public void handleIncomeData(String[] args) {
        try {
            if (checkInnerParam(args[0], args[1], args[2])) {
                json = customJsonParse(new File(args[1]));
                writeDstFile = Paths.get(args[2]);
                if (json.size() != 0) {
                    if (args[0].equals("search")) {
                        SqlOperationsForSearch sqlOperationsForSearch = new SqlOperationsForSearch();
                        sqlOperationsForSearch.searchOperationManager();
                        JsonWriter.writeToJsonFile(Paths.get(args[2]), "search");
                    } else if (args[0].equals("stat")) {
                        SqlOperationsForStat sqlOperationsForStat = new SqlOperationsForStat();
                        sqlOperationsForStat.makePojoForStat();
                        JsonWriter.writeToJsonFile(Paths.get(args[2]), "stat");
                    }
                } else {
                    Error.setCause(ErrorMessage.INCORRECT_INPUT_FILES);
                    JsonWriter.writeToJsonFile(IncomeHandler.getWriteDstFile(), "error");
                }
            } else {
                Error.setCause(ErrorMessage.INCORRECT_INPUT_PARAMS);
                JsonWriter.writeToJsonFile(IncomeHandler.getWriteDstFile(), "error");
            }
        } catch (Exception ignore) {
        }

    }

    private boolean checkInnerParam(String firstParam, String secondParam, String thirdParam) {
        return Paths.get(secondParam).toFile().isFile() && Paths.get(thirdParam).toFile().isFile() &&
                (firstParam.equals("search") || firstParam.equals("stat"));
    }
}
