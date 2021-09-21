package pl.redhat.samples.eventdriven;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerStreamsApp {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerStreamsApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerStreamsApp.class, args);
    }

}
