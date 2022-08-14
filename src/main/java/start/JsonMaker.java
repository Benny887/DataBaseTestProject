package start;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import criterias.Stat;
import criterias.Type;
import criterias.searchDTOs.*;
import database.SqlOperationsForSearch;
import database.SqlOperationsForStat;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonMaker {
    private static ArrayList<Criteria> al = new ArrayList<>();

    public static ArrayList<Criteria> getAl() {
        return al;
    }

    public static void makeSearchJson(String firstJsonElem, String secondJsonElem, int numOfOperation) {
        List<LastName> results = new ArrayList<>();
        Criteria criteria;
        List<String> criterias = SqlOperationsForSearch.chooseCriteriaList(numOfOperation);
        for (String str : criterias) {
            results.add(new LastName(str.split(":")[0], str.split(":")[1]));
        }
        criterias.clear();
        switch (numOfOperation) {
            case 1:
                Name nameCriteria = new Name(firstJsonElem);
                criteria = new FirstCriteria(nameCriteria, results);
                al.add(criteria);
                break;
            case 2:
                MinTimesPurchase nameCriteria1 = new MinTimesPurchase(firstJsonElem, Integer.parseInt(secondJsonElem));
                criteria = new SecondCriteria(nameCriteria1, results);
                al.add(criteria);
                break;
            case 3:
                MinMaxExpenses nameCriteria3 = new MinMaxExpenses(Integer.parseInt(firstJsonElem), Integer.parseInt(secondJsonElem));
                criteria = new ThirdCriteria(nameCriteria3, results);
                al.add(criteria);
                break;
            case 4:
                BadCustomer nameCriteria4 = new BadCustomer(Integer.parseInt(firstJsonElem));
                criteria = new FourthCriteria(nameCriteria4, results);
                al.add(criteria);
                break;
        }
    }

    public static void writeToJsonFile() throws IOException {
        Type type;
        Stat stat;
        GsonBuilder gsonB = new GsonBuilder()
                .setPrettyPrinting();
        Gson gson = gsonB.create();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.json", true)))) {
            if(al.size() !=0) {
                type = new Type("search", al);
                gson.toJson(type, writer);
            } else {
                stat = new Stat("stat", SqlOperationsForStat.getTotalDays(),SqlOperationsForStat.getCustomers(),
                        SqlOperationsForStat.getAllCustomersExpanses(),SqlOperationsForStat.getGetAvgExpenses());
                gson.toJson(stat, writer);
            }
        }
    }
}

