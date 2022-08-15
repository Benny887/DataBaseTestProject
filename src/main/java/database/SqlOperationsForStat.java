package database;

import criterias.Error;
import criterias.statDTOs.Customer;
import criterias.statDTOs.Purchase;
import json.JsonWriter;
import start.IncomeHandler;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;


public class SqlOperationsForStat {

    private static List<Customer> customers = new ArrayList<>();
    private static Map<String,List<Purchase>> uniqueNames = new LinkedHashMap<>();
    private static int totalDays;
    private static int allCustomersExpanses;
    private static double getAvgExpenses;

    public static List<Customer> getAllCustomersPurchases() {
        return customers;
    }

    public static int getTotalDays() {
        return totalDays;
    }

    public static int getAllCustomersExpanses() {
        return allCustomersExpanses;
    }

    public static double getGetAvgExpenses() {
        return getAvgExpenses;
    }

    public void makePojoForStat() throws SQLException, IOException {
        try {
            java.sql.Date sqlDate1 = java.sql.Date.valueOf(IncomeHandler.getJson().get(0).split(":")[1].trim());
            java.sql.Date sqlDate2 = java.sql.Date.valueOf(IncomeHandler.getJson().get(1).split(":")[1].trim());
            getSqlDataForStat(sqlDate1, sqlDate2);
        } catch (IllegalArgumentException e){
            Error.setCause("Ошибка в значении даты во входящем файле(неверный формат)");
            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDst(),"error");
            throw new IllegalArgumentException();
        }
    }

    private void getSqlDataForStat(Date fromDate, Date toDate) throws SQLException, IOException {
        try (Connection connection = InitialTables.getConnection()){
            getAllCustomersPurchases(connection,fromDate,toDate);
            totalDays = getDaysWithoutHolidays(connection,fromDate,toDate);
            allCustomersExpanses = getAllCustomersExpanses(connection,fromDate,toDate);
            getAvgExpenses = getAvgExpenses(connection,fromDate,toDate);
        }
//        catch (SQLException sql){
//            Error.setCause("Ошибка в запрос");
//            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDst(),"error");
//            throw new IllegalArgumentException();
//        }
    }

    private void getAllCustomersPurchases(Connection connection, Date fromDate, Date toDate) throws SQLException {
        String name;
        Purchase purch;
        PreparedStatement stat = connection.prepareStatement("select c.firstName, c.lastName, p.purchase, sum(pr.price) total\n" +
                "from purchases p\n" +
                "join products pr on p.purchase = pr.prodName\n" +
                "join customers c on p.customer = c.lastName\n" +
                "where acquireDate between ? and ? \n" +
                "      and dayname(acquireDate) not in ('Sunday','Saturday') \n" +
                "group by c.firstName, c.lastName, p.purchase\n" +
                "order by total desc");
        stat.setDate(1, fromDate);
        stat.setDate(2, toDate);
        ResultSet result = stat.executeQuery();
        while (result.next()) {
            name = result.getString(2) + " " + result.getString(1);
            purch = new Purchase(result.getString(3), Double.parseDouble(result.getString(4)));
            if(uniqueNames.containsKey(name))
                uniqueNames.get(name).add(purch);
            else {
                ArrayList<Purchase> purchases = new ArrayList<>();
                purchases.add(purch);
                uniqueNames.put(name,purchases);
            }
        }
        for (Map.Entry<String, List<Purchase>> list : uniqueNames.entrySet()){
            int total = getOneCustomerExpenses(connection, fromDate, toDate, list.getKey().split(" ")[0]);
            customers.add(new Customer(list.getKey(), list.getValue(), total));
        }
        stat.close();
    }

    private int getOneCustomerExpenses(Connection connection, Date fromDate, Date toDate, String lastName) throws SQLException {
        int value=0;
        PreparedStatement stat = connection.prepareStatement("select sum(pr.price)\n" +
                "from purchases p\n" +
                "join products pr on p.purchase = pr.prodName\n" +
                "where customer = ? \n" +
                "and acquireDate between ? and ? \n" +
                "and dayname(acquireDate) not in ('Sunday','Saturday')");
        stat.setString(1, lastName);
        stat.setDate(2, fromDate);
        stat.setDate(3, toDate);
        ResultSet result = stat.executeQuery();
        if(result.next())
            value=result.getInt(1);
        stat.close();
        return value;
    }

    public int getDaysWithoutHolidays (Connection connection, Date fromDate, Date toDate) throws SQLException {
        int value = 0;
        String drop = "drop function if exists exclude_holidays";
        String func ="create function exclude_holidays(date1 DATE, date2 DATE)\n" +
                "returns int deterministic\n" +
                "return abs(datediff(date2, date1)) + 1 - abs(datediff(adddate(date2, interval 1 - dayofweek(date2) day),\n" +
                "                    adddate(date1, interval 1 - dayofweek(date1) day))) / 7 * 2 - (dayofweek(if(date1 > date2, date1, date2)) = 6) - \n" +
                "                    (dayofweek(if(date1 < date2, date1, date2)) = 7);";
        Statement stat = connection.createStatement();
        stat.execute(drop);
        stat.execute(func);
        CallableStatement call = connection.prepareCall("{? = call exclude_holidays(?, ?)}");
        call.registerOutParameter(1,Types.INTEGER);
        call.setDate(2, fromDate);
        call.setDate(3, toDate);
        call.execute();
        ResultSet result= call.executeQuery();
        if (result.next())
            value = result.getInt(1);
        call.close();
        return value;
    }

    private int getAllCustomersExpanses(Connection connection, Date fromDate, Date toDate) throws SQLException {
        int value=0;
        PreparedStatement stat = connection.prepareStatement("select sum(pr.price) total\n" +
                "from purchases p\n" +
                "join products pr on p.purchase = pr.prodName\n" +
                "and acquireDate between ? and ?\n" +
                "and dayname(acquireDate) not in ('Sunday','Saturday') ");
        stat.setDate(1, fromDate);
        stat.setDate(2, toDate);
        ResultSet result = stat.executeQuery();
        if(result.next())
            value=result.getInt(1);
        stat.close();
        return value;
    }

    private double getAvgExpenses(Connection connection, Date fromDate, Date toDate) throws SQLException {
        int value=0;
        PreparedStatement stat = connection.prepareStatement("select avg(pr.price) total\n" +
                "from purchases p\n" +
                "join products pr on p.purchase = pr.prodName\n" +
                "and acquireDate between ? and ?\n" +
                "and dayname(acquireDate) not in ('Sunday','Saturday')");
        stat.setDate(1, fromDate);
        stat.setDate(2, toDate);
        ResultSet result = stat.executeQuery();
        if(result.next())
            value=result.getInt(1);
        stat.close();
        return value;
    }

}

