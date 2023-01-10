package pl.redhat.samples.insurance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.redhat.samples.insurance.client.PersonClient;
import pl.redhat.samples.insurance.client.message.Person;
import pl.redhat.samples.insurance.domain.Insurance;
import pl.redhat.samples.insurance.domain.InsuranceDetails;
import pl.redhat.samples.insurance.domain.InsuranceView;
import pl.redhat.samples.insurance.repository.InsuranceRepository;
import pl.redhat.samples.insurance.repository.InsuranceViewRepository;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/insurances")
public class InsuranceController {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceController.class);
    private PersonClient personClient;
    private InsuranceRepository repository;
    private InsuranceViewRepository viewRepository;

    public InsuranceController(PersonClient personClient, InsuranceRepository repository, InsuranceViewRepository viewRepository) {
        this.personClient = personClient;
        this.repository = repository;
        this.viewRepository = viewRepository;
    }

    @GetMapping
    public List<Insurance> getAll() {
        LOG.info("Get all insurances");
        return (List<Insurance>) repository.findAll();
    }

    @GetMapping("/{id}")
    public InsuranceView getById(@PathVariable("id") Integer id) {
        LOG.info("Get insurance by id={}", id);
        return viewRepository.findOne(id);
    }

    @PostMapping
    public Insurance addNew(@RequestBody Insurance insurance) {
        LOG.info("Add new insurance: {}", insurance);
        return repository.save(insurance);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        LOG.info("Delete insurance by id={}", id);
        repository.deleteById(id);
    }

    @GetMapping("/{id}/details")
    public InsuranceDetails getInsuranceDetailsById(@PathParam("id") Integer id) {
        Insurance insurance = repository.findById(id).orElseThrow();
        Person person = personClient.getPersonById(insurance.getId());
        // TODO - detect why person==null
        // TODO - finish implementation
        return null;
    }
}
