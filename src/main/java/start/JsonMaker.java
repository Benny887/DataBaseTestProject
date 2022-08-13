package start;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import criterias.Type;
import criterias.search.*;
import database.SqlSearchOperations;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonMaker {
    private static ArrayList<Criteria> al = new ArrayList<>();

    public static ArrayList<Criteria> getAl() {
        return al;
    }

    public static void makeSearchJson(String firstJsonElem, String secondJsonElem, int numOfOperation) {
        List<LastName> results = new ArrayList<>();
        Criteria criteria;
        List<String> criterias = SqlSearchOperations.chooseCriteriaList(numOfOperation);
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
        GsonBuilder gsonB = new GsonBuilder()
                .setPrettyPrinting();
        Gson gson = gsonB.create();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.json", true)))) {
            Type type = new Type("search", al);
            gson.toJson(type, writer);
        }
    }
}

//            по имени
//        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.json", true)))) {
//            List<LastName> results = new ArrayList<>();
//            Name nameCriteria = new Name(jsonElem);
//            for (String str : SqlSearchOperations.chooseCriteriaList(elem)) {
//                results.add(new LastName(str.split(":")[0], str.split(":")[1]));
//            }
//            FirstCriteria firstCriteria = new FirstCriteria(nameCriteria, results);
//            ArrayList<Criteria> al = new ArrayList<>();
//            al.add(firstCriteria);
//            Type type = new Type("search", al);
//
//        по мин колич покуп
//             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.json",true)));
//            List<LastName> results = new ArrayList<>();
//            MinTimesPurchase nameCriteria = new MinTimesPurchase(firstParam,Integer.parseInt(secondParam));
//            for(String str : chooseCriteriaList(numOfOperation)){
//                results.add(new LastName(str.split(":")[0], str.split(":")[1]));
//            }
//            SecondCriteria secondCriteria = new SecondCriteria(nameCriteria,results);
//            List<Criteria> al = new ArrayList<>();
//            al.add(secondCriteria);
//            Type type = new Type("search",al);

//           в min max интервал
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.json",true)));
//        List<LastName> results = new ArrayList<>();
//        MinMaxExpenses nameCriteria = new MinMaxExpenses(Integer.parseInt(firstParam),Integer.parseInt(secondParam));
//        for(String str : chooseCriteriaList(numOfOperation)){
//            results.add(new LastName(str.split(":")[0], str.split(":")[1]));
//        }
//        ThirdCriteria thirdCriteria = new ThirdCriteria(nameCriteria,results);
//        List<Criteria> al = new ArrayList<>();
//        al.add(thirdCriteria);
//        Type type = new Type("search",al);
//
//        пассивный покупатель
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.json",true)));
//        List<LastName> results = new ArrayList<>();
//        BadCustomer nameCriteria = new BadCustomer(Integer.parseInt(firstParam));
//        for(String str : chooseCriteriaList(numOfOperation)){
//            results.add(new LastName(str.split(":")[0], str.split(":")[1]));
//        }
//        FourthCriteria fourthCriteria = new FourthCriteria(nameCriteria,results);
//        List<Criteria> al = new ArrayList<>();
//        al.add(fourthCriteria);
//        Type type = new Type("search",al);


//                    gson.toJson(type, writer);
//                    writer.close();
//
//        }

