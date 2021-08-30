package pl.redhat.samples.eventdriven.shipment.message;

public class ConfirmCommand extends AbstractOrderCommand {

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
