package pl.redhat.samples.quarkus.insurance.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Insurance extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long personId;
    @Enumerated(EnumType.STRING)
    public InsuranceType type;
    public int amount;
    public Date expiry;
}
