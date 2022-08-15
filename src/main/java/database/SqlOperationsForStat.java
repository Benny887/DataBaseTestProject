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
    private static Map<String, List<Purchase>> uniqueNames = new LinkedHashMap<>();
    private static int totalDays;
    private static int allCustomersExpanses;
    private static double getAvgExpenses;
    private static PreparedStatement stat = null;

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

    public void makePojoForStat() throws IOException {
        try {
            java.sql.Date sqlDate1 = java.sql.Date.valueOf(IncomeHandler.getJson().get(0).split(":")[1].trim());
            java.sql.Date sqlDate2 = java.sql.Date.valueOf(IncomeHandler.getJson().get(1).split(":")[1].trim());
            getSqlDataForStat(sqlDate1, sqlDate2);
        } catch (IllegalArgumentException e) {
            Error.setCause("Ошибка в значении даты во входящем файле(неверный формат)");
            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDst(), "error");
            throw new IllegalArgumentException();
        }
    }

    private void getSqlDataForStat(Date fromDate, Date toDate) throws IOException {
        try (Connection connection = InitialTables.getConnection()) {
            getAllCustomersPurchases(connection, fromDate, toDate);
            totalDays = getDataFromQuery(connection, fromDate, toDate, null, "days");
            allCustomersExpanses = getDataFromQuery(connection, fromDate, toDate, null, "allExp");
            getAvgExpenses = getDataFromQuery(connection, fromDate, toDate, null, "avgExp");
        } catch (SQLException sql) {
            Error.setCause("Ошибка в запросе");
            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDst(), "error");
            throw new IllegalArgumentException();
        }
    }

    private void getAllCustomersPurchases(Connection connection, Date fromDate, Date toDate) throws SQLException {
        String name;
        Purchase purch;
        PreparedStatement stat = connection.prepareStatement(StatQueries.getAllCustomersPurchasesQuery());
        stat.setDate(1, fromDate);
        stat.setDate(2, toDate);
        ResultSet result = stat.executeQuery();
        while (result.next()) {
            name = result.getString(2) + " " + result.getString(1);
            purch = new Purchase(result.getString(3), Double.parseDouble(result.getString(4)));
            if (uniqueNames.containsKey(name))
                uniqueNames.get(name).add(purch);
            else {
                ArrayList<Purchase> purchases = new ArrayList<>();
                purchases.add(purch);
                uniqueNames.put(name, purchases);
            }
        }
        for (Map.Entry<String, List<Purchase>> list : uniqueNames.entrySet()) {
            int total = getDataFromQuery(connection, fromDate, toDate, list.getKey().split(" ")[0], "oneCastExp");
            customers.add(new Customer(list.getKey(), list.getValue(), total));
        }
        stat.close();
    }


    private int getDataFromQuery(Connection connection, Date fromDate, Date toDate, String lastName, String queryItem) throws SQLException {
        int value = 0;
        if (queryItem.equals("oneCastExp")) {
            stat = connection.prepareStatement(StatQueries.getOneCustomerExpensesQuery());
            stat.setString(1, lastName);
            stat.setDate(2, fromDate);
            stat.setDate(3, toDate);
        } else {
            switch (queryItem) {
                case "days":
                    stat = connection.prepareStatement(StatQueries.getDaysWithoutHolidaysQuery());
                    break;
                case "allExp":
                    stat = connection.prepareStatement(StatQueries.getAllCustomersExpensesQuery());
                    break;
                case "avgExp":
                    stat = connection.prepareStatement(StatQueries.getAvgExpensesQuery());
                    break;
            }
            stat.setDate(1, fromDate);
            stat.setDate(2, toDate);
        }
        ResultSet result = stat.executeQuery();
        if (result.next())
            value = result.getInt(1);
        stat.close();
        return value;
    }


}

