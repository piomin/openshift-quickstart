package pl.redhat.samples.messaging.simple.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
public class AmqpConfig {

//    @PostConstruct
//    public void init() {
//        RestTemplate t = new RestTemplateBuilder().build();
//        int x = t.getForObject("http://simple-counter:8080/counter", Integer.class);
//        System.setProperty("amqphub.amqp10jms.remoteUrl",
//                "amqp://ex-aao-my-acceptor-" + x + "-svc:5672");
//    }

}
