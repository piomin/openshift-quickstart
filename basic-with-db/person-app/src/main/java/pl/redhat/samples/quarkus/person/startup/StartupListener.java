package pl.redhat.samples.quarkus.person.startup;

import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

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
