# Workshop: Event-driven Architecture with Kafka, Spring Cloud Stream and OpenShift
In this workshop we will develop a simple event-driven architecture using [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream) framework to show the most commonly used patterns and techniques. \
We will use Kafka deployed on AWS ([Red Hat Hybrid Cloud Console](https://cloud.redhat.com/)) and then as deployment on OpenShift (AMQ Streams). \
Before starting see the list of requirements.

# Table of Contents
1. [Prerequisites](#1-prerequisites)\
   1.1. [JDK](#11-jdk11)\
   1.2. [Maven](#12-maven-35)\
   1.3. [CLI `oc` client](#13-cli-oc-client-40)\
   1.4. [CLI `odo` client](#14-cli-odo-client-20)\
   1.5. [CLI `kafkacat`](#15-cli-kafkacat)\
   1.6. [CLI `kafka` client](#16-kafka-cli-270)\
   1.7. [IDE for Java development](#17-ide-for-java-development)\
   1.8. [Git client](#18-git-client)\
   1.9. [Account on `redhat.developers.com`](#19-account-at-httpsdevelopersredhatcom)
2. [Create producer with Spring Cloud Stream](#2-create-the-producer-with-spring-cloud-stream)
3. [Consume messages using `kafkacat` CLI](#3-consume-messages-using-kafkacat-cli)
4. [Create `consumer` with Spring Cloud Stream](#4-create-the-consumer-with-spring-cloud-stream)
5. [Shared Kafka cluster](#5-shared-kafka-cluster)
6. [Enable metrics](#6-enable-metrics)
7. [Enable partitioning and consumer groups](#7-enable-partitioning-and-consumer-groups)
8. [Kafka on OpenShift](#8-kafka-on-openshift)
9. [Implement event-driven architrecture](#9-implement-event-driven-architecture)\
   9.1. [Event gateway](#91-event-gateway)\
   9.2. [Shipment service](#92-shipment-service)\
   9.3. [Payment service](#93-payment-service)\
   9.4. [Order service](#94-order-service)\
   9.5. [SAGA pattern](#95-saga-pattern)\
   9.6. [CQRS pattern](#96-cqrs-pattern)\
   9.7. [DLQ pattern](#97-dlq-dead-letter-queue-pattern)\
   9.8. [Rollback SAGA and event routing](#98-rollback-saga-and-event-routing)
10. [Schema versioning](#10-schema-versioning)
11. [Streams and Tables](#11-streams-and-tables)

## 1. Prerequisites

### 1.1. JDK11+

```shell
$ java --version
java 16.0.2 2021-07-20
Java(TM) SE Runtime Environment (build 16.0.2+7-67)
Java HotSpot(TM) 64-Bit Server VM (build 16.0.2+7-67, mixed mode, sharing)
```

### 1.2. Maven 3.5+

```shell
$ mvn -version
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: /Users/pminkows/apache-maven-3.6.3
Java version: 16.0.2, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk-16.0.2.jdk/Contents/Home
Default locale: en_PL, platform encoding: UTF-8
```

### 1.3. CLI `oc` client 4.0+

```shell
$ oc version
Client Version: 4.6.12
```

### 1.4. CLI `odo` client 2.0+

Tool for deploying app directly from the current version of the code. If you are familiar with other tools then `odo` you may use it instead.
```shell
$ odo version  
odo v2.2.1 (17a078b67)
```
### 1.5. CLI `kafkacat`

```shell
$ kafkacat -V 
kafkacat - Apache Kafka producer and consumer tool
https://github.com/edenhill/kafkacat
Copyright (c) 2014-2019, Magnus Edenhill
Version 1.6.0 (JSON, Avro, Transactions, librdkafka 1.7.0 builtin.features=gzip,snappy,ssl,sasl,regex,lz4,sasl_gssapi,sasl_plain,sasl_scram,plugins,zstd,sasl_oauthbearer)
```

### 1.6. Kafka CLI 2.7.0+

```shell
./kafka-topics.sh --version
2.8.0 (Commit:ebb1d6e21cc92130)
```

### 1.7. IDE for Java Development
The presenter will use IntelliJ IDEA.

### 1.8. Git client

```shell
git --version
git version 2.24.3 (Apple Git-128)
```

### 1.9. Account at https://developers.redhat.com/
In order to create own instance of Apache Kafka there.

## 2. Create the producer with Spring Cloud Stream

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

## 3. Consume messages using Kafkacat CLI

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

## 4. Create the consumer with Spring Cloud Stream

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

## 5. Shared Kafka cluster

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

## 6. Enable metrics

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

## 7. Enable partitioning and consumer groups

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

## 8. Kafka on OpenShift

Set the following environment variables:
```shell
export OPSH_CLUSTER=qyt1tahi.eastus.aroapp.io
export OPSH_USER=<your-user-name>
export OPSH_PASSWORD=<your-password>
```
Login to the OpenShift cluster:
```shell
oc login -u $OPSH_USER -p $OPSH_SERVER --server=https://api.$OPSH_CLUSTER:6443
```
Create new project:
```shell
oc new-project $OPSH_USER-workshop
```
Go to the `producer` directory. Then create new `java` application with `odo`:
```shell
cd event-driven/producer
odo create java --s2i producer
```
Check Kafka bootstrap server address:
```shell
oc get svc -n kafka | grep kafka-bootstrap
```
Replace it in the `producer` `application.yml`:
```yaml
spring.kafka.bootstrap-servers=<kafka-bootstrap-service-name>.kafka:9092
```
Ensure you have the following properties commented out for now:
```properties
spring.cloud.stream.kafka.binder.configuration.security.protocol
spring.cloud.stream.kafka.binder.configuration.sasl.mechanism
spring.cloud.stream.kafka.binder.jaas.loginModule
spring.cloud.stream.kafka.binder.jaas.options.username
spring.cloud.stream.kafka.binder.jaas.options.password
```
Add the following property to the `application.yml`:
```yaml
spring.cloud.stream.kafka.binder.autoCreateTopics: false
```
Build and deploy your application on OpenShift:
```shell
odo push
```
Verify the application logs. Did the producer connect with the topic? \
Create the following YAML manifest, e.g. `topic.yaml`:
```yaml
apiVersion: kafka.strimzi.io/v1beta2
kind: KafkaTopic
metadata:
  name: <your-topic-name>
  labels:
    strimzi.io/cluster: my-cluster
  namespace: kafka
spec:
  config:
    retention.ms: 360000
    segment.bytes: 102400
  partitions: 10
  replicas: 1
```
Then apply the configuration to the `kafka` namespace:
```shell
oc apply -f topic.yaml -n kafka
```
Then verify your application logs:
```shell
oc logs -f -l app.kubernetes.io/instance=producer
```
Then create a YAML manifest, e.g. `user.yaml`:
```yaml
apiVersion: kafka.strimzi.io/v1beta2
kind: KafkaUser
metadata:
  name: <your-user-name>
  labels:
    strimzi.io/cluster: my-cluster
  namespace: kafka
spec:
  authentication:
    type: scram-sha-512
  authorization:
    acls:
      - resource:
          type: topic
          name: <your-topic-name>
          patternType: literal
        operation: Read
        host: '*'
      - resource:
          type: topic
          name: <your-topic-name>
          patternType: literal
        operation: Describe
        host: '*'
      - resource:
          type: group
          name: a
          patternType: literal
        operation: Read
        host: '*'
      - resource:
          type: topic
          name: <your-topic-name>
          patternType: literal
        operation: Write
        host: '*'
    type: simple
```
Then apply the configuration to the `kafka` namespace:
```shell
oc apply -f user.yaml -n kafka
```
Then verify your application logs:
```shell
oc logs -f -l app.kubernetes.io/instance=producer
```
Find the secret related to your user:
```shell
oc get secret <your-user-name> -n kafka -o yaml
```
Run the following command to watch for the changes in the source code:
```shell
odo watch
```
Add the following properties to the application.yml for both producer and consumer applications:
```yaml
spring.cloud.stream.kafka.binder.configuration:
  security.protocol=SASL_PLAINTEXT
  sasl.mechanism=SCRAM-SHA-512
spring.cloud.stream.kafka.binder.jaas:
  loginModule: org.apache.kafka.common.security.scram.ScramLoginModule
  options:
    username: <your-user-name>
    password: <your-user-password>
```
Go to the `consumer` directory:
```shell
cd event-driven/consumer
```
Ensure to disable dynamic port generation enabled in the previous section. Then create new `java` application with `odo`:
```shell
odo create java --s2i consumer
```
Then verify your application logs:
```shell
oc logs -f -l app.kubernetes.io/instance=consumer
```
Scale out the number of `consumer` instances. Verify the logs of all the instances. 

## 9. Implement event-driven architecture

There are several microservices sending and listening for events, and a gateway exposing REST API for an external client. \
Let's start with the implementation of an event gateway.

### 9.1. Event Gateway

Go to the event-gateway directory:
```shell
cd event-driven/event-gateway
```
Open the class `pl.redhat.samples.eventdriven.gateway.message.AbstractOrderCommand`. \
In the same package create the class `OrderCommand` as a subclass of `AbstractOrderCommand`. Add a `String` field `id`, with getters/setters.

Add the following dependencies into Maven `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-stream-kafka</artifactId>
    </dependency>
</dependencies>
```
Add a single POST endpoint into the `OrderController` class. It takes `OrderCommand` as an input. \
Use `StreamBridge` bean to send the message to the arbitrary output:
```java
@PostMapping
public Boolean orders(@RequestBody OrderCommand orderCommand) {
    orderCommand.setId(UUID.randomUUID().toString());
    return streamBridge.send("orders-out-0", orderCommand);
}
```
Configure output destination for the `orders-out-0` binding:
```yaml
spring.cloud.stream.source: orders
spring.cloud.stream.bindings.orders-out-0.destination: <your-topic-name>
```
Deploy the application on OpenShift using `odo`.

### 9.2. Shipment Service

Go to the `shipment-service` directory:
```shell
cd event-driven/shipment-service
```
Create the `OrderCommand`. You can copy it from the previous service. \
Also create `OrderEvent` with the following fields:
```java
public class OrderEvent {

    private String id;
    private String commandId;
    private String type;
    private String status;

    public OrderEvent() {
        this.id = UUID.randomUUID().toString();
    }

    public OrderEvent(String commandId, String type, String status) {
        this.id = UUID.randomUUID().toString();
        this.commandId = commandId;
        this.type = type;
        this.status = status;
    }

    // GENERATE GETTERS AND SETTERS
}
```
Go to the `pl.redhat.samples.eventdriven.shipment.service.ShipmentService`. Add the method for reserving products. It should return `OrderEvent`: 
```java
public OrderEvent reserveProducts(OrderCommand orderCommand) {
    Product product = productRepository.findById(orderCommand.getProductId()).orElseThrow();
    product.setReservedCount(product.getReservedCount() - orderCommand.getProductCount());
    productRepository.save(product);
    return new OrderEvent(orderCommand.getId(), "OK", "RESERVATION");
}
```
Then add the `Function` bean to the application main class responsible for processing orders. It listens for an input `OrderCommand` and sends `OrderEvent` as a response: 
```java
@Bean
public Function<OrderCommand, OrderEvent> orders() {
    return command -> shipmentService.reserveProducts(command);
}
```
Go to the `application.yml`. Configure a destination for the input and output.
```yaml
spring.cloud.stream.bindings.orders-in-0.destination: <your-in-topic-name>
spring.cloud.stream.bindings.orders-out-0.destination: <your-out-topic-name>
```
### 9.3. Payment Service

Go to the `payment-service` directory:
```shell
cd event-driven/payment-service
```

Copy `OrderEvent` and `OrderCommand` from the previous services. \
Go to the `pl.redhat.samples.eventdriven.payment.service.PaymentService`. Add the method for reserving balance. It should return `OrderEvent`:
```java
public OrderEvent reserveBalance(OrderCommand orderCommand) {
    Account account = accountRepository.findByCustomerId(orderCommand.getCustomerId()).stream().findFirst().orElseThrow();
    account.setReservedAmount(product.getReservedAmount() - orderCommand.getAmount());
    accountRepository.save(account);
    return new OrderEvent(orderCommand.getId(), "OK", "RESERVATION");
}
```
Then add the `Function` bean to the application main class responsible for processing orders. It listens for an input `OrderCommand` and sends `OrderEvent` as a response:
```java
@Bean
public Function<OrderCommand, OrderEvent> orders() {
    return command -> paymentService.reserveBalance(command);
}
```
Go to the `application.yml`. Configure a destination for the input and output. 

### 9.4. Order Service

Go to the `order-service` directory:
```shell
cd event-driven/order-service
```

Go to the `pl.redhat.samples.eventdriven.order.service.OrderService`. \
Add the method for storing a new `OrderCommand`:
```java
public void addOrderCommand(OrderCommand orderCommand) {
    orderCommand.setStatus("NEW");
    orderCommandRepository.save(orderCommand);
}
```
Then go to the main application class and add the following bean:
```java
@Bean
public Consumer<OrderCommand> orders() {
    return command -> orderService.addOrderCommand(command);
}
```

### 9.5. SAGA Pattern

In `order-service` add the following a new command `pl.redhat.samples.eventdriven.order.message.ConfirmCommand`:
```java
public class ConfirmCommand extends AbstractOrderCommand {

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
```
Copy that command to the `payment-service` and `shipment-service`. \
Then go to the `pl.redhat.samples.eventdriven.order.service.OrderService` and the method for updating order status or removing it from a cache. \
If order has been confirmed by the both `payment-service` and `shipment-service` it may be removed and `order-service` sends the confirmation message:
```java
public void updateOrderCommandStatus(String id) {
    OrderCommand orderCommand = orderCommandRepository.findById(id).orElseThrow();
    if (orderCommand.getStatus().equals("NEW")) {
        orderCommand.setStatus("PARTIALLY_CONFIRMED");
        orderCommandRepository.save(orderCommand);
    } else if (orderCommand.getStatus().equals("PARTIALLY_CONFIRMED")) {
        ConfirmCommand confirmCommand = new ConfirmCommand();
        confirmCommand.setOrderId(id);
        confirmCommand.setAmount(orderCommand.getAmount());
        confirmCommand.setProductCount(orderCommand.getProductCount());
        confirmCommand.setProductId(orderCommand.getProductId());
        confirmCommand.setCustomerId(orderCommand.getCustomerId());
        streamBridge.send("confirmations-out-0", confirmCommand);
        orderCommandRepository.deleteById(id);
    }
}
```
Add the following bean to the application main class:
```java
@Bean
public Consumer<OrderEvent> events() {
    return event -> orderService.updateOrderCommandStatus(event.getCommandId());
}
```
Then add the destination for binding used to send `ConfirmOrder`:
```yaml
spring.cloud.stream.bindings.confirmations-out-0.destination: <your-topic-name>
```
Also add mappping for multiple functional beans and with a source declaration the `StreamBridge` bean:
```yaml
spring.cloud.function.definition: orders;events
spring.cloud.stream.source: confirmations
```

Switch to the `shipment-service`. Add the following method to the `pl.redhat.samples.eventdriven.shipment.service.ShipmentService`:
```java
public OrderEvent confirmProducts(ConfirmCommand confirmCommand) {
    Product product = productRepository.findById(confirmCommand.getProductId()).orElseThrow();
    product.setReservedCount(product.getCurrentCount() - confirmCommand.getProductCount());
    productRepository.save(product);
    return new OrderEvent(confirmCommand.getOrderId(), "OK", "CONFIRM");
}
```
Then go to the main class and the following bean:
```java
@Bean
public Consumer<ConfirmCommand> confirmations() {
    return command -> shipmentService.confirmProducts(command);
}
```
Set the input destination for the `ConfirmCommand`:
```yaml
spring.cloud.stream.bindings.confirmations-in-0.destination: <your-topic-name>
```
There are multiple functional bean declarations in `shipment-service`. Therefore, the following configuration property is required.
```yaml
spring.cloud.function.definition: orders;confirmations
```

Then switch to the `payment-service`. Implement the method `confirmBalance` in the `pl.redhat.samples.eventdriven.payment.service.PaymentService`:
```java
public void confirmBalance(ConfirmCommand confirmCommand) {
    // TODO - implement by yourself
}
```
Add the `Consumer` bean and set the right destination in `application.yml`. Remember about multiple Spring Cloud Function beans defined. \
Then deploy all the three services `payment-service`, `shipment-service`, `order-service` on OpenShift cluster using`odo`.

Send the test order to the `event-gateway` HTTP endpoint:
```shell
curl http://<route-address>/orders -d "{\"customerId\":1,\"productId\":1,\"productCount\":3,\"amount\":1000}"
```

### 9.6. CQRS Pattern

Go to the `event-driven` directory. Create `OrderQuery` in the `pl.redhat.samples.eventdriven.gateway.message` package with the following fields:
```java
public class OrderQuery {

    private String queryId;
    private Integer customerId;
    private Date startDate;
    private Date endDate;

    // GENERATE GETTERS, SETTERS AND CONSTRUCTORS
}
```
Create or copy that class to `order-query-service` application. In `order-query-service` create a class `OrderQueryResult` with the following fields:
```java
public class OrderQueryResult {

    private String queryId;
    private List<Order> orders;

    // GENERATE GETTERS, SETTERS AND CONSTRUCTORS
}
```
Go to the application main class. \
Query service should consume both messages `OrderCommand` and `ConfirmCommand`. Here's bean declaration for receiving `OrderCommand`:
```java
@Bean
public Consumer<OrderCommand> orders() {
    return input -> orderRepository
            .save(new Order(input.getId(),
                    input.getCustomerId(),
                    input.getProductId(),
                    input.getProductCount(),
                    input.getAmount(),
                    "RESERVATION"));
}
```
Create a similar declaration for `ConfirmCommand` messages. Set another event type, e.g. `CONFIRMATION`. \
Then create receiver/sender bean for handling queries. It takes `OrderQuery` as an input, and returns `OrderQueryResult` as an output. \
Optionally: you can try to provide more advanced implementation basing also on the `OrderQuery` `startDate` and `endDate`.
```java
@Bean
public Function<OrderQuery, OrderQueryResult> queries() {
    return input -> {
        LOG.info("New Query: {}", input.getQueryId());
        List<Order> orders = orderRepository.findByCustomerId(input.getCustomerId());
        return new OrderQueryResult(input.getQueryId(), orders);
    };
}
```
Then switch to the `application.yml` file. There are several functional beans defined, so we need to provide additional configuration containing the names of those beans. For me it the following configuration:
```yaml
spring.cloud.function.definition: orders;confirmations;queries
```
Now, we need to configure destinations for all the binding. Let's start with consumers. \
Because we use in-memory database for storing events, each time the application is starting we copy the events topics:
```yaml
spring.cloud.stream.bindings.orders-in-0.destination: user.<your_namespace>.ordercommand
spring.cloud.stream.kafka.bindings.orders-in-0.consumer.startOffset: earliest
spring.cloud.stream.kafka.bindings.orders-in-0.consumer.resetOffsets: true

spring.cloud.stream.bindings.confirmations-in-0.destination: user.<your_namespace>.confirmcommand
spring.cloud.stream.kafka.bindings.confirmations-in-0.consumer.startOffset: earliest
spring.cloud.stream.kafka.bindings.confirmations-in-0.consumer.resetOffsets: true
```
Finally, we should set destination for queries:
```yaml
spring.cloud.stream.bindings.queries-in-0.destination: user.<your_namespace>.orderquery
spring.cloud.stream.bindings.queries-out-0.destination: user.<your_namespace>.orderqueryresult
```
Create and push the application to the OpenShift cluster:
```shell
odo create java --s2i order-query-service 
odo push
```

Switch to the `event-gateway` application. \
Copy `OrderQueryResult` created in `order-query-service`. Alternatively, add the appropriate constructor. \
Create `pl.redhat.samples.eventdriven.gateway.message.Order` class with the following fields:
```java
public class Order {

    private Integer id;
    private String messageId;
    private Integer customerId;
    private Integer productId;
    private int productCount;
    private int amount;
    private String type;
    private LocalDateTime creationDate;

    // GENERATE GETTERS, SETTERS AND CONSTRUCTORS
}
```
Run `odo` command for inner development loop:
```shell
odo watch
```

In the `OrderController` class add REST endpoint responsible for sending queries to the Kafka topic:
```java
@GetMapping("/customer/{customerId}")
public String query(@PathVariable Integer customerId) {
    String uuid = UUID.randomUUID().toString();
    streamBridge.send("queries-out-0", new OrderQuery(uuid, customerId));
    return uuid;
}
```
Switch to the application main class. Add a bean responsible for handling incoming query results. Also create a `Map` bean for storing responses temporary:
```java
@SpringBootApplication
public class EventGatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(EventGatewayApp.class, args);
    }

    @Bean
    public Consumer<OrderQueryResult> queries() {
        return input -> results().put(input.getQueryId(), input.getOrders());
    }

    @Bean
    Map<String, List<Order>> results() {
        return new HashMap();
    }
}
```
Then open the `application.yml` file. Add the following two properties to set binding destinations:
```yaml
spring.cloud.stream.bindings.queries-out-0.destination: <output-query-topic-name>
spring.cloud.stream.bindings.queries-in-0.destination: <input-result-topic-name>
```
Optionally, we may print producer log messages:
```yaml
logging.level.org.springframework.cloud.stream: DEBUG
```
Save the changes. \
Query for orders sent by the customer with `id=1`:
```shell
curl http://<route-address>/orders/customer/1
```
See the logs printed by the `order-query-service` application. \
You should see exception that `java.time.LocalDateTime` is not supported. First verify how many log lines starting with `New Query:` you see. Why? \
You can customize this behaviour, and increase the number of retries to e.g. `5`:
```yaml
spring.cloud.stream.bindings.queries-in-0.consumer.maxAttempts: 5
```
Query for orders once again. How many retries do you see now? \
Fix error. You need to add the following dependency:
```xml
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```
Then go to the `Order` class and add the following annotation for `LocalDateTime` field:
```java
@JsonDeserialize(using = LocalDateTimeDeserializer.class)
@JsonSerialize(using = LocalDateTimeSerializer.class)
```
Provide the same change the `Order` class implementation in the `event-gateway. \
Query for orders sent by the customer with `id=1`. You should receive response with `uuid`:
```shell
curl http://<route-address>/orders/customer/1
85b8792d-93f7-40db-8da2-b367d0891671
```
Now, obtain the result using a dedicated endpoint and uuid received in the previous response:
```shell
curl http://<route-address>/orders/results/{queryId}
```

### 9.7. DLQ (Dead Letter Queue) Pattern

Go to the `shipment-service`. Add the following Exception class `pl.redhat.samples.eventdriven.shipment.service.NotEnoughProductsException`:
```java
public class NotEnoughProductsException extends RuntimeException {

    public NotEnoughProductsException() {
        super("Not enough products");
    }
}
```
Change the implementation of the `reserveProducts` method in `ShipmentService` `@Service` and throw `Exception` if there is not enough products.
```java
public OrderEvent reserveProducts(OrderCommand orderCommand) {
    Product product = productRepository.findById(orderCommand.getProductId()).orElseThrow();
    product.setReservedCount(product.getReservedCount() - orderCommand.getProductCount());
    if (product.getReservedCount() > 0)
        throw new NotEnoughProductsException();
    productRepository.save(product);
    return new OrderEvent(orderCommand.getId(), "OK", "RESERVATION");
}
```
Enable DLQ for the consumer and override the name of DLQ topic:
```yaml
spring.cloud.stream.bindings.orders-in-0.consumer.enableDlq: true
spring.cloud.stream.bindings.orders-in-0.consumer.dlqName: user.<your-namespace>.shipment.ordercommand.dlq
```

Create the wrapper `pl.redhat.samples.eventdriven.shipment.message.OrderCommandDelayed` for delaying `OrderCommand` received from DLQ topic:
```java
public class OrderCommandDelayed implements Delayed {

    private OrderCommand orderCommand;
    private long startTime;

    public OrderCommandDelayed(OrderCommand orderCommand, long delayInMilliseconds) {
        this.orderCommand = orderCommand;
        this.startTime = System.currentTimeMillis() + delayInMilliseconds;
    }

    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(@NotNull Delayed o) {
        return (int) (this.startTime - ((OrderCommandDelayed) o).startTime);
    }

    public OrderCommand getOrderCommand() {
        return orderCommand;
    }
}
```
Then declare Java `DelayQueue` `@Bean` for handling delayed events:
```java
@Bean
public DelayQueue<OrderCommandDelayed> delayQueue() {
    return new DelayQueue<OrderCommandDelayed>();
}
```
After that, declare `Consumer` `@Bean` for consuming events from DLQ topic and pushing them into the `DelayQueue`:
```java
@Bean
public Consumer<OrderCommand> dlqs() {
    return input -> delayQueue().offer(new OrderCommandDelayed(input, 60000));
}
```
Add a required configuration for bindings in `application.yml`. \
Then create a special @Service for handling delayed commands. It is based on Spring `@Scheduled`:
```java
@Service
public class DlqService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentServiceApp.class);

    private DelayQueue<OrderCommandDelayed> delayQueue;
    private StreamBridge streamBridge;
    private ShipmentService shipmentService;

    public DlqService(DelayQueue<OrderCommandDelayed> delayQueue, StreamBridge streamBridge, ShipmentService shipmentService) {
        this.delayQueue = delayQueue;
        this.streamBridge = streamBridge;
        this.shipmentService = shipmentService;
    }

    @Scheduled(fixedDelay = 1000)
    public void schedule() {
        try {
            OrderCommandDelayed delayed = delayQueue.take();
            OrderEvent event = null;
            try {
                shipmentService.reserveProducts(delayed.getOrderCommand());
                event = new OrderEvent(delayed.getOrderCommand().getId(), "RESERVATION", "OK");
            } catch (NotEnoughProductsException e) {
                LOG.error("Not enough products: id={}", delayed.getOrderCommand().getProductId());
                event = new OrderEvent(delayed.getOrderCommand().getId(), "RESERVATION", "FAILED");
            }
            streamBridge.send("orders-out-0", event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
Annotate the main class with `@EnableScheduling`. If you didn't run `odo watch`, deploy the latest version of application with the following `odo` command:
```shell
odo push
```

### 9.8. Rollback (SAGA) and event routing

Switch to the `payment-service`. \
Implement negative scenario in `pl.redhat.samples.eventdriven.payment.service.PaymentService`. Just send back `OrderEvent{status=FAILED}` if there is no sufficient funds on the account. \
Modify `OrderEvent` to add a field, e.g. `source` that indicates a producer of the event. Modify it in all the applications. 

Switch to the `order-service`. Add a new command `RollbackCommand`:
```java
public class RollbackCommand extends AbstractOrderCommand {

    private String orderId;
    private String source;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
```
Go to the `pl.redhat.samples.eventdriven.order.service.OrderService`. Add the method for handling rollback and fix `TODO`:
```java
public void rollbackOrder(String id) {
    OrderCommand orderCommand = orderCommandRepository.findById(id).orElseThrow();
    RollbackCommand rollbackCommand = new RollbackCommand();
    rollbackCommand.setOrderId(id);
    rollbackCommand.setAmount(orderCommand.getAmount());
    rollbackCommand.setProductCount(orderCommand.getProductCount());
    rollbackCommand.setProductId(orderCommand.getProductId());
    rollbackCommand.setCustomerId(orderCommand.getCustomerId());
    rollbackCommand.setSource(""); // TODO - change the method signature to obtain a source name
    streamBridge.send("confirmations-out-0", rollbackCommand);
    orderCommandRepository.deleteById(id);
}
```
Add new Consumer with the following name and fix `TODO`:
```java
@Bean
public Consumer<OrderEvent> failedEvents() {
    // TODO - add implementation
}
```
Go to the `application.yml`. How will change the current bindings configuration?:
```yaml
spring.cloud.function.definition: orders;events
spring.cloud.stream.source: confirmations
spring.cloud.stream.bindings.orders-in-0.destination: user.pminkows.ordercommand
spring.cloud.stream.bindings.events-in-0.destination: user.pminkows.orderevent
spring.cloud.stream.bindings.confirmations-out-0.destination: user.pminkows.confirmcommand
```
We need to add function router:
```yaml
spring.cloud.stream.bindings.functionRouter-in-0.destination=user.pminkows.orderevent
spring.cloud.stream.bindings.functionRouter-in-0.group=b
spring.cloud.stream.bindings.functionRouter-in-0.consumer.partitioned=true
spring.cloud.stream.function.routing.enabled=true
spring.cloud.function.routing-expression=(payload.status=='FAILED') ? 'failedEvents':'events'
```

Copy `RollbackCommand` class to the `shipment-service` and `payment-service`. Then do the necessary changes to handle rollback there.

## 10. Schema versioning

List the services in the kafka namespace:
```shell
oc get svc -n kafka | grep apicurio
```
List the routes in the kafka namespace:
```shell
oc get route -n kafka | grep apicurio
```
Visit the schema registry web UI using its route. 

Switch to the `producer` application. \
In the `src/main/resources` directory create folder `avro`. Then, create a file `callmeevent.avro` with the following content:
```json
{
  "type":"record",
  "name":"CallmeEvent",
  "namespace":"pl.redhat.samples.eventdriven.producer.message.avro",
  "fields": [
    {
      "name":"id",
      "type":"int"
    },{
      "name":"message",
      "type":"string"
    },{
      "name":"eventType",
      "type": "string"
    }
  ]
}
```
Switch to the Maven `pom.xml`. Add the following plugin into the plugins list:
```xml
<plugin>
  <groupId>org.apache.avro</groupId>
  <artifactId>avro-maven-plugin</artifactId>
  <version>1.10.2</version>
  <executions>
    <execution>
      <phase>generate-sources</phase>
      <goals>
        <goal>schema</goal>
      </goals>
      <configuration>
        <sourceDirectory>${project.basedir}/src/main/resources/avro/</sourceDirectory>
        <outputDirectory>${project.basedir}/target/generated-sources/avro/</outputDirectory>
      </configuration>
    </execution>
  </executions>
</plugin>
```
To simplify working with the generated code you may also add the following plugin:
```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>build-helper-maven-plugin</artifactId>
  <version>3.2.0</version>
  <executions>
    <execution>
      <phase>generate-sources</phase>
      <goals>
        <goal>add-source</goal>
      </goals>
      <configuration>
        <sources>
          <source>${project.build.directory}/generated-sources/avro</source>
        </sources>
      </configuration>
    </execution>
  </executions>
</plugin>
```
Then build your Maven project:
```shell
mvn clean package
```

Replace `CallmeEvent` in the producer Supplier with the class generated by `avro-maven-plugin`. \
Change the `contentType` of message serialization for the producer binding:
```yaml
spring.cloud.stream.bindings.eventSupplier-out-0.contentType=application/*+avro
```
Add schema registry client for Apicurio:
```xml
<dependency>
    <groupId>com.github.piomin</groupId>
    <artifactId>spring-cloud-schema-registry-client-apicurio</artifactId>
    <version>1.0</version>
</dependency>
```
Override a default address of schema registry into `Service` displayed before:
```yaml
spring.cloud.schemaRegistryClient.endpoint: http://<apicurio-svc-name>.kafka:8080/
```
Before you run the application change its name in `application.yml` into the following pattern:
```yaml
spring.application.name: producer-<your-namespace-name>
```
Create the following bean in your application main class:
```java
@Bean
public SchemaRegistryClient schemaRegistryClient(){
  return new ApicurioSchemaRegistryClient();
}
```
Annotate the main class with `@EnableSchemaRegistryClient`.

Do all these steps also for the `consumer` application. \
Deploy the latest version of these into OpenShift using the `odo push` command. \
Visit Apicurio Schema UI once again. Find your schema. Verify application logs:
Then verify your application logs:
```shell
oc logs -f -l app.kubernetes.io/instance=producer
```
Then display `consumer` logs:
```shell
oc logs -f -l app.kubernetes.io/instance=consumer
```

Now let's provide some non-compliant changes in the `callmeevent.avro` schema, e.g. change the name of the field from `message` into `content`:
```json
{
  "type":"record",
  "name":"CallmeEvent",
  "namespace":"pl.redhat.samples.eventdriven.producer.message.avro",
  "fields": [
    {
      "name":"id",
      "type":"int"
    },{
      "name":"content",
      "type":"string"
    },{
      "name":"eventType",
      "type": "string"
    }
  ]
}
```
Rebuild your `producer` application:
```shell
mvn clean package
```
Deploy the latest version of these into OpenShift using the `odo push` command. \
Then verify your application logs once again:
```shell
oc logs -f -l app.kubernetes.io/instance=producer
```
Then display `consumer` logs:
```shell
oc logs -f -l app.kubernetes.io/instance=consumer
```
Do some compliant changes like adding a new field into `callmeevent.avro` without removing anything. Replay the procedure as before. \
Then go to the Apicurio Schema UI once again. Find your schema. Go to the details. See a new version.

## 11. Streams and Tables

Go to the `streams` directory:
```shell
cd event-driven/streams
```
There are two applications: `producer-streams` and `consumer-streams`. First go to the `producer-streams` module. \
Add a single dependency to Spring Cloud Stream Kafka binder:
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-stream-kafka</artifactId>
    </dependency>
</dependencies>
```
The message objects are available inside `pl.redhat.samples.eventdriven.streams.producer.message` package. These are `Order` and `Customer`. \
Go the application main class. Add a sample list of objects to send into the target topic, e.g.:
```java
LinkedList<Order> orders = new LinkedList<>(List.of(
        new Order(1, "NEW", 3),
        new Order(2, "NEW", 1),
        new Order(3, "NEW", 2),
        new Order(1, "PROCESSING", 3),
        new Order(2, "FINISHED", 1)
));
```
Then add the `Supplier` bean for sending orders. Use `MessageBuilder` and add Kafka key to the message using the header `kafka_messageKey`:
```java
@Bean
public Supplier<Message<Order>> orderSupplier() {
    return () -> orders.peek() != null ? MessageBuilder
            .withPayload(orders.peek())
            .setHeader(KafkaHeaders.MESSAGE_KEY, orders.poll().getId())
            .build() : null;
}
```
Go to the `application.yml` file and add the following properties:
```yaml
spring.cloud.stream.bindings.orderSupplier-out-0.destination: <your-topic-name>
spring.cloud.stream.bindings.orderSupplier-out-0.producer.partitionKeyExpression: headers['kafka_messageKey']
spring.cloud.stream.bindings.orderSupplier-out-0.producer.partitionCount: 5
spring.cloud.stream.kafka.bindings.orderSupplier-out-0.producer.configuration.key.serializer: org.apache.kafka.common.serialization.IntegerSerializer
```
Also replace Kafka connection settings with your data:
```yaml
spring.kafka.bootstrap-servers: <your-broker-url>
spring.cloud.stream.kafka.binder.configuration.security.protocol: SASL_SSL
spring.cloud.stream.kafka.binder.configuration.sasl.mechanism: PLAIN
spring.cloud.stream.kafka.binder.jaas.loginModule: org.apache.kafka.common.security.plain.PlainLoginModule
spring.cloud.stream.kafka.binder.jaas.options.username: <your-client-id>
spring.cloud.stream.kafka.binder.jaas.options.password: <your-client-secret>
```
You can also add property for detailed logging:
```yaml
logging.level.org.springframework.cloud.stream: DEBUG
```

Switch to the `consumer-streams` module. Add dependency to the Spring Cloud Stream Kafka Streams binder:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-stream-binder-kafka-streams</artifactId>
</dependency>
```
Message classes are available inside the `pl.redhat.samples.eventdriven.domain` package. \
Go to the application main class. Add the following `Consumer` bean to the application:
```java
@Bean
public Consumer<KStream<Integer, Order>> eventStream() {
    return input -> input.foreach((key, value) -> LOG.info("Stream: key={}, val={}", key, value));
}
```
In application.yml file add the properties for mapping destinations to the bindings:
```yaml
spring.cloud.stream.bindings.eventStream-in-0.destination: <your-topic-name>
spring.cloud.stream.bindings.eventStream-in-0.consumer.partitioned: true
spring.cloud.stream.bindings.eventStream-in-0.group: streams-02
spring.cloud.stream.kafka.streams.binder.functions.eventStream.applicationId: streams-02
```
The same as before replace Kafka connection settings with your data. It is a little different then before:
```yaml
spring.kafka.bootstrap-servers: <your-broker-url>
spring.cloud.stream.kafka.streams.binder.configuration.security.protocol: SASL_SSL
spring.cloud.stream.kafka.streams.binder.configuration.sasl.mechanism: PLAIN
spring.cloud.stream.kafka.streams.binder.configuration.sasl.jaas.config: org.apache.kafka.common.security.plain.PlainLoginModule required username="<your-client-id>" password="<your-client-secret>";
```

After that run `consumer-streams` application, and then `producer-streams`. Observe logs on the consumer side. \
We can implement some operations on an input stream, like e.g. count events grouped by the message key. Replace a previous implementation with the following:
```java
@Bean
public Consumer<KStream<Integer, Order>> eventStream() {
    return input -> input.groupByKey()
            .windowedBy(TimeWindows.of(Duration.ofMillis(10000)))
            .count()
            .toStream()
            .foreach((key, value) -> LOG.info("Stream: key={}, val={}", key, value));
}
```
Then, restart both consumer and producer applications.

Add the `KTable` consumer exactly on the same stream as before. Do not remove `KStream` consumer:
```java
@Bean
public Consumer<KTable<Integer, Order>> eventConsumer() {
    return input -> input.toStream().foreach((key, value) -> LOG.info("Table: key={}, val={}", key, value));
}
```
Add the following properties into `application.yml`:
```yaml
spring.cloud.stream.bindings.eventConsumer-in-0.destination: <your-topic-name>
spring.cloud.stream.bindings.eventConsumer-in-0.consumer.partitioned: true
spring.cloud.stream.bindings.eventConsumer-in-0.group: streams-01
spring.cloud.stream.kafka.streams.binder.functions.eventConsumer.applicationId: streams-01
spring.cloud.stream.kafka.streams.bindings.eventConsumer-in-0.consumer.materializedAs: orders-view
```
Input binding will be materialized as the KV `orders-view`. To fetch data from it, first add the following dependency in Maven `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
In your @RestController you would have to inject `InteractiveQueryService` to query data stored in materialized view `orders-view`:
```java
@RestController
@RequestMapping("/orders")
public class OrderController {

    private InteractiveQueryService queryService;

    public OrderController(InteractiveQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        ReadOnlyKeyValueStore<Integer, Order> keyValueStore =
                queryService.getQueryableStore("orders-view", QueryableStoreTypes.keyValueStore());
        KeyValueIterator<Integer, Order> it = keyValueStore.all();
        while (it.hasNext()) {
            KeyValue<Integer, Order> kv = it.next();
            orders.add(kv.value);
        }
        return orders;
    }
}
```
Then, restart `consumer-streams` application and call endpoint:
```shell
curl http://localhost:8080/orders
```

Back to `consumer-streams` main class. Add the following bean with two input `KTable` and a single output: 
```java
@Bean
public BiFunction<KTable<Integer, Order>, KTable<Integer, Customer>, KTable<Integer, CustomerOrder>> process() {
    return (tableOrders, tableCustomers) -> tableOrders
            .leftJoin(tableCustomers, Order::getCustomerId,
                    (order, customer) -> new CustomerOrder(order.getId(), customer.getId(), customer.getName(), order.getStatus()));
}
```
Then add a configuration in `application.yml`:
```yaml
spring.cloud.stream.bindings.process-in-0.destination: <your-first-topic-name>
spring.cloud.stream.bindings.process-in-0.consumer.partitioned: true
spring.cloud.stream.bindings.process-in-0.group: streams-03
spring.cloud.stream.bindings.process-in-1.destination: <your-second-topic-name>
spring.cloud.stream.bindings.process-in-1.consumer.partitioned: true
spring.cloud.stream.bindings.process-in-1.group: streams-03
spring.cloud.stream.bindings.process-out-0.destination: <your-output-topic-name>
spring.cloud.stream.kafka.streams.binder.functions.process.applicationId: streams-03
```
After that, add and configure a `Consumer` of the output topic. Materialize this topic with unique name, and provide endpoint for that.