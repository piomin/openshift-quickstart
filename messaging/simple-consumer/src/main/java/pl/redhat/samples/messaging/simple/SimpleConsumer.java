package pl.redhat.samples.messaging.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import pl.redhat.samples.messaging.simple.message.SimpleMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@SpringBootApplication
@EnableJms
public class SimpleConsumer {

    public static void main(String[] args) {
        SpringApplication.run(SimpleConsumer.class, args);
    }

    @Bean
    BlockingQueue<SimpleMessage> events() {
        return new LinkedBlockingDeque<>();
    }

}
