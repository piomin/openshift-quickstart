package pl.redhat.samples.eventdriven.domain;

public class Order {

    private Integer id;
    private String status;
    private Integer customerId;

    public Order() {
    }

    public Order(Integer id, String status, Integer customerId) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}
