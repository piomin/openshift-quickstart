package pl.redhat.samples.eventdriven.gateway.message;

import java.util.Date;
import java.util.UUID;

public class OrderQuery {

    private String queryId;
    private Integer customerId;
    private Date startDate;
    private Date endDate;

    public OrderQuery() {
    }

    public OrderQuery(String queryId, Integer customerId) {
        this.queryId = queryId;
        this.customerId = customerId;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
}
