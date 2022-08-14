package criterias.statDTOs;

import java.util.List;

public class Customer {
    private String name;
    private List<Purchase> purchases;
    private int totalExpanses;

    public Customer() {
    }

    public Customer(String name, List<Purchase> purchases, int totalExpanses) {
        this.name = name;
        this.purchases = purchases;
        this.totalExpanses = totalExpanses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public int getTotalExpanses() {
        return totalExpanses;
    }

    public void setTotalExpanses(int totalExpanses) {
        this.totalExpanses = totalExpanses;
    }
}
