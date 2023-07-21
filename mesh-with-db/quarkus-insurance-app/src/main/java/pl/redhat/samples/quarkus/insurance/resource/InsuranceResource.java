package pl.redhat.samples.quarkus.insurance.resource;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import pl.redhat.samples.quarkus.insurance.client.PersonService;
import pl.redhat.samples.quarkus.insurance.model.Insurance;
import pl.redhat.samples.quarkus.insurance.model.InsuranceDetails;
import pl.redhat.samples.quarkus.insurance.repository.InsuranceRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import java.util.List;

@Path("/insurances")
public class InsuranceResource {

    @Inject
    Logger log;
    @Inject
    InsuranceRepository insuranceRepository;
    @Inject @RestClient
    PersonService personService;

    @POST
    @Transactional
    public Insurance addInsurance(Insurance insurance) {
        insuranceRepository.persist(insurance);
        return insurance;
    }

    @GET
    public List<Insurance> getInsurances() {
        return insuranceRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Insurance getInsuranceById(@PathParam("id") Long id) {
        return insuranceRepository.findById(id);
    }

    @GET
    @Path("/{id}/details")
    public InsuranceDetails getInsuranceDetailsById(@PathParam("id") Long id, @HeaderParam("X-Version") String version) {
        log.infof("getInsuranceDetailsById: id=%d, version=%s", id, version);
        Insurance insurance = insuranceRepository.findById(id);
        InsuranceDetails insuranceDetails = new InsuranceDetails();
        insuranceDetails.setPersonId(insurance.getPersonId());
        insuranceDetails.setAmount(insurance.getAmount());
        insuranceDetails.setType(insurance.getType());
        insuranceDetails.setExpiry(insurance.getExpiry());
        insuranceDetails.setPerson(personService.getPersonById(insurance.getPersonId()));
        return insuranceDetails;
    }

}
