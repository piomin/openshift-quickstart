package pl.redhat.samples.insurance.domain;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import java.util.Date;

@EntityView(Insurance.class)
public interface InsuranceView {
    @IdMapping
    Integer getId();
    Integer getPersonId();
    InsuranceType getType();
    int getAmount();
    Date getExpiry();
}
