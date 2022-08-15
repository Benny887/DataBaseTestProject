package criterias.searchDTOs;

import java.util.List;

public class ThirdCriteria implements Criteria {
    private MinMaxExpenses criteria;

    private List<LastName> results;

    public ThirdCriteria(MinMaxExpenses criteria, List<LastName> results) {
        this.criteria = criteria;
        this.results = results;
    }

    public ThirdCriteria() {
    }

    public MinMaxExpenses getCriteria() {
        return criteria;
    }

    public void setCriteria(MinMaxExpenses criteria) {
        this.criteria = criteria;
    }

    public List<LastName> getResults() {
        return results;
    }

    public void setResults(List<LastName> results) {
        this.results = results;
    }
}
