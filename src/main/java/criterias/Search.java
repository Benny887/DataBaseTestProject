package criterias;

import criterias.searchDTOs.Criteria;
import java.util.List;

public class Search {
    private String type;

    List<Criteria> results;

    public Search(String type, List<Criteria> results) {
        this.type = type;
        this.results = results;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Criteria> getResults() {
        return results;
    }

    public void setResults(List<Criteria> results) {
        this.results = results;
    }
}
