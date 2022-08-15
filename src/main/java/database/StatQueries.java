package database;

public class StatQueries {
    private static final String ALL_CUSTOMERS_PURCHASES_QUERY = "select c.firstName, c.lastName, p.purchase, sum(pr.price) total\n" +
            "from purchases p\n" +
            "join products pr on p.purchase = pr.prodName\n" +
            "join customers c on p.customer = c.lastName\n" +
            "where acquireDate between ? and ?\n" +
            "      and extract(dow from acquireDate) not in (0,6) \n" +
            "group by c.firstName, c.lastName, p.purchase\n" +
            "order by total desc";
    private static final String ONE_CUSTOMER_EXPENSES_QUERY = "select sum(pr.price)\n" +
            "from purchases p\n" +
            "join products pr on p.purchase = pr.prodName\n" +
            "where customer = ? \n" +
            "and acquireDate between ? and ? \n" +
            "and extract(dow from acquireDate) not in (0,6)";
    private static final String DAYS_WITHOUT_HOLIDAYS_QUERY = "select count(*) " +
            "from (select EXTRACT(DOW FROM s.d::date) as dd " +
            "from generate_series(?::DATE, ?::DATE , '1 day') " +
            "AS s(d)) t where dd not in(0,6)";
    private static final String ALL_CUSTOMERS_EXPENSES_QUERY = "select sum(pr.price) total\n" +
            "from purchases p\n" +
            "join products pr on p.purchase = pr.prodName\n" +
            "and acquireDate between ? and ?\n" +
            "and extract(dow from acquireDate) not in (0,6)";
    private static final String AVG_EXPENSES_QUERY = "select avg(pr.price) total\n" +
            "from purchases p\n" +
            "join products pr on p.purchase = pr.prodName\n" +
            "where acquireDate between ? and ?\n" +
            "and extract(dow from acquireDate) not in (0,6)";

    public static String getAllCustomersPurchasesQuery() {
        return ALL_CUSTOMERS_PURCHASES_QUERY;
    }

    public static String getOneCustomerExpensesQuery() {
        return ONE_CUSTOMER_EXPENSES_QUERY;
    }

    public static String getDaysWithoutHolidaysQuery() {
        return DAYS_WITHOUT_HOLIDAYS_QUERY;
    }

    public static String getAllCustomersExpensesQuery() {
        return ALL_CUSTOMERS_EXPENSES_QUERY;
    }

    public static String getAvgExpensesQuery() {
        return AVG_EXPENSES_QUERY;
    }
}
