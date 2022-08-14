import database.InitialTables;
import start.IncomeHandler;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
//        InitialTables.makeTable();
        IncomeHandler incomeHandler = new IncomeHandler();
        incomeHandler.handleIncomeData(args);
    }
}
