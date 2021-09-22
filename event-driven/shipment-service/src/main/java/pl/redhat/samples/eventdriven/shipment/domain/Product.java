package pl.redhat.samples.eventdriven.shipment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int currentCount;
    private int reservedCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public int getReservedCount() {
        return reservedCount;
    }

    public void setReservedCount(int reservedCount) {
        this.reservedCount = reservedCount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", currentCount=" + currentCount +
                ", reservedCount=" + reservedCount +
                '}';
    }
}
