import database.InitialTables;
import start.IncomeHandler;


public class Main {
    public static void main(String[] args) {
        InitialTables.makeTable();
        IncomeHandler incomeHandler = new IncomeHandler();
        incomeHandler.handleIncomeData(args);
    }
}
