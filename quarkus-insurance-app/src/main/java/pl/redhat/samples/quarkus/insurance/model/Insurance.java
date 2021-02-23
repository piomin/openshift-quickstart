package pl.redhat.samples.quarkus.insurance.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Insurance extends PanacheEntity {
    private Long personId;
    private InsuranceType type;
    private int amount;
    private Date expiry;
}
