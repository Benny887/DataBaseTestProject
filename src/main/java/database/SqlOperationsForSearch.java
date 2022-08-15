package database;

import criterias.Error;
import criterias.searchDTOs.*;
import json.JsonWriter;
import start.IncomeHandler;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SqlOperationsForSearch {
    private static final List<String> lastName = new ArrayList<>();
    private static final List<String> productName = new ArrayList<>();
    private static final List<String> minExpenses = new ArrayList<>();
    private static final List<String> badCustomers = new ArrayList<>();
    private static final ArrayList<Criteria> dataForJsonFile = new ArrayList<>();
    public static ArrayList<Criteria> getDataForJsonFile() {
        return dataForJsonFile;
    }
    private static PreparedStatement stat = null;
    private static ResultSet result;

    private int getNumberOfOperation(String json) {
        switch (json.split(":")[0].trim()) {
            case "lastName":
                return 1;
            case "productName":
                return 2;
            case "minExpenses":
                return 3;
            case "badCustomers":
                return 4;
            default:
                return 0;
        }
    }

    public void searchOperationManager() throws SQLException, IOException {
        try {
            for (int i = 0; i < IncomeHandler.getJson().size(); i++) {
                int operation = getNumberOfOperation(IncomeHandler.getJson().get(i));
                String elem = IncomeHandler.getJson().get(i);
                switch (operation) {
                    case 1:
                    case 4:
                        getSqlDataForSearch(elem.split(":")[1].trim(), null, operation);
                        break;
                    case 2:
                    case 3:
                        getSqlDataForSearch(elem.split(":")[1].trim(), getNextElemValue(i).split(":")[1].trim(), operation);
                        i++;
                        break;
                    default:
                        Error.setCause("Неверно указан один из критериев поиска");
                        JsonWriter.writeToJsonFile(IncomeHandler.getWriteDst(), "error");
                        throw new IllegalArgumentException();
                }
            }
        } catch (IllegalArgumentException e) {
            Error.setCause("Ошибка в значении критерия поиска");
            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDst(), "error");
            throw new IllegalArgumentException();
        }
    }

    private String getNextElemValue(int i) {
        return IncomeHandler.getJson().get(i + 1);
    }

    private void getSqlDataForSearch(String firstParam, String secondParam, int numOfOperation) throws SQLException, IOException {
        try (Connection connection = InitialTables.getConnection()) {
            switch (numOfOperation) {
                case 1:
                    stat = connection.prepareStatement(SearchQueries.getLastnameQuery());
                    stat.setString(1, firstParam);
                    break;
                case 2:
                    stat = connection.prepareStatement(SearchQueries.getProductAndCountQuery());
                    stat.setString(1, firstParam);
                    stat.setInt(2, Integer.parseInt(secondParam));
                    break;
                case 3:
                    stat = connection.prepareStatement(SearchQueries.getMinMaxQuery());
                    stat.setInt(1, Integer.parseInt(firstParam));
                    stat.setInt(2, Integer.parseInt(secondParam));
                    break;
                case 4:
                    stat = connection.prepareStatement(SearchQueries.getBadCustomersQuery());
                    stat.setInt(1, Integer.parseInt(firstParam));
                    break;
            }
            result = stat.executeQuery();
            while (result.next()) {
                chooseCriteriaList(numOfOperation).add(result.getString(1) + ":" + result.getString(2));
            }
            makePojoForSearch(firstParam, secondParam, numOfOperation);
            stat.close();
        }
    }

    private void makePojoForSearch(String firstJsonElem, String secondJsonElem, int numOfOperation) {
        List<LastName> results = new ArrayList<>();
        Criteria criteria;
        List<String> criterias = chooseCriteriaList(numOfOperation);
        for (String str : criterias) {
            results.add(new LastName(str.split(":")[1], str.split(":")[0]));
        }
        criterias.clear();
        switch (numOfOperation) {
            case 1:
                Name nameCriteria = new Name(firstJsonElem);
                criteria = new FirstCriteria(nameCriteria, results);
                dataForJsonFile.add(criteria);
                break;
            case 2:
                MinTimesPurchase nameCriteria1 = new MinTimesPurchase(firstJsonElem, Integer.parseInt(secondJsonElem));
                criteria = new SecondCriteria(nameCriteria1, results);
                dataForJsonFile.add(criteria);
                break;
            case 3:
                MinMaxExpenses nameCriteria3 = new MinMaxExpenses(Integer.parseInt(firstJsonElem), Integer.parseInt(secondJsonElem));
                criteria = new ThirdCriteria(nameCriteria3, results);
                dataForJsonFile.add(criteria);
                break;
            case 4:
                BadCustomer nameCriteria4 = new BadCustomer(Integer.parseInt(firstJsonElem));
                criteria = new FourthCriteria(nameCriteria4, results);
                dataForJsonFile.add(criteria);
                break;
        }
    }


    private List<String> chooseCriteriaList(int criteria) {
        switch (criteria) {
            case 1:
                return lastName;
            case 2:
                return productName;
            case 3:
                return minExpenses;
            case 4:
                return badCustomers;
            default:
                return null;
        }
    }
}