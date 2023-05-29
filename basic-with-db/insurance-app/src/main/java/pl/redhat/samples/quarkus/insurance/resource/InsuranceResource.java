package pl.redhat.samples.quarkus.insurance.resource;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.redhat.samples.quarkus.insurance.client.PersonService;
import pl.redhat.samples.quarkus.insurance.client.message.Person;
import pl.redhat.samples.quarkus.insurance.model.Insurance;
import pl.redhat.samples.quarkus.insurance.model.InsuranceDetails;
import pl.redhat.samples.quarkus.insurance.repository.InsuranceRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
        Person person = personService.getPersonById(insurance.personId);
        return new InsuranceDetails(person, insurance);
    }

}
