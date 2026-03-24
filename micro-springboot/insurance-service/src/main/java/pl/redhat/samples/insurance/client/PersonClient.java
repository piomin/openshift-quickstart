package pl.redhat.samples.insurance.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import pl.redhat.samples.insurance.client.message.Person;

@Service
public class PersonClient {

    @Value("${PERSON_SERVICE_URL:http://localhost}")
    private String personServiceUrl;
    private RestClient restClient;

    public PersonClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Person getPersonById(Integer personId) {
        try {
            return restClient.get()
                    .uri(personServiceUrl + "/persons/{id}", personId)
                    .retrieve()
                    .body(Person.class);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }
}
