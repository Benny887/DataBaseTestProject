package start;

import criterias.Error;
import database.SqlOperationsForSearch;
import database.SqlOperationsForStat;
import json.JsonWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import static json.MyParseJSON.customJsonParse;

public class IncomeHandler {

    private static List<String> json;
    private static Path writeDst;

    public static Path getWriteDst() {
        return writeDst;
    }

    public static List<String> getJson() {
        return json;
    }

    public void handleIncomeData(String[] args) throws IOException, SQLException {
//        try {
            if (checkInnerParam(args[0],args[1],args[2])) {
                json = customJsonParse(new File(args[1]));
                writeDst = Paths.get(args[2]);
                if(json.size() != 0) {
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
                    Error.setCause("Входной файл содержит некорректные данные(пуст)");
                    JsonWriter.writeToJsonFile(IncomeHandler.getWriteDst(), "error");
                }
            } else {
                System.out.println("Проверьте корректность входных параметров:названия файлов и тип операции");
            }
        }
//        catch (Exception ignore){
//            System.out.println("какая-то ошибка в IncomeHandler"); //поправить
//        }
//    }

    private boolean checkInnerParam(String firstParam, String secondParam, String thirdParam){
        return Paths.get(secondParam).toFile().isFile() && Paths.get(thirdParam).toFile().isFile() &&
                (firstParam.equals("search") || firstParam.equals("stat"));
    }
}
