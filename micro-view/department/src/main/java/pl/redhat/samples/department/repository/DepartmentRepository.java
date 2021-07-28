package pl.redhat.samples.department.repository;

import org.springframework.data.repository.CrudRepository;
import pl.redhat.samples.department.model.Department;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

    List<Department> findByOrganizationId(String organizationId);

}
