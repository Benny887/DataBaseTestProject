package criterias.stat;

import java.util.Date;

public class DateForStat {
    private Date startDate;
    private Date endDate;

    public DateForStat(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateForStat() {
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DateForStat{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
