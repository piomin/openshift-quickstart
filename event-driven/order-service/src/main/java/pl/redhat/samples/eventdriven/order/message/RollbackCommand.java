package pl.redhat.samples.eventdriven.order.message;

public class RollbackCommand extends AbstractOrderCommand {

    private String orderId;
    private String source;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
