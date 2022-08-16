package database;

import criterias.Error;
import json.JsonWriter;
import start.IncomeHandler;
import support.ErrorMessage;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class InitialTables {

    public static void makeTable() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();
             Scanner scanner = new Scanner(System.in)) {
            statement.executeUpdate(InitialTablesQueries.getCreateTableCustomers());
            statement.executeUpdate(InitialTablesQueries.getCreateTableProducts());
            statement.executeUpdate(InitialTablesQueries.getCreateTablePurchases());
            System.out.println("Пожалуйста заполните базу данных из дампа и нажимте Enter по готовности");
            scanner.nextLine();
        } catch (Exception ignore) {
        }
    }

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        Connection connection;
        try (InputStream is = InitialTables.class.getResourceAsStream("/db.properties")) {
            props.load(is);
            String drivers = props.getProperty("jdbc.drivers");
            if (drivers != null)
                System.setProperty("jdbc.drivers", drivers);
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException | IOException e) {
            Error.setCause(ErrorMessage.INCORRECT_DATABASE_CONNECTION);
            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDstFile(), "error");
            throw new SQLException();
        }
        return connection;
    }

}
