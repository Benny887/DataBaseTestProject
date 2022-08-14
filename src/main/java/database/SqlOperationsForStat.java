package database;

import start.JsonMaker;

import java.io.IOException;
import java.sql.*;


public class SqlOperationsForStat {

    private static final String daysOutOfHolidays = "select count(*) from purchases\n" +
            "where acquireDate between '2021-01-11' and '2021-01-17' \n" +
            "      and dayname(acquireDate) not in ('Sunday','Saturday')";

    private static final String allPurchasesInPeriod = "select p.purchase, sum(pr.price) total\n" +
            "from purchases p join products pr on p.purchase = pr.prodName\n" +
            "where customer = 'Вентура' and acquireDate between '2021-01-11' and '2021-01-14' \n" +
            "group by p.purchase order by total desc";

    private static final String totalPriceOfOneCustomer = "select sum(pr.price)\n" +
            "from purchases p join products pr on p.purchase = pr.prodName\n" +
            "where customer = 'Фет' and acquireDate between '2021-01-12' and '2021-01-14' ";

    private static final String totalPriceOfAllCustomers = "select sum(pr.price) total\n" +
            "from purchases p join products pr on p.purchase = pr.prodName\n" +
            "and acquireDate between '2021-01-12' and '2021-01-14' ";

    private static final String averageExpensesOfAllCustomers = "select avg(pr.price) total\n" +
            "from purchases p join products pr on p.purchase = pr.prodName\n" +
            "and acquireDate between '2021-01-12' and '2021-01-14'";

    public void getSqlDataForStat(Date fromDate, Date toDate, String lastName) throws SQLException, IOException {

        PreparedStatement stat;
        ResultSet result;
        try (Connection connection = InitialTables.getConnection();
             ) {

                    stat = connection.prepareStatement("select count(*) from purchases " +
                            "where acquireDate between ? and ? and dayname(acquireDate) " +
                            "not in ('Sunday','Saturday')");
                    stat.setDate(1, fromDate);
                    stat.setDate(2,toDate);
                    result = stat.executeQuery();
                        int s = result.getInt(1);

                    stat = connection.prepareStatement("select p.purchase, sum(pr.price) total " +
                            "from purchases p join products pr on p.purchase = pr.prodName" +
                            "where customer = ? and acquireDate between ? and ? " +
                            "group by p.purchase order by total desc");
                    stat.setString(1, lastName);
                    stat.setDate(2, fromDate);
                    stat.setDate(3, toDate);

                    stat = connection.prepareStatement("select sum(pr.price) from purchases p " +
                            "join products pr on p.purchase = pr.prodName" +
                            "where customer = ? and acquireDate between ? and ? ");
                    stat.setString(1, lastName);
                    stat.setDate(2, fromDate);
                    stat.setDate(3, toDate);

                    stat = connection.prepareStatement("select sum(pr.price) total" +
                            "from purchases p join products pr on p.purchase = pr.prodName" +
                            "and acquireDate between ? and ? ");
                    stat.setDate(1, fromDate);
                    stat.setDate(2, toDate);

                    stat = connection.prepareStatement("select avg(pr.price) total " +
                            "from purchases p join products pr on p.purchase = pr.prodName" +
                            "and acquireDate between ? and ?");
                    stat.setDate(1, fromDate);
                    stat.setDate(2, toDate);
            }


            stat.close();
        }
    }

