package pl.redhat.samples.insurance.repository;

import com.blazebit.persistence.spring.data.repository.EntityViewRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.redhat.samples.insurance.domain.InsuranceView;

@Transactional(readOnly = true)
public interface InsuranceViewRepository extends EntityViewRepository<InsuranceView, Integer> {
}
