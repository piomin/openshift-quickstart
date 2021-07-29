package pl.redhat.samples.organization.repository;

import org.springframework.data.repository.CrudRepository;
import pl.redhat.samples.organization.model.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {

}
