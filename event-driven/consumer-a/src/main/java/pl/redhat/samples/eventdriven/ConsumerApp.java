package pl.redhat.samples.eventdriven;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.message.CallmeEvent;

import java.util.function.Consumer;

@SpringBootApplication
public class ConsumerApp {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }

    @Bean
    public Consumer<CallmeEvent> eventConsumer() {
        return event -> LOG.info("Received: {}", event);
    }
}
