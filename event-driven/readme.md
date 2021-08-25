
## X.X - Create the producer with Spring Cloud Stream

Add the following dependency to Maven `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-kafka</artifactId>
</dependency>
```
Create the event class as shown below. Generate `toString` method for logging as you wish.
```java
public class CallmeEvent {

    private Integer id;
    private String message;
    private String eventType;

    public CallmeEvent() {
    }

    public CallmeEvent(Integer id, String message, String eventType) {
        this.id = id;
        this.message = message;
        this.eventType = eventType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
```
Create the application main class containing producer (`Supplier`) bean that sends `CallmeEvent`:
```java
@SpringBootApplication
public class ProducerApp {
    
    private static int id = 0;

    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }

    @Bean
    public Supplier<CallmeEvent> eventSupplier() {
        return () -> new CallmeEvent(++id, "Hello" + id, "PING");
    }
}
```
Go to the configuration properties inside the `application.yml` file:
```yaml
spring.cloud.stream.bindings.eventSupplier-out-0.destination: test-topic
spring.kafka.bootstrap-servers: <address-of-your-kafka-cluster>
```
If you use `Streams for Apache Kafka` on `console.redhat.com` add the following configuration properties:
```yaml
spring.cloud.stream.kafka.binder:
  configuration:
    security.protocol: SASL_SSL
    sasl.mechanism: PLAIN
  jaas:
    loginModule: org.apache.kafka.common.security.plain.PlainLoginModule
    options:
      username: <your-client-id>
      password: <your-client-secret>
```
In order to obtain connection settings go to your Kafka instance and choose `View connection information` -> `Service accounts` -> `Create service account`. \
Then copy client id and client secret. You can also check an external address of your Kafka instance in the `Bootstrap server` field. \
Finally, start your application.

## X.X Consume messages using Kafkacat CLI

Export the following environment variables:
```shell
export BOOTSTRAP_SERVER=<your-bootstrap-server>
export USER=<your-client-id>
export PASSWORD=<your-client-secret>
export GROUP_ID=a
```
Run the following Kafkacat command and observe the output:
```shell
kafkacat -t test-topic -b "$BOOTSTRAP_SERVER" -X security.protocol=SASL_SSL -X sasl.mechanisms=PLAIN -X sasl.username="$USER" -X sasl.password="$PASSWORD" -C
```
Then, kill the command with `CTRL+C`.

## X.X - Create the consumer with Spring Cloud Stream

Add the following dependency to Maven `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-kafka</artifactId>
</dependency>
```
Create the event class as shown below. Generate `toString` method for logging as you wish.
```java
public class CallmeEvent {

    private Integer id;
    private String message;
    private String eventType;

    public CallmeEvent() {
    }

    public CallmeEvent(Integer id, String message, String eventType) {
        this.id = id;
        this.message = message;
        this.eventType = eventType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
```
Create the application main class containing consumer (`Consumer`) bean that receives `CallmeEvent`:
```java
@SpringBootApplication
public class ConsumerAApp {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerAApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerAApp.class, args);
    }

    @Bean
    public Consumer<CallmeEvent> eventConsumer() {
        return event -> LOG.info("Received: {}", event);
    }
}
```
Go to the configuration properties inside the `application.yml` file:
```yaml
spring.cloud.stream.bindings.eventConsumer-in-0.destination: test-topic
spring.kafka.bootstrap-servers: <address-of-your-kafka-cluster>
```
If you use `Streams for Apache Kafka` on `console.redhat.com` add the following configuration properties:
```yaml
spring.cloud.stream.kafka.binder:
  configuration:
    security.protocol: SASL_SSL
    sasl.mechanism: PLAIN
  jaas:
    loginModule: org.apache.kafka.common.security.plain.PlainLoginModule
    options:
      username: <your-client-id>
      password: <your-client-secret>
```
Finally, start your application and watch on the logs.

## X.X - Shared Kafka cluster

Change the address of Kafka inside the `application.yml` file into the shared cluster:
```yaml
spring.kafka.bootstrap-servers: <address-of-shared-kafka-cluster>
```
Modify the username and password into the shared cluster credentials:
```yaml
spring.cloud.stream.kafka.binder:
  configuration:
    security.protocol: SASL_SSL
    sasl.mechanism: PLAIN
  jaas:
    loginModule: org.apache.kafka.common.security.plain.PlainLoginModule
    options:
      username: <shared-client-id>
      password: <shared-client-secret>
```
To that operation for both `producer` and `consumer` applications. \
Change the name of the topic into the following: `<message_type>.<dataset_name>.<data_name>`. Where `message_type=user`, `dataset_name` is the same as your username at `console.redhat.com` and `data_name` is the name of your event class in lower case. \
To that operation for both `producer` and `consumer` applications. \
Restart both applications. \
You can also consider overriding default value of the property `spring.cloud.stream.kafka.binder.autoCreateTopics` to `false`.