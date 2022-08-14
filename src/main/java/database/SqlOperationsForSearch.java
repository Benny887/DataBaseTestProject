package database;

import start.JsonMaker;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SqlOperationsForSearch {
    private static List<String> lastName = new ArrayList<>();
    private static List<String> productName = new ArrayList<>();
    private static List<String> minExpenses = new ArrayList<>();
    private static List<String> badCustomers = new ArrayList<>();

    public static List<String> getLastName() {
        return lastName;
    }

    public static List<String> getProductName() {
        return productName;
    }

    public static List<String> getMinExpenses() {
        return minExpenses;
    }

    public static List<String> getBadCustomers() {
        return badCustomers;
    }

    public void getSqlDataForSearch(String firstParam, String secondParam, int numOfOperation) throws SQLException, IOException {

        PreparedStatement stat = null;
        try (Connection connection = InitialTables.getConnection()) {
            switch (numOfOperation) {
                case 1:
                    stat = connection.prepareStatement("select * from customers \n" +
                            "where lastName = ?");
                    stat.setString(1, firstParam);
                    break;
                case 2:
                    stat = connection.prepareStatement("select c.firstName, c.lastName from customers c\n" +
                            "inner join (select customer from purchases where purchase = ? \n" +
                            "group by customer having count(*) >= ?) cp on c.lastName = cp.customer");
                    stat.setString(1, firstParam);
                    stat.setInt(2, Integer.parseInt(secondParam));
                    break;
                case 3:
                    stat = connection.prepareStatement("select c.firstName, c.lastName from customers c\n" +
                            "inner join  (select pu.customer, sum(pr.price) allPurch\n" +
                            "from purchases pu join products pr on pu.purchase = pr.prodName\n" +
                            "group by pu.customer having sum(pr.price) between ? and ?) dog\n" +
                            "on c.lastName = dog.customer");
                    stat.setInt(1, Integer.parseInt(firstParam));
                    stat.setInt(2, Integer.parseInt(secondParam));
                    break;
                case 4:
                    stat = connection.prepareStatement("select c.firstName, c.lastName from customers c " +
                            "inner join (select p.customer\n" +
                            "from purchases p group by p.customer having count(*) = (select min(cou) " +
                            "from (select pu.customer p , count(*) cou from purchases pu " +
                            "group by pu.customer) cu) limit ?) " +
                            "as kot on c.lastName = kot.customer");
                    stat.setInt(1, Integer.parseInt(firstParam));
                    break;
            }
            ResultSet result = stat.executeQuery();
            while (result.next()) {
                chooseCriteriaList(numOfOperation).add(result.getString(1) + ":" + result.getString(2));
            }
            JsonMaker.makeSearchJson(firstParam,secondParam,numOfOperation);
            stat.close();
        }
    }


    public static List<String> chooseCriteriaList(int criteria){
        switch (criteria){
            case 1: return lastName;
            case 2: return productName;
            case 3: return minExpenses;
            case 4: return badCustomers;
            default: return null;
        }
    }
}
// "lastNameCriteriaResults":"Коваль",
//         "productName":"Морозильная камера",
//         "minTimes": 5,
//         "minExpenses": 1000,
//         "maxExpenses": 100000,
//         "productName":"Холодильник",
//         "minTimes": 1,
//         "lastNameCriteriaResults":"Соколов",
//         "minExpenses": 1000,
//         "maxExpenses": 10000,
//         "badCustomers": 3,
//         "badCustomers": 2