package criterias.statDTOs;

public class Purchase {
    private String name;
    private double expenses;

    public Purchase() {
    }

    public Purchase(String name, double expenses) {
        this.name = name;
        this.expenses = expenses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }
}
