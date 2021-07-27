package pl.redhat.samples.insurance.repository;

import org.springframework.data.repository.CrudRepository;
import pl.redhat.samples.insurance.domain.Insurance;

public interface InsuranceRepository extends CrudRepository<Insurance, Integer> {
}
