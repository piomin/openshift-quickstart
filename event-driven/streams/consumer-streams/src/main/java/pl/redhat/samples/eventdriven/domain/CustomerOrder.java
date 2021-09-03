package pl.redhat.samples.eventdriven.domain;

public class CustomerOrder {

    private Integer id;
    private Integer customerId;
    private String customerName;
    private String status;

    public CustomerOrder() {
    }

    public CustomerOrder(Integer id, Integer customerId, String customerName, String status) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
