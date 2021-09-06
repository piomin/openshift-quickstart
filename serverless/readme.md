# Workshop: Knative Eventing on OpenShift with Quarkus 

## 1. Prerequisites

## 2. Before start

Clone the following repository from GitHub:
```shell
git clone https://github.com/piomin/sample-quarkus-serverless-kafka.git
```
Go to the `serverless` directory. There three applications there: `order-saga`, `payment-saga`, and `shipment-saga`:
```shell
cd serverless
```

## 3. Kafka support

First, go to the `order-saga` directory. \ 
Add the following dependency into Maven `pom.xml`:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-reactive-messaging-kafka</artifactId>
</dependency>
```
Then, open the `pl.redhat.samples.serverless.order.service.OrderPublisher` class. Add the following implementation for sending messages to the Kafka topic:
```java
@ApplicationScoped
public class OrderPublisher {

    private final Random random = new Random();

    @Outgoing("order-events")
    public Multi<Order> publishOrder() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .map(tick -> {
                    int r = random.nextInt(1000);
                    return new Order(r, r%10+1, r%10+1, 5, 100, "NEW");
                });
    }
}
```
Go the `application.properties` file, and the address of your Kafka cluster:
```properties
kafka.bootstrap.servers = <your-kafka-cluster>
```
Then add the SASL credentials:
```properties
mp.messaging.connector.smallrye-kafka.security.protocol = SASL_SSL
mp.messaging.connector.smallrye-kafka.sasl.mechanism = PLAIN
mp.messaging.connector.smallrye-kafka.sasl.jaas.config = org.apache.kafka.common.security.plain.PlainLoginModule required username="<your-kafka-username>" password="<your-kafka-password>";
```
Add the mapping for Kafka topic name:
```properties
mp.messaging.outgoing.order-events.connector = smallrye-kafka
mp.messaging.outgoing.order-events.topic = <your-topic-name>
mp.messaging.outgoing.order-events.value.serializer = io.quarkus.kafka.client.serialization.ObjectMapperSerializer
```
Run the `order-saga` locally using the following Maven command:
```shell
mvn quarkus:dev
```

Go to the `payment-saga` directory. Add the same dependency `quarkus-smallrye-reactive-messaging-kafka` to the Maven `pom.xml`.
Create `OrderDeserializer` class in the `pl.redhat.samples.serverless.payment.domain.deserialize` package:
```java
public class OrderDeserializer extends ObjectMapperDeserializer<Order> {

    public OrderDeserializer() {
        super(Order.class);
    }

}
```
Go to the `pl.redhat.samples.serverless.payment.service.OderConsumer` and add the following implementation:
```java
@ApplicationScoped
public class OrderConsumer {

    @Inject
    Logger log;

    @Incoming("order-events")
    public void consumeOrder(Order order) {
        log.infof("Received: %s", order);
    }
}
```
Add the same address of the Kafka broker and credentials as before. Then add mapping for the Kafka topic:
```properties
mp.messaging.incoming.order-events.connector = smallrye-kafka
mp.messaging.incoming.order-events.topic = <your-topic-name>
mp.messaging.incoming.order-events.value.deserializer = pl.redhat.samples.serverless.payment.domain.deserialize.OrderDeserializer
```
Run the `payment-saga` application using the `mvn quarkus:dev` command.

## 4. Quarkus Functions and HTTP

Add the following dependency into Maven `pom.xml`:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-funqy-http</artifactId>
</dependency>
```
Go to the `pl.redhat.samples.serverless.payment.function.OrderReserveFunction`, and add the implementation:
```java
public class OrderReserveFunction {

    @Inject
    Logger log;

    @Funq
    public void reserve(Order order) {
        log.infof("Received order: %s", order);
    }
}    
```
Send the HTTP request to the Funqy endpoint:
```shell
curl http://localhost:8080/reserve -d "{\"customerId\":1,\"productId\":1,\"productCount\":3,\"amount\":1000}" -H "application/json"
```

## 5. Deploy Applications on OpenShift Serverless (Knative)

These actions need to be performed for our both applications. \
Add the following dependency into Maven `pom.xml`:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-openshift</artifactId>
</dependency>
```
Add the following properties into the `application.properties` file:
```properties
quarkus.kubernetes.deploy = true
quarkus.kubernetes.deployment-target = knative
quarkus.container-image.group = <your-openshift-namespace>
quarkus.container-image.registry = image-registry.openshift-image-registry.svc:5000
```
Before running the build command ensure you have deleted the class `OrderConsumer`. \
Just run the following Maven command to build and deploy the applications:
```shell
mvn clean package
```
After build is finished go to the `target/kubernetes/` directory. Open the file `openshift.yml` and `knative.yml`.

With Knative CLI display a list of services:
```shell
kn services list
```
Then, go to OpenShift Console. Expand the `Serverless` tab. Choose `Serving`. Click `order-saga' -> `Edit Service`. \
In the YAML manifest configure minimal number of running instances to disable `scale-to-zero`:
```yaml
spec:
  template:
    metadata:
      annotations:
        autoscaling.knative.dev/minScale: '1'
```
Then run the following command to display a list of revisions:
```shell
kn revision list
```
List the Knative `Routes`:
```shell
kn route list
```
Display the details of the `order-saga` route:
```shell
kn route describe order-saga
```
Do the same thing using OpenShift Console.

