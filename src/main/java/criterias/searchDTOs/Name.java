package criterias.searchDTOs;

public class Name {
    String lastName;

    public Name(String name) {
        this.lastName = name;
    }

    public Name() {
    }

    public String getCriteria() {
        return lastName;
    }

    public void setCriteria(String criteria) {
        this.lastName = criteria;
    }
}
