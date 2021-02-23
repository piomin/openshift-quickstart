package pl.redhat.samples.quarkus.insurance.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Insurance extends PanacheEntity {
    private Long personId;
    @Enumerated(EnumType.STRING)
    private InsuranceType type;
    private int amount;
    private Date expiry;
}
