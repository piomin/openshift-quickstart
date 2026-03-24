package pl.redhat.samples.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class OrganizationApp {

    public static void main(String[] args) {
        SpringApplication.run(OrganizationApp.class, args);
    }

    @Bean
    RestClient restClient() {
        return RestClient.builder().build();
    }
}
