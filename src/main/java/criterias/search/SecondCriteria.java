package criterias.search;

import java.util.List;

public class SecondCriteria implements Criteria{
    private MinTimesPurchase criteria;

    private List<LastName> results;

    public SecondCriteria(MinTimesPurchase criteria, List<LastName> results) {
        this.criteria = criteria;
        this.results = results;
    }

    public SecondCriteria() {
    }

    public MinTimesPurchase getCriteria() {
        return criteria;
    }

    public void setCriteria(MinTimesPurchase criteria) {
        this.criteria = criteria;
    }

    public List<LastName> getResults() {
        return results;
    }

    public void setResults(List<LastName> results) {
        this.results = results;
    }
}
