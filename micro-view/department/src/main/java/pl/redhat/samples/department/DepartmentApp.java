package pl.redhat.samples.department;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class DepartmentApp {

    public static void main(String[] args) {
        SpringApplication.run(DepartmentApp.class, args);
    }

    @Bean
    RestClient restClient() {
        return RestClient.builder().build();
    }
}
