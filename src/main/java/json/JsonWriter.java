package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import criterias.Error;
import criterias.Stat;
import criterias.Search;
import database.SqlOperationsForSearch;
import database.SqlOperationsForStat;
import java.io.*;
import java.nio.file.Path;

public class JsonWriter {

    public static void writeToJsonFile(Path path,String dataToWrite) throws IOException {
        GsonBuilder gsonB = new GsonBuilder()
                .setPrettyPrinting();
        Gson gson = gsonB.create();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.valueOf(path), true)))) {
            if(dataToWrite.equals("search")) {
                Search type = new Search("search", SqlOperationsForSearch.getAl());
                gson.toJson(type, writer);
            } else if (dataToWrite.equals("stat")){
                Stat stat = new Stat("stat", SqlOperationsForStat.getTotalDays(),SqlOperationsForStat.getAllCustomersPurchases(),
                        SqlOperationsForStat.getAllCustomersExpanses(),SqlOperationsForStat.getGetAvgExpenses());
                gson.toJson(stat, writer);
            } else {
                Error error = new Error("error",Error.getCause());
                gson.toJson(error,writer);
            }
        }
    }
}

