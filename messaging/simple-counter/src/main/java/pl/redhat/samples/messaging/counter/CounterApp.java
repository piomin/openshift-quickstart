package pl.redhat.samples.messaging.counter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/counter")
public class CounterApp {

    private static int c = 0;

    public static void main(String[] args) {
        SpringApplication.run(CounterApp.class, args);
    }

    @Value("${DIVIDER:0}")
    int divider;

    @GetMapping
    public Integer count() {
        if (divider > 0)
            return c++ % divider;
        else
            return c++;
    }
}
