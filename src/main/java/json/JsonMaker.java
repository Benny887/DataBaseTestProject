package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import criterias.Stat;
import criterias.Type;
import database.SqlOperationsForSearch;
import database.SqlOperationsForStat;
import java.io.*;
import java.nio.file.Path;

public class JsonMaker {

    public static void writeToJsonFile(Path path) throws IOException {
        Type type;
        Stat stat;
        GsonBuilder gsonB = new GsonBuilder()
                .setPrettyPrinting();
        Gson gson = gsonB.create();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.valueOf(path), true)))) {
            if(SqlOperationsForSearch.getAl().size() !=0) {
                type = new Type("search", SqlOperationsForSearch.getAl());
                gson.toJson(type, writer);
            } else {
                stat = new Stat("stat", SqlOperationsForStat.getTotalDays(),SqlOperationsForStat.getCustomers(),
                        SqlOperationsForStat.getAllCustomersExpanses(),SqlOperationsForStat.getGetAvgExpenses());
                gson.toJson(stat, writer);
            }
        }
    }
}

