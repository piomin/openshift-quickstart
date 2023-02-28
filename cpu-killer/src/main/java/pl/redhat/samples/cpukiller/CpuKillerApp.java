package pl.redhat.samples.cpukiller;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import pl.redhat.samples.cpukiller.message.PingEvent;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;
import java.util.Random;

@ApplicationScoped
public class CpuKillerApp {

    Logger log;

    public CpuKillerApp(Logger log) {
        this.log = log;
    }

    @Incoming("events")
    void onEvent(PingEvent event) {
        log.infof("New event: id=%d", event.getId());
        BigInteger bi = new BigInteger(1000, 10, new Random());
        event.setBigNumber(bi.toString());
        log.info("Computed: " + event);
    }
}
