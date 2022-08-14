package criterias.searchDTOs;

import java.util.List;

public class FirstCriteria implements Criteria{
    private Name criteria;

    private List<LastName> results;

    public FirstCriteria(Name criteria, List<LastName> results) {
        this.criteria = criteria;
        this.results = results;
    }

    public FirstCriteria() {
    }

    public Name getCriteria() {
        return criteria;
    }

    public void setCriteria(Name criteria) {
        this.criteria = criteria;
    }

    public List<LastName> getResults() {
        return results;
    }

    public void setResults(List<LastName> results) {
        this.results = results;
    }
}
