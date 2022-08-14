package start;

import database.SqlOperationsForSearch;
import database.SqlOperationsForStat;
import json.JsonMaker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import static json.MyParseJSON.customJsonParse;

public class IncomeHandler {

    private static List<String> json;

    public static List<String> getJson() {
        return json;
    }
    public void handleIncomeData(String[] args) throws IOException, SQLException {
        json = customJsonParse(new File(args[1]));
        if(args[0].equals("search")){
            SqlOperationsForSearch sqlOperationsForSearch = new SqlOperationsForSearch();
            sqlOperationsForSearch.searchOperationManager();
            JsonMaker.writeToJsonFile(Paths.get(args[2]));
        } else if(args[0].equals("stat")){
            SqlOperationsForStat sqlOperationsForStat = new SqlOperationsForStat();
            sqlOperationsForStat.makePojoForStat();
            JsonMaker.writeToJsonFile(Paths.get(args[2]));
        }
    }
}
