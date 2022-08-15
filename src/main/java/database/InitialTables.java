package database;

import criterias.Error;
import json.JsonWriter;
import start.IncomeHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class InitialTables {
    private static final String separator = File.separator;
    private static final File connAttr = new File(System.getProperty("user.dir") +
            separator + "src" + separator + "main" + separator + "resources" + separator + "db.properties");

    public static void makeTable() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();
             Scanner scanner = new Scanner(System.in)) {
            statement.executeUpdate("create table customers(\n" +
                    "firstName varchar(15) not null check (firstName <> ''),\n" +
                    "lastName varchar(20) not null check (lastName <> '')  primary key\n" +
                    ")");
            statement.executeUpdate("create table products(\n" +
                    "prodName varchar(30) unique not null check(prodName <> '') primary key,\n" +
                    "price decimal(10,2) not null check (price >= 0)  \n" +
                    ")");
            statement.executeUpdate("create table purchases (\n" +
                    "customer varchar(20) not null check(customer <> ''),\n" +
                    "purchase varchar(30) not null check(purchase <> ''),\n" +
                    "acquireDate date not null,\n" +
                    "foreign key (purchase) references products(prodName),\n" +
                    "foreign key (customer) references customers(lastName)\n" +
                    ")");
            System.out.println("Пожалуйста заполните базу данных из дампа и нажимте Enter по готовности");
            scanner.nextLine();
        } catch (Exception ignore) {
        }
    }

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        Connection connection;
        try (InputStream is = Files.newInputStream(
                Paths.get(String.valueOf(connAttr)))) {
            props.load(is);
            String drivers = props.getProperty("jdbc.drivers");
            if (drivers != null)
                System.setProperty("jdbc.drivers", drivers);
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            Error.setCause("Некорректные параметры соединения с базой данных.Проверьте правильность параметров файла кофигурации.");
            JsonWriter.writeToJsonFile(IncomeHandler.getWriteDst(), "error");
            throw new SQLSyntaxErrorException();
        }
        return connection;
    }

}
