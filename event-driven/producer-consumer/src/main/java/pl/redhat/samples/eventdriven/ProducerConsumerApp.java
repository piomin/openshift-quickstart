package pl.redhat.samples.eventdriven;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.message.CallmeEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootApplication
public class ProducerConsumerApp {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerConsumerApp.class);
    private static int id = 0;

    public static void main(String[] args) {
        SpringApplication.run(ProducerConsumerApp.class, args);
    }

    @Bean
    public Supplier<CallmeEvent> eventSupplier() {
        return () -> new CallmeEvent(++id, "Hello" + id, "PING");
    }

    @Bean
    public Consumer<CallmeEvent> eventConsumer() {
        return event -> LOG.info("Received: {}", event);
    }

}
