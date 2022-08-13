package criterias.search;

public class MinTimesPurchase {
    private String productName;
    int minTimes;

    public MinTimesPurchase(String productName, int minTimes) {
        this.productName = productName;
        this.minTimes = minTimes;
    }

    public MinTimesPurchase() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getMinTimes() {
        return minTimes;
    }

    public void setMinTimes(int minTimes) {
        this.minTimes = minTimes;
    }
}
