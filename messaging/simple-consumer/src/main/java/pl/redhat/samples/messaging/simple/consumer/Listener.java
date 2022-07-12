package pl.redhat.samples.messaging.simple.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class Listener {

    private static final Logger LOG = LoggerFactory.getLogger(Listener.class);

    @JmsListener(destination = "example")
    public void processMsg(String message) {
        LOG.info("============= Received: " + message);
    }

}
