package pl.redhat.samples.eventdriven.payment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer customerId;
    private int currentAmount;
    private int reservedAmount;

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

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(int reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", currentAmount=" + currentAmount +
                ", reservedAmount=" + reservedAmount +
                '}';
    }
}
