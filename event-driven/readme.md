
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
You can also consider overriding default value of the property `spring.cloud.stream.kafka.binder.autoCreateTopics` to `false`. \
To force naming conventions you can configure the property `auto.create.topics.enable` on the broker.

## X.X - Enable metrics

Change the parameters related with frequency and number of messages sent to the broker: 
```yaml
spring.cloud.stream.poller:
  maxMessagesPerPoll: 20
  fixedDelay: 100
```
Add the following dependencies to the Maven `pom.xml`. Our goal is to enable metrics generation and exposure for the Kafka producer in Prometheus format.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```
The metrics are available for the application under endpoint `GET http://localhost:8080/actuator/prometheus`. \
Verify the values for the following metrics:
`kafka_producer_request_total` \
`kafka_producer_topic_record_send_rate` \
`kafka_producer_node_request_size_avg` \
`kafka_producer_node_outgoing_byte_total` \
`kafka_producer_node_incoming_byte_total` \
`kafka_producer_network_io_rate` \
`kafka_producer_batch_size_avg` \
`kafka_producer_batch_size_max`.

Then, let's create a class for a large event:
```java
public class LargeEvent {
    private Integer id;
    private String message;
    private String eventType;
    private int size;

    public LargeEvent() {
        byte[] array = new byte[2000];
        new Random().nextBytes(array);
        message = new String(array, StandardCharsets.UTF_8);
    }

    public LargeEvent(Integer id, String eventType, int size) {
        this.id = id;
        this.eventType = eventType;
        this.size = size;
        byte[] array = new byte[size];
        new Random().nextBytes(array);
        message = new String(array, StandardCharsets.UTF_8);
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
}
```
Then change the implementation of a `Supplier` bean to the following (message size [2000, 4000], but you can add your own values) and restart the application:
```java
@SpringBootApplication
public class ProducerApp {

    private static int id = 0;
    private static final Random RAND = new Random();

    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }

    @Bean
    public Supplier<LargeEvent> eventSupplier() {
        return () -> {
            int size = RAND.nextInt(2000);
            return new LargeEvent(++id, "PING", 2000 + size);
        };
    }

}
```
Change the following properties in `application.yml` and restart your application:
```yaml
spring.cloud.stream.kafka.bindings.eventSupplier-out-0.producer.bufferSize: 32768
spring.cloud.stream.kafka.binder.requiredAcks: 0
```
Then verify the values for the following metrics once again:
`kafka_producer_request_total` \
`kafka_producer_topic_record_send_rate` \
`kafka_producer_node_request_size_avg` \
`kafka_producer_node_outgoing_byte_total` \
`kafka_producer_node_incoming_byte_total` \
`kafka_producer_network_io_rate` \
`kafka_producer_batch_size_avg` \
`kafka_producer_batch_size_max`.

After the test back to the producer implementation with `CallmeEvent` and comment out all the properties except `spring.cloud.stream.poller.maxMessagesPerPoll`:
```java
@Bean
public Supplier<CallmeEvent> eventSupplier() {
    return () -> new CallmeEvent(++id, "Hello" + id, "PING");
}
```
Delete the topic (auto-create option is enabled) or switch to your instance of Kafka. \
Then switch to the `consumer` application. Add the following property in your `application.yml`:
```yaml
spring.cloud.stream.bindings.eventConsumer-in-0.consumer.batch-mode: true
```
The consumer should receive a `List` of records instead of a single record. Replace the existing implementation of a consumer with the following:
```java
@Bean
public Consumer<List<CallmeEvent>> eventConsumer() {
    return event -> {
        LOG.info("Received batches: {}", event.size());
        event.forEach(ev -> LOG.info("Event: {}", ev));
    };
}
```
Now kill the consumer. Run `kafkacat` in the consumer mode:
```shell
kafkacat -t <your-topic-name> -b "$BOOTSTRAP_SERVER" -X security.protocol=SASL_SSL -X sasl.mechanisms=PLAIN -X sasl.username="$USER" -X sasl.password="$PASSWORD" -C
```
Change the implementation of producer into a batch mode and run the application:
```java
@Bean
public Supplier<List<CallmeEvent>> eventSupplier() {
    return () -> List.of(
            new CallmeEvent(++id, "T" + id, "PING"),
            new CallmeEvent(++id, "T" + id, "PING"),
            new CallmeEvent(++id, "T" + id, "PING"),
            new CallmeEvent(++id, "T" + id, "PING"),
            new CallmeEvent(++id, "T" + id, "PING"));
}
```
Finally, you can enable metrics for the consumer application. When running locally change the port number with the `server.port` property. \
Add the following dependencies to the Maven `pom.xml`. Our goal is to enable metrics generation and exposure for the Kafka consumer in Prometheus format:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```
Verify the results: `http://localhost:<server.port>/actuator/prometheus`.

## X.X - Enable partitioning and consumer groups

