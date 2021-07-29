package pl.redhat.samples.organization.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.redhat.samples.organization.model.Department;
import pl.redhat.samples.organization.model.Employee;
import pl.redhat.samples.organization.model.Organization;
import pl.redhat.samples.organization.repository.OrganizationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class OrganizationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    OrganizationRepository repository;
    @Autowired
    RestTemplate restTemplate;

    @PostMapping
    public Organization add(@RequestBody Organization organization) {
        LOGGER.info("Organization add: {}", organization);
        return repository.save(organization);
    }

    @GetMapping
    public Iterable<Organization> findAll() {
        LOGGER.info("Organization find");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Organization findById(@PathVariable("id") Long id) {
        LOGGER.info("Organization find: id={}", id);
        return repository.findById(id).orElseThrow();
    }

    @GetMapping("/{id}/with-departments")
    public Organization findByIdWithDepartments(@PathVariable("id") Long id) {
        LOGGER.info("Organization find: id={}", id);
        Optional<Organization> organization = repository.findById(id);
        if (organization.isPresent()) {
            Organization o = organization.get();
            o.setDepartments(findDepartmentsByOrganization(o.getId()));
            return o;
        } else {
            return null;
        }
    }

    @GetMapping("/{id}/with-departments-and-employees")
    public Organization findByIdWithDepartmentsAndEmployees(@PathVariable("id") Long id) {
        LOGGER.info("Organization find: id={}", id);
        Optional<Organization> organization = repository.findById(id);
        if (organization.isPresent()) {
            Organization o = organization.get();
            o.setDepartments(findDepartmentsByOrganizationWithEmployees(o.getId()));
            return o;
        } else {
            return null;
        }
    }

    @GetMapping("/{id}/with-employees")
    public Organization findByIdWithEmployees(@PathVariable("id") Long id) {
        LOGGER.info("Organization find: id={}", id);
        Optional<Organization> organization = repository.findById(id);
        if (organization.isPresent()) {
            Organization o = organization.get();
            o.setEmployees(findEmployeesByOrganization(o.getId()));
            return o;
        } else {
            return null;
        }
    }

    private List<Employee> findEmployeesByOrganization(Long organizationId) {
        Employee[] employees = restTemplate.getForObject("http//employee:8080/organization/{organizationId}",
                Employee[].class, organizationId);
        return Arrays.asList(employees);
    }

    private List<Department> findDepartmentsByOrganization(Long organizationId) {
        Department[] departments = restTemplate.getForObject("http//department:8080/organization/{organizationId}",
                Department[].class, organizationId);
        return Arrays.asList(departments);
    }

    private List<Department> findDepartmentsByOrganizationWithEmployees(Long organizationId) {
        Department[] departments = restTemplate.getForObject("http//department:8080/organization/{organizationId}/with-employees",
                Department[].class, organizationId);
        return Arrays.asList(departments);
    }
}
