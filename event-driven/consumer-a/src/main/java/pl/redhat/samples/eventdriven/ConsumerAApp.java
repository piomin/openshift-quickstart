package pl.redhat.samples.eventdriven;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.util.backoff.FixedBackOff;
import pl.redhat.samples.eventdriven.message.CallmeEvent;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

@SpringBootApplication
public class ConsumerAApp {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerAApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerAApp.class, args);
    }

    @Bean
    public Consumer<Message<CallmeEvent>> eventConsumer() {
        return event -> {
            LOG.info("Received: {}", event.getPayload());
//            throw new UnsupportedOperationException();
//            Acknowledgment acknowledgment = event.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
//            if (acknowledgment != null) {
//                LOG.info("Manual Ack");
//                if (event.getPayload().getEventType().equals("COMMIT")) {
//                    acknowledgment.acknowledge();
//                    LOG.info("Committed");
//                }
//            }
        };
    }

    @Bean
    public ListenerContainerCustomizer<AbstractMessageListenerContainer> containerCustomizer() {
        return (container, dest, group) -> container.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(1000, 3)));
    }


}