We are going to run several instances of the `consumer` application. First, change the port number to the dynamically generated or disable a web support for the app:
```yaml
server.port: 0
```
Create a topic with 5 partitions. Then change the configuration inside the `application.yml` file:
```yaml
spring.cloud.stream.bindings.eventConsumer-in-0.group=a
spring.cloud.stream.bindings.eventConsumer-in-0.consumer.partitioned=true
```
Run three instances of your application sequentially and observe the logs. Try to find the log starting with `a: partitions assigned: [` for the first instance. \
Run the second instance of your application. Try to find the log starting with `a: partitions assigned: [` for the second instance. \
Then back to the logs of the first instance. Find the log starting with `a: partitions revoked: [` and then `a: partitions assigned: [`. \
Run the third instance of your application. Do the same thing as before.

Switch to the `producer` application. Add the following two lines in the `application.yml` file:
```yaml
spring.cloud.stream.bindings.eventSupplier-out-0.producer.partitionKeyExpression: payload.id
spring.cloud.stream.bindings.eventSupplier-out-0.producer.partitionCount: 5
```
Then run a single instance of the `producer` application. Observer the logs of your three instances of the `consumer` application. \
Then go to `cloud.redhat.com`. Choose your instance of Kafka. Switch to the `Consumer groups` tab. Click your group - in our example the name group is `a`. \
Then verify current offset on partitions and consumer lag on each partition. \
Now we will do the same thing using Kafka CLI. Go to your Kafka installation directory, then got to the `config` directory. \
Create the file `app-service.properties` with the following content:
```properties
sasl.mechanism=PLAIN
security.protocol=SASL_SSL

sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required \
  username="<your-client-id>" \
  password="<your-client-secret>" \
  group.id="a";
```
After creating a file switch to the `bin` directory. Run the following command for your Kafka cluster to describe your topic:
```shell
./kafka-topics.sh --describe --topic <your-topic-name> --bootstrap-server $BOOTSTRAP_SERVER --command-config ../config/app-services.properties
```
Then run the following command to describe your current group, offset and consumer lag:
```shell
./kafka-consumer-groups.sh --describe --group a --bootstrap-server $BOOTSTRAP_SERVER --command-config ../config/app-services.properties
```
Stop all the applications.

Go to the `cloud.redhat.com`. Choose your instance of Kafka, then edit your topic. Increase number of partitions from 5 to 9 (just to test). Accept change - you will see warning. \ 
Go to the `consumer` directory. Add the following property in the `application.yml` file:
```yaml
spring.cloud.stream.bindings.eventConsumer-in-0.consumer.concurrency: 3
```
Re-run your instances of the `consumer` application. Run the following command for your Kafka cluster to describe your topic and compare it to the previous result:
```shell
./kafka-topics.sh --describe --topic <your-topic-name> --bootstrap-server $BOOTSTRAP_SERVER --command-config ../config/app-services.properties
```
Just to test - you increase a value of property `spring.cloud.stream.bindings.eventConsumer-in-0.consumer.concurrency` to e.g. `6` or run another 4th instance of the `consumer` and compare result of `kafka-consumer-groups.sh`.

Go to the `producer` directory. Change the value of the following property:
```yaml
spring.cloud.stream.bindings.eventSupplier-out-0.producer.partitionCount: 9
```
Finally, run the instance of the `producer` application and observe logs of the `consumer` applications. \
Then run the following command to describe your current group, offset and consumer lag:
```shell
./kafka-consumer-groups.sh --describe --group a --bootstrap-server $BOOTSTRAP_SERVER --command-config ../config/app-services.properties
```

Now, let's change the implementation of a Supplier bean for the `producer` application into e.g. the following:
```java
@SpringBootApplication
public class ProducerApp {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerApp.class);
    private static int id = 0;
    private static final Random RAND = new Random();

    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }

    @Bean
    public Supplier<CallmeEvent> eventSupplier() {
        return () -> {
            int i = RAND.nextInt(8);
            return new CallmeEvent(++id, "Hello" + id, i == 4 ? "COMMIT" : "PING");
        };
    }
}
```
Go to the `consumer` directory. Add the following property to the `application.yml` file:
```yaml
spring.cloud.stream.kafka.default.consumer.autoCommitOffset: false
```
Then change the implementation of the `Consumer` bean:
```java
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
            Acknowledgment acknowledgment = event.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
            if (acknowledgment != null) {
                LOG.info("Manual Ack");
                if (event.getPayload().getEventType().equals("COMMIT")) {
                    acknowledgment.acknowledge();
                    LOG.info("Committed");
                }
            }
        };
    }
}
```

Then run all three instances of the `consumer` application, and a single instance of the `producer` application. \
Observe the logs from the `consumer` application. \
Stop all the applications. \
Then run the following command to describe your current group, offset and consumer lag:
```shell
./kafka-consumer-groups.sh --describe --group a --bootstrap-server $BOOTSTRAP_SERVER --command-config ../config/app-services.properties
```
Run a single instance of the `consumer` application. Observe the logs. How many events did it receive? \
Then run the following command to describe your current group, offset and consumer lag once again:
```shell
./kafka-consumer-groups.sh --describe --group a --bootstrap-server $BOOTSTRAP_SERVER --command-config ../config/app-services.properties
```
Clear the logs of the first instance. \
Run another instance of the `consumer` application. Observe the logs. How many events did it receive? \
Now, switch back to the logs of the first instance. Did it receive any events once again?