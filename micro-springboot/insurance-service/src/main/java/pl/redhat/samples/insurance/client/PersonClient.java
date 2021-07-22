package pl.redhat.samples.insurance.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.redhat.samples.insurance.client.message.Person;

@Service
public class PersonClient {

    @Value("${PERSON_SERVICE_URL}")
    private String personServiceUrl;
    private RestTemplate restTemplate;

    public PersonClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Person getPersonById(Long id) {
        return restTemplate.getForObject(personServiceUrl + "/persons/{id}", Person.class, id);
    }
}
