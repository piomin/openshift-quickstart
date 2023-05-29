package pl.redhat.samples.cpukiller;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import pl.redhat.samples.cpukiller.message.PingEvent;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class CpuKillerApp {

    AtomicLong counter = new AtomicLong();
    Logger log;

    public CpuKillerApp(Logger log) {
        this.log = log;
    }

    ExecutorService executor = Executors.newFixedThreadPool(15);

    @Incoming("events")
    void onEvent(ConsumerRecord<String, PingEvent> event) {
        executor.submit(() -> {
            long id = counter.incrementAndGet();
            log.infof("New event: id=%d, partition=%d", id, event.partition());
            BigInteger bi = new BigInteger(1000, 10, new Random());
            PingEvent ping = event.value();
            ping.setId(id);
            ping.setBigNumber(bi.toString());
            log.info("Computed: " + ping);
        });
    }
}
