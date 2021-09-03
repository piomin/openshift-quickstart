package pl.redhat.samples.eventdriven.streams.producer.message;

public class Order {

    private Integer id;
    private String status;
    private Integer customerId;

    public Order() {
    }

    public Order(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Order(Integer id, String status, Integer customerId) {
        this.id = id;
        this.status = status;
        this.customerId = customerId;
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
}
