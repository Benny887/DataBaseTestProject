package database;

import criterias.Error;
import criterias.statDTOs.Customer;
import criterias.statDTOs.Purchase;
import json.JsonWriter;
import start.IncomeHandler;
import support.ErrorMessage;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;


public class SqlOperationsForStat {

    private static final List<Customer> customersInfo = new ArrayList<>();
    private static final Map<String, List<Purchase>> customerNames = new LinkedHashMap<>();
    private static int totalDays;
    private static int allCustomersExpanses;
    private static double getAvgExpenses;
    private static PreparedStatement stat = null;

    public static List<Customer> getAllCustomersInfo() {
        return customersInfo;
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
            java.sql.Date sqlDate_1 = java.sql.Date.valueOf(IncomeHandler.getJson().get(0).split(":")[1].trim());
            java.sql.Date sqlDate_2 = java.sql.Date.valueOf(IncomeHandler.getJson().get(1).split(":")[1].trim());
            getSqlDataForStat(sqlDate_1, sqlDate_2);
        } catch (IllegalArgumentException e) {
            Error.setCause(ErrorMessage.INCORRECT_DATE);
            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDstFile(), "error");
            throw new IllegalArgumentException();
        }
    }

    private void getSqlDataForStat(Date fromDate, Date toDate) throws IOException {
        try (Connection connection = InitialTables.getConnection()) {
            getAllCustomersInfo(connection, fromDate, toDate);
            totalDays = getDataFromQuery(connection, fromDate, toDate, null, "days");
            allCustomersExpanses = getDataFromQuery(connection, fromDate, toDate, null, "allExp");
            getAvgExpenses = getDataFromQuery(connection, fromDate, toDate, null, "avgExp");
        } catch (Exception sql) {
            Error.setCause(ErrorMessage.BAD_QUERY);
            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDstFile(), "error");
        }
    }

    private void getAllCustomersInfo(Connection connection, Date fromDate, Date toDate) throws SQLException {
        String name;
        Purchase purchase;
        try {
            PreparedStatement stat = connection.prepareStatement(StatQueries.getAllCustomersPurchasesQuery());
            stat.setDate(1, fromDate);
            stat.setDate(2, toDate);
            ResultSet result = stat.executeQuery();
            while (result.next()) {
                name = result.getString(2) + " " + result.getString(1);
                purchase = new Purchase(result.getString(3), Integer.parseInt(result.getString(4)));
                if (customerNames.containsKey(name))
                    customerNames.get(name).add(purchase);
                else {
                    ArrayList<Purchase> purchases = new ArrayList<>();
                    purchases.add(purchase);
                    customerNames.put(name, purchases);
                }
            }
            for (Map.Entry<String, List<Purchase>> list : customerNames.entrySet()) {
                int total = getDataFromQuery(connection, fromDate, toDate, list.getKey().split(" ")[0], "oneCastExp");
                customersInfo.add(new Customer(list.getKey(), list.getValue(), total));
            }
        } catch (SQLException e) {
            stat.close();
            throw new SQLException();
        }
    }


    private int getDataFromQuery(Connection connection, Date fromDate, Date toDate, String lastName, String queryItem) throws SQLException {
        int value = 0;
        try {
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
        } catch (SQLException e) {
            stat.close();
            throw new SQLException();
        }
        return value;
    }


}

