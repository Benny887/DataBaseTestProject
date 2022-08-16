package database;

public class InitialTablesQueries {
    private static final String CREATE_TABLE_CUSTOMERS = "create table customers(\n" +
            "firstName varchar(15) not null check (firstName <> ''),\n" +
            "lastName varchar(20) not null check (lastName <> '')  primary key\n" +
            ")";
    private static final String CREATE_TABLE_PRODUCTS = "create table products(\n" +
            "prodName varchar(30) unique not null check(prodName <> '') primary key,\n" +
            "price int not null check (price >= 0)  \n" +
            ")";

    private static final String CREATE_TABLE_PURCHASES = "create table purchases (\n" +
            "customer varchar(20) not null check(customer <> ''),\n" +
            "purchase varchar(30) not null check(purchase <> ''),\n" +
            "acquireDate date not null,\n" +
            "foreign key (purchase) references products(prodName),\n" +
            "foreign key (customer) references customers(lastName)\n" +
            ")";

    public static String getCreateTableCustomers() {
        return CREATE_TABLE_CUSTOMERS;
    }

    public static String getCreateTableProducts() {
        return CREATE_TABLE_PRODUCTS;
    }

    public static String getCreateTablePurchases() {
        return CREATE_TABLE_PURCHASES;
    }
}
