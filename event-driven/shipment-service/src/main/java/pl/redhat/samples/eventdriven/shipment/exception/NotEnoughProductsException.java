package pl.redhat.samples.eventdriven.shipment.exception;

public class NotEnoughProductsException extends RuntimeException {

    public NotEnoughProductsException() {
        super("Not enough products");
    }
}
