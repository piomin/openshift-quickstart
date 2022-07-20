package pl.redhat.samples.messaging.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class SimpleProducer {

    public static void main(String[] args) {
        SpringApplication.run(SimpleProducer.class, args);
    }
}
