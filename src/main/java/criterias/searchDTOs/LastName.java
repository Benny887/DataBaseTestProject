package criterias.searchDTOs;

public class LastName {
    private String firstName;
    private String lastName;

    public LastName() {
    }

    public LastName(String lastName,String firstName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
