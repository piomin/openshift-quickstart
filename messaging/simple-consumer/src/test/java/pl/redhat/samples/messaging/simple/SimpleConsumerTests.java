package pl.redhat.samples.messaging.simple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.redhat.samples.messaging.simple.message.SimpleMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {
   "amqphub.amqp10jms.username=guest",
   "amqphub.amqp10jms.password=guest"
})
@Testcontainers
public class SimpleConsumerTests {

    @Container
    static final GenericContainer artemis = new GenericContainer<>("quay.io/artemiscloud/activemq-artemis-broker:dev.latest")
            .withExposedPorts(5672)
            .withEnv("AMQ_USER", "guest")
            .withEnv("AMQ_PASSWORD", "guest");

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("amqphub.amqp10jms.remoteUrl", () -> "amqp://localhost:" + artemis.getMappedPort(5672));
    }

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    BlockingQueue<SimpleMessage> events;

    @Test
    void listen() throws InterruptedException {
        jmsTemplate.convertAndSend("test-1", new SimpleMessage(1L, "test", "test"));
        SimpleMessage m = events.poll(1000, TimeUnit.MILLISECONDS);
        assertNotNull(m);
        assertEquals(1L, m.getId());
    }
}
