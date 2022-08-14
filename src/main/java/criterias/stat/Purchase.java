package criterias.stat;

public class Purchase {
    private String name;
    private int expenses;

    public Purchase() {
    }

    public Purchase(String name, int expenses) {
        this.name = name;
        this.expenses = expenses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpenses() {
        return expenses;
    }

    public void setExpenses(int expenses) {
        this.expenses = expenses;
    }
}
