package pl.redhat.samples.insurance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.redhat.samples.insurance.client.PersonClient;
import pl.redhat.samples.insurance.client.message.Person;
import pl.redhat.samples.insurance.domain.Insurance;
import pl.redhat.samples.insurance.domain.InsuranceDetails;
import pl.redhat.samples.insurance.repository.InsuranceRepository;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/insurances")
public class InsuranceController {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceController.class);
    private PersonClient personClient;
    private InsuranceRepository repository;

    public InsuranceController(PersonClient personClient, InsuranceRepository repository) {
        this.personClient = personClient;
        this.repository = repository;
    }

    @GetMapping
    public List<Insurance> getAll() {
        LOG.info("Get all insurances");
        return (List<Insurance>) repository.findAll();
    }

    @GetMapping("/{id}")
    public Insurance getById(@PathVariable("id") Integer id) {
        LOG.info("Get insurance by id={}", id);
        return repository.findById(id).orElseThrow();
    }

    @PostMapping
    public Insurance addNew(@RequestBody Insurance insurance) {
        LOG.info("Add new insurance: {}", insurance);
        return repository.save(insurance);
    }

    @GetMapping("/{id}/details")
    public InsuranceDetails getInsuranceDetailsById(@PathVariable("id") Integer id) {
        Insurance insurance = repository.findById(id).orElseThrow();
        Person person = personClient.getPersonById(insurance.getId());
        LOG.info("Get insurance by details id={}", id);
        // TODO - detect why person==null
        // TODO - finish implementation
        return null;
    }
}
