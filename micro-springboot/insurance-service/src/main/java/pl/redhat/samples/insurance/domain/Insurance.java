package pl.redhat.samples.insurance.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer personId;
    @Enumerated(EnumType.STRING)
    private InsuranceType type;
    private int amount;
    @Temporal(TemporalType.DATE)
    private Date expiry;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
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
                "id=" + id +
                ", personId=" + personId +
                ", type=" + type +
                ", amount=" + amount +
                ", expiry=" + expiry +
                '}';
    }
}
