package pl.redhat.samples.eventdriven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.message.CallmeEvent;

import java.util.function.Supplier;

@SpringBootApplication
public class ProducerApp {

    private static int id = 0;

    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }

    @Bean
    public Supplier<CallmeEvent> eventSupplier() {
        return () -> new CallmeEvent(++id, "Hello" + id, "PING");
    }

}

