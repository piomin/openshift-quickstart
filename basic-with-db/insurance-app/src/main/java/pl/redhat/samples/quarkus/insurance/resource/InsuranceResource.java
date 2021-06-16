package pl.redhat.samples.quarkus.insurance.resource;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.redhat.samples.quarkus.insurance.client.PersonService;
import pl.redhat.samples.quarkus.insurance.model.Insurance;
import pl.redhat.samples.quarkus.insurance.model.InsuranceDetails;
import pl.redhat.samples.quarkus.insurance.repository.InsuranceRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/insurances")
public class InsuranceResource {

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
    public InsuranceDetails getInsuranceDetailsById(@PathParam("id") Long id) {
        Insurance insurance = insuranceRepository.findById(id);
        InsuranceDetails insuranceDetails = new InsuranceDetails();
        insuranceDetails.personId = insurance.personId;
        insuranceDetails.amount = insurance.amount;
        insuranceDetails.type = insurance.type;
        insuranceDetails.expiry = insurance.expiry;
        insuranceDetails.setPerson(personService.getPersonById(insurance.personId));
        return insuranceDetails;
    }

}
