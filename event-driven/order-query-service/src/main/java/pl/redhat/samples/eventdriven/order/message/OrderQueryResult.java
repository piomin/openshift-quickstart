package pl.redhat.samples.eventdriven.order.message;

import pl.redhat.samples.eventdriven.order.domain.Order;

import java.util.List;

public class OrderQueryResult {

    private String queryId;
    private List<Order> orders;

    public OrderQueryResult(String queryId, List<Order> orders) {
        this.queryId = queryId;
        this.orders = orders;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
