package pl.redhat.samples.quarkus.insurance.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Date;

@Entity
public class Insurance extends PanacheEntity {
    private Long personId;
    @Enumerated(EnumType.STRING)
    private InsuranceType type;
    private int amount;
    private Date expiry;

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public InsuranceType getType() {
        return type;
    }

    public void setType(InsuranceType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    @Override
    public String toString() {
        return "Insurance{" +
                "personId=" + personId +
                ", type=" + type +
                ", amount=" + amount +
                ", expiry=" + expiry +
                ", id=" + id +
                '}';
    }
}
