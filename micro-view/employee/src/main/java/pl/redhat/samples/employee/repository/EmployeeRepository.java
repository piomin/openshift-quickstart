package pl.redhat.samples.employee.repository;

import org.springframework.data.repository.CrudRepository;
import pl.redhat.samples.employee.model.Employee;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, String> {

    List<Employee> findByDepartmentId(String departmentId);
    List<Employee> findByOrganizationId(String organizationId);

}
