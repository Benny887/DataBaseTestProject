package criterias.searchDTOs;

import java.util.List;

public class FourthCriteria implements Criteria{
    private BadCustomer criteria;

    private List<LastName> results;

    public FourthCriteria(BadCustomer criteria, List<LastName> results) {
        this.criteria = criteria;
        this.results = results;
    }

    public FourthCriteria() {
    }

    public BadCustomer getCriteria() {
        return criteria;
    }

    public void setCriteria(BadCustomer criteria) {
        this.criteria = criteria;
    }

    public List<LastName> getResults() {
        return results;
    }

    public void setResults(List<LastName> results) {
        this.results = results;
    }
}
