package pl.redhat.samples.eventdriven.shipment.message;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class OrderCommandDelayed implements Delayed {

    private OrderCommand orderCommand;
    private long startTime;

    public OrderCommandDelayed(OrderCommand orderCommand, long delayInMilliseconds) {
        this.orderCommand = orderCommand;
        this.startTime = System.currentTimeMillis() + delayInMilliseconds;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.startTime - ((OrderCommandDelayed) o).startTime);
    }

    public OrderCommand getOrderCommand() {
        return orderCommand;
    }
}

