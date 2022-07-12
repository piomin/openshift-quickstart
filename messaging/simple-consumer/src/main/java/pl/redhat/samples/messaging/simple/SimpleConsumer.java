package pl.redhat.samples.messaging.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleConsumer {

    public static void main(String[] args) {
        SpringApplication.run(SimpleConsumer.class, args);
    }

}
