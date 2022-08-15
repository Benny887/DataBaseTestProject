package database;

public class SearchQueries {
    private static final String LASTNAME_QUERY = "select * from customers where lastName = ?";
    private static final String PRODUCT_AND_COUNT_QUERY = "select c.firstName, c.lastName from customers c\n" +
            "inner join (select customer from purchases where purchase = ? \n" +
            "group by customer having count(*) >= ?) cp on c.lastName = cp.customer";
    private static final String MIN_MAX_QUERY = "select c.firstName, c.lastName from customers c\n" +
            "inner join  (select pu.customer, sum(pr.price) allPurch\n" +
            "from purchases pu join products pr on pu.purchase = pr.prodName\n" +
            "group by pu.customer having sum(pr.price) between ? and ?) dog\n" +
            "on c.lastName = dog.customer";
    private static final String BAD_CUSTOMERS_QUERY = "select c.firstName, c.lastName from customers c " +
            "inner join (select p.customer\n" +
            "from purchases p group by p.customer having count(*) = (select min(cou) " +
            "from (select pu.customer p , count(*) cou from purchases pu " +
            "group by pu.customer) cu) limit ?) " +
            "as kot on c.lastName = kot.customer";

    public static String getLastnameQuery() {
        return LASTNAME_QUERY;
    }

    public static String getProductAndCountQuery() {
        return PRODUCT_AND_COUNT_QUERY;
    }

    public static String getMinMaxQuery() {
        return MIN_MAX_QUERY;
    }

    public static String getBadCustomersQuery() {
        return BAD_CUSTOMERS_QUERY;
    }
}
