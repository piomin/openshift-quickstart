package pl.redhat.samples.eventdriven.order.message;

public class ConfirmCommand extends AbstractOrderCommand {

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
