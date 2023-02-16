package pl.redhat.samples.messaging.simple.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import pl.redhat.samples.messaging.simple.message.SimpleMessage;

import java.util.concurrent.BlockingQueue;

@Service
public class Listener {

    private static final Logger LOG = LoggerFactory.getLogger(Listener.class);
    private BlockingQueue<SimpleMessage> events;

    public Listener(BlockingQueue<SimpleMessage> events) {
        this.events = events;
    }

    @JmsListener(destination = "test-1")
    public void processMsg(SimpleMessage message) throws InterruptedException {
        LOG.info("============= Received: " + message);
        events.put(message);
    }

}
