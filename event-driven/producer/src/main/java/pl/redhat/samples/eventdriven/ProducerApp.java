package pl.redhat.samples.eventdriven;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.context.annotation.Bean;
import pl.redhat.samples.eventdriven.message.CallmeEvent;
import pl.redhat.samples.eventdriven.message.LargeEvent;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@SpringBootApplication
public class ProducerApp {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerApp.class);
    private static int id = 0;
    private static final Random RAND = new Random();

    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }

    @Bean
    public Supplier<CallmeEvent> eventSupplier() {
        return () -> {
            int i = RAND.nextInt(8);
            return new CallmeEvent(++id, "Hello" + id, i == 4 ? "COMMIT" : "PING");
        };
    }

//    @Bean
//    public Supplier<List<CallmeEvent>> eventSupplier() {
//        return () -> List.of(
//                new CallmeEvent(++id, "T" + id, "COMMIT"),
//                new CallmeEvent(++id, "T" + id, "COMMIT"),
//                new CallmeEvent(++id, "T" + id, "COMMIT"),
//                new CallmeEvent(++id, "T" + id, "COMMIT"),
//                new CallmeEvent(++id, "T" + id, "COMMIT"));
//    }

//    @Bean
//    public Supplier<LargeEvent> eventSupplier() {
//        return () -> {
//            int size = RAND.nextInt(4000);
//            return new LargeEvent(++id, "PING", 4000 + size);
//        };
//    }

}
