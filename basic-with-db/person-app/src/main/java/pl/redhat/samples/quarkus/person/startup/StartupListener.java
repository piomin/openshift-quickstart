package pl.redhat.samples.quarkus.person.startup;

import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class StartupListener {

    @Inject
    Logger log;

    public void event(@Observes StartupEvent ev) {
        final long delay = 0;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.infof("Application startup - %d ms delay", delay);
    }
}
