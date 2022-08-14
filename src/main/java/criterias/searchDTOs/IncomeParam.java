package criterias.searchDTOs;

public class IncomeParam {
    private String lastName;

    public IncomeParam(String lastName) {
        this.lastName = lastName;
    }

    public IncomeParam() {
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