## 6. Configure Knative Eventing

Create a secret on OpenShift for the Kafka SASL authentication:
```shell
oc create secret generic kafka-sasl-auth --from-literal=username=<your-username> --from-literal=password=<your-password>
```

In the `k8s` catalog define the file `kafka-source.yml` with the following content:
```yaml
apiVersion: sources.knative.dev/v1beta1
kind: KafkaSource
metadata:
  name: kafka-source-orders-payment
spec:
  consumerGroup: payment
  bootstrapServers:
    - my-cluster-kafka-bootstrap.kafka:9092
  topics:
    - <your-topic-name>
  sink:
    ref:
      apiVersion: serving.knative.dev/v1
      kind: Service
      name: payment-saga
    uri: /reserve
  net:
    sasl:
      enable: true
      user:
        secretKeyRef:
          name: kafka-sasl-auth
          key: username
      password:
        secretKeyRef:
          name: kafka-sasl-auth
          key: password
```
Alternatively, you can switch to the `Developer` perspective on OpenShift. Then choose `+Add` -> `Event Source` -> `Kafka Source`. \
Click button `Create Event Source`. Fill the required fields. Click `Create`.

Also add the file `kafka-binding.yaml` with the following content:
```yaml
apiVersion: bindings.knative.dev/v1beta1
kind: KafkaBinding
metadata:
  name: kafka-binding-payment-saga
spec:
  subject:
    apiVersion: serving.knative.dev/v1
    kind: Service
    name: payment-saga
  bootstrapServers:
    - my-cluster-kafka-bootstrap.kafka:9092
```
Change the value of the Kafka cluster address property in the `application.properties` file:
```properties
kafka.bootstrap.servers = ${KAFKA_BOOTSTRAP_SERVERS}
```
Configure HTTP `access.log` for incoming HTTP requests:
```properties
quarkus.http.access-log.enabled = true
quarkus.http.access-log.pattern = long
```
Redeploy the application on OpenShift Serverless. Observe the logs generated by the `payment-service`.

Display a list of Knative `Sources`:
```shell
kn source list
```

## 7. Emitting Events 

Finish the implementation of the SAGA pattern in the `OrderReserveFunction` class. You should use `Emitter` to implement imperative way of sending events:
```java
public class OrderReserveFunction {

    @Inject
    Logger log;

    @Inject
    AccountRepository repository;
    @Inject
    @Channel("reserve-events")
    Emitter<Order> orderEmitter;

    @Funq
    public void reserve(Order order) {
        log.infof("Received order: %s", order);
        doReserve(order);
    }

    private void doReserve(Order order) {
        Account account = repository.findById(order.getCustomerId());
        log.infof("Account: %s", account);
        if (order.getStatus().equals("NEW")) {
            account.setReservedAmount(account.getReservedAmount() + order.getAmount());
            account.setCurrentAmount(account.getCurrentAmount() - order.getAmount());
            order.setStatus("IN_PROGRESS");
            log.infof("Order reserved: %s", order);
            orderEmitter.send(order);
        } else if (order.getStatus().equals("CONFIRMED")) {
            account.setReservedAmount(account.getReservedAmount() - order.getAmount());
        }
        repository.persist(account);
    }

}
```
Configure outgoing channel in the `application.properties`:
```properties
mp.messaging.outgoing.reserve-events.connector = smallrye-kafka
mp.messaging.outgoing.reserve-events.topic = <your-topic-name>
mp.messaging.outgoing.reserve-events.value.serializer = io.quarkus.kafka.client.serialization.ObjectMapperSerializer
```
If needed, create a topic on Kafka using AMQ Streams operator using OpenShift Console or the following YAML manifest e.g. `kafka-topic.yaml`:
```yaml
apiVersion: kafka.strimzi.io/v1beta2
kind: KafkaTopic
metadata:
  name: <your-topic-name>
  labels:
    strimzi.io/cluster: my-cluster
  namespace: kafka
spec:
  partitions: 10
  replicas: 3
```
Then apply the changes:
```shell
oc apply -f kafka-topic.yaml

```
Switch to the `shipment-saga` module. Once again add the following dependency into Maven `pom.xml`:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-funqy-http</artifactId>
</dependency>
```
Open the file `pl.redhat.samples.serverless.shipment.function.OrderReserveFunction`. \
Provide the similar implementation of the SAGA pattern for `Product` entity. \
Then configure outgoing channel for `Emitter`. \
Add new `KafkaSource` and `KafkaBinding` objects dedicated for the `shipment-saga`.

Display a list of Knative `Sources`:
```shell
kn source list
```
Display a list of running pods:
```shell
oc get pod --field-selector=status.phase=Running
```