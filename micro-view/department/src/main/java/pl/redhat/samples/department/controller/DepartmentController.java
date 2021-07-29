package pl.redhat.samples.department.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.redhat.samples.department.model.Department;
import pl.redhat.samples.department.model.Employee;
import pl.redhat.samples.department.repository.DepartmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    DepartmentRepository repository;
    RestTemplate restTemplate;

    public DepartmentController(DepartmentRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/")
    public Department add(@RequestBody Department department) {
        LOGGER.info("Department add: {}", department);
        return repository.save(department);
    }

    @GetMapping("/{id}")
    public Department findById(@PathVariable("id") Long id) {
        LOGGER.info("Department find: id={}", id);
        return repository.findById(id).get();
    }

    @GetMapping("/{id}/with-employees")
    public Department findByIdWithEmployees(@PathVariable("id") Long id) {
        LOGGER.info("Department findByIdWithEmployees: id={}", id);
        Optional<Department> optDepartment = repository.findById(id);
        if (optDepartment.isPresent()) {
            Department department = optDepartment.get();
            department.setEmployees(findEmployeesByDepartment(department.getId()));
            return department;
        }
        return null;
    }

    @GetMapping("/")
    public Iterable<Department> findAll() {
        LOGGER.info("Department find");
        return repository.findAll();
    }

    @GetMapping("/organization/{organizationId}")
    public List<Department> findByOrganization(@PathVariable("organizationId") String organizationId) {
        LOGGER.info("Department find: organizationId={}", organizationId);
        return repository.findByOrganizationId(organizationId);
    }

    @GetMapping("/organization/{organizationId}/with-employees")
    public List<Department> findByOrganizationWithEmployees(@PathVariable("organizationId") String organizationId) {
        LOGGER.info("Department find: organizationId={}", organizationId);
        List<Department> departments = repository.findByOrganizationId(organizationId);
        departments.forEach(d -> d.setEmployees(findEmployeesByDepartment(d.getId())));
        return departments;
    }

    private List<Employee> findEmployeesByDepartment(Long departmentId) {
        Employee[] employees = restTemplate
                .getForObject("http://employee:8080//department/{departmentId}", Employee[].class, departmentId);
        return Arrays.asList(employees);
    }

}
