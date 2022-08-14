package database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class InitialTables {
    private static final String separator = File.separator;
    private static final File connAttr = new File(System.getProperty("user.dir") +
            separator + "src" + separator + "main" + separator + "resources" + separator + "db.properties");

    public static void makeTable() throws SQLException, IOException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
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
            try (ResultSet resultSet = statement.executeQuery("select * from customers")) {
                if (resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                }
            }
        }
    }

    static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        try (InputStream is = Files.newInputStream(
                Paths.get(String.valueOf(connAttr)))) {
            props.load(is);
        }
        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null)
            System.setProperty("jdbc.drivers", drivers);
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");
        return DriverManager.getConnection(url, username, password);
    }


}
