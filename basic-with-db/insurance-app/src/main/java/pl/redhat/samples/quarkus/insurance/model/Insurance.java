package pl.redhat.samples.quarkus.insurance.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
public class Insurance extends PanacheEntity {
    public Long personId;
    @Enumerated(EnumType.STRING)
    public InsuranceType type;
    public int amount;
    public Date expiry;
}
