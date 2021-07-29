# Workshop: Microservices on OpenShift with Spring Boot

## Prerequisites

1. Install `oc` client
```shell
$ oc version
Client Version: 4.6.12
Server Version: 4.7.16
Kubernetes Version: v1.20.0+2817867
```

2. Install `odo` client
```shell
$ odo version
odo v2.2.1 (17a078b67)

Server: https://api.qyt1tahi.eastus.aroapp.io:6443
Kubernetes: v1.20.0+2817867
```

3. Install JDK11+
```shell
$ java --version
java 15.0.2 2021-01-19
Java(TM) SE Runtime Environment (build 15.0.2+7-27)
Java HotSpot(TM) 64-Bit Server VM (build 15.0.2+7-27, mixed mode, sharing)
```

4. Install and configure Maven
```shell
$ mvn -version
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: /Users/pminkows/apache-maven-3.6.3
Java version: 15.0.2, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk-15.0.2.jdk/Contents/Home
Default locale: en_PL, platform encoding: UTF-8
OS name: "mac os x", version: "10.15.7", arch: "x86_64", family: "mac"
```

5. Install `git` client
```shell
$ git version
git version 2.24.3 (Apple Git-128)
```

## Step 1: Deploy applications using `oc` client

1. Login to the cluster

With `oc` client.
```shell
$ oc login -u $USER -p $PASSWORD --server=https://api.qyt1tahi.eastus.aroapp.io:6443
```

2. Login with web console. 

Go to: https://console-openshift-console.apps.qyt1tahi.eastus.aroapp.io/. \
Switch do the `Developer` perspective. \
Choose `Add+` -> `From Git`. \
Type as Git Repo URL: `https://github.com/piomin/openshift-quickstart.git`, Context dir: `/micro-springboot/person-service`, Git reference: `workshops`. \
Then choose Builder Image version: `openjdk-11-ubi8`. Then override 'Application name' and `Name` with `person-service`. \
Leave default on the other fields. Click `Create`. A new application is created on OpenShift. \

You are redirected to the `Topology` view. \
Click on Java Duke icon -> `Resources` -> See `Pods` -> Click `View logs`. \
Optionally click `Show in Kibana`. \

Back to the `Topology` view. Click on Java Duke icon -> `Resources` -> See `Builds` -> Click `#1`. Then choose tab `Logs`. \
Back to the `Topology` view. Click on Java Duke icon -> `Resources` -> See `Routes` -> Click it.
```shell
$ curl http://person-service-piotr-dev.apps.qyt1tahi.eastus.aroapp.io/persons 
[]
```

Back to the `Topology` view. Click on Java Duke icon -> `Details` -> scale up number of instances

## Step 2: Develop applications using `odo` client

Clone the repository from GitHub. You can do it using your IDE.
```shell
$ git clone https://github.com/piomin/openshift-quickstart.git
```
There are two applications. In the first step, we are going to deploy `person-service`.
```shell
cd micro-springboot/person-service
```
Replace the following line in your `pom.xml`.
```xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```
With Postgresql driver.
```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
```
Then go to the OpenShift console. Choose `Add+` -> `Database` -> `PostgreSQL` -> `Instantiate Template`. \
Type `person-db` as `Database Service Name`, leave default values in the rest of fields. Click `Create` button. \
Then go to `Secrets` and choose `postgresql` secret. \
Go back to your source code. You are in the `person-service` directory. First, list all available components with `odo`.
```shell
$ odo catalog list components
```

We choose S2I with Java.
```shell
$ odo create java --s2i person-app
```

Odo created the `.odo` directory and `devfile.yaml` configuration file. You can view their content. \
Now let's add the following configuration properties to our `application.yml` file. 
```yaml
spring:
  datasource:
    url: jdbc:postgresql://person-db:5432/${DATABASE_NAME}
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}
```

Set environment variables for `odo`.
```shell
$ odo config set --env DATABASE_NAME=<your-value> --env DATABASE_USER=<your-value> --env DATABASE_PASSWORD=<your-value>
```

Finally, deploy the application to OpenShift.
```shell
$ odo push
```

Go to the console -> `Topology`. Verify if `person-app` deployment exists. \
Go to `Project` -> `Routes`. Call the route with port `8080`. Call the following endpoint.
```shell
$ curl http://person-service-piotr-dev.apps.qyt1tahi.eastus.aroapp.io/persons 
[]
```

Then, start development. Run the following command in the `person-service` directory.
```shell
$ odo watch
```

Then go to the `PersonController`. Finish the implementation by replacing TODO with a code. Then save changes and switch back to the terminal. \
Call the following endpoint once again.
```shell
$ curl http://person-service-piotr-dev.apps.qyt1tahi.eastus.aroapp.io/persons
```

Then, go to the `pom.xml`. Include the following artifact into dependencies.
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Then call the endpoint `GET /actuator` using your web browser. Then click endpoint `/actuator/health` and see what is the result. \
Go to the `application.yml`. Add the following lines.
```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint.health:
      show-details: always
      group:
        readiness:
          include: db
      probes:
        enabled: true
```

After application redeploy type `CTRL+S` to stop `odo watch`. \

Go to the `Topology` view. Click `person-app` icon. Click `Add health checks`. Then click `Add readiness probe` and type `actuator/readiness`. \
Analogically add a liveliness probe. Then click `Add` button. The application should be redeployed. \
Then call the endpoint `GET \actuator\metrics` using web browser. See HTTP traffic metrics. \

Finally, add the following dependencies.
```xml

```
Push changes into OpenShift with odo.
```shell
$ odo push
```

## Step 3: Debugging with `odo` client
We are going to deploy `insurance-service`. First, got to the `micro-springboot/insurance-service` directory. \
```shell
$ cd micro-springboot/insurance-service
```
Create S2I Java application with `odo`:
```shell
$ odo create java --s2i insurance-app
```
Then go to the OpenShift console. Choose `Add+` -> `Database` -> `PostgreSQL` -> `Instantiate Template`. \
Type `insurance-db` as `Database Service Name`, leave default values in the rest of fields. Click `Create` button. \
Then go to `Secrets` and choose `insurance-db` secret. \
Push application to OpenShift:
```shell
$ odo push
```
Go to the `topology` view. Verify deployment and application logs. What is the reason that application is not running? \
Choose `Actions` -> `Edit deployment` -> `Environment`. \
Choose `Add from ConfigMap or Secret`. Add three environment variables from code visible below. Choose `insurance-db` secret as a resource and a right key. \
Then click `Save` button. Verify application logs after redeploy.
```yaml
spring:
  datasource:
    url: jdbc:postgresql://person-db:5432/${DATABASE_NAME}
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}
```
Set breakpoint inside the `InsuranceController` class in the following line and detect why person==null.
```java
@GetMapping("/{id}/details")
public InsuranceDetails getInsuranceDetailsById(@PathParam("id") Integer id) {
    Insurance insurance = repository.findById(id).orElseThrow(); // set breakpoint
    Person person = personClient.getPersonById(insurance.getId());
    // TODO - detect why person==null
    // TODO - finish implementation
    return null;
}
```
Deploy application in DEBUG mode.
```shell
$ odo debug
```

## Step 4: Modifying OpenShift Topology View
Go to the `Topology` view. \
Edit `person-app` -> `Actions` -> `Edit labels`. Add labels `app.kubernetes.io/part-of=person-service`, `app.openshift.io/runtime=spring-boot`, `app.kubernetes.io/component=backend`. \

Repeat the same step for `insurance-app` by adding names related to that application. \
Edit `person-db` -> `Actions` -> `Edit labels`. Add labels `app.kubernetes.io/part-of=person-service`, `app.kubernetes.io/instance=person-db`, `app.kubernetes.io/component=database`, `app.openshift.io/runtime=postgresql`. \

Repeat the same step for `insurance-db` by adding names related to that application. \

Once again edit `person-app` -> `Actions` -> `Edit annotations`. Add annotation `app.openshift.io/connects-to=person-db`. \
Then edit `insurance-app` -> `Actions` -> `Edit annotations`. Add annotation `app.openshift.io/connects-to=insurance-db`. 

## Step 5: Logging and communication
Find the `PersonClient` class. Change the current implementation from the following:
```java
public Person getPersonById(Integer personId) {
    try {
        return restTemplate.getForObject(personServiceUrl + "/persons/{id}", Person.class, personId);
    } catch (HttpStatusCodeException e) {
        return null;
    }
}
```
Into the implementation that sets a header `CorrelationId` on the request and puts it as MDC field into the thread context.
```java
public Person getPersonById(Integer personId) {
    try {
        String uuid = UUID.randomUUID().toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("CorrelationId", uuid);
        HttpEntity entity = new HttpEntity(headers);
        MDC.put("CorrelationId", uuid);
        ResponseEntity<Person> response = restTemplate
                .exchange(personServiceUrl + "/persons/{id}", HttpMethod.GET, entity, Person.class, personId);
        return response.getBody();
    } catch (HttpStatusCodeException e) {
        return null;
    }
}
```
Go to the `person-service`. Open `PersonController`. Change the implementation of `getById()` method into the following:
```java
@GetMapping("/{id}")
public Person getById(@PathVariable("id") Integer id, @RequestHeader("CorrelationId") String correlationId) {
    LOG.info("Get person by id={}", id);
    MDC.put("CorrelationId", correlationId);
    return repository.findById(id).orElseThrow();
}
```
Now, let's call the `GET /insurances/{id}/details` endpoint from `insurance-service`.
```shell
$ curl http://insurance-service-piotr-dev.apps.qyt1tahi.eastus.aroapp.io/insurances/1/details
```
View logs from `insurance-service` pod -> `Show in Kibana`. Do the same for `person-service`. We want to display the logs filtered by `CorrelationId`. 

Repeat those steps for both applications. Include the following dependency into `pom.xml`:
```xml
<dependency>
  <groupId>net.logstash.logback</groupId>
  <artifactId>logstash-logback-encoder</artifactId>
  <version>6.6</version>
</dependency>
```
Add the file `logback-spring.xml` to the `/src/main/resources` directory with the following content:
```xml
<configuration>
    <appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    </appender>
    <logger name="jsonLogger" additivity="false" level="DEBUG">
        <appender-ref ref="consoleAppender"/>
    </logger>
    <root level="INFO">
        <appender-ref ref="consoleAppender"/>
    </root>
</configuration>
```
Redeploy both application with `odo push` command. \
Then, let's call the `GET /insurances/{id}/details` endpoint from `insurance-service` once again.
```shell
$ curl http://insurance-service-piotr-dev.apps.qyt1tahi.eastus.aroapp.io/insurances/1/details
```

View logs from `insurance-service` pod -> `Show in Kibana`. Do the same for `person-service`. Now, you should be able to display the logs filtered by `CorrelationId`.

## Step 6: Metrics

Add the following annotations to the deployment of both applications:
```properties
prometheus.io/scrape="true"
prometheus.io/path=/actuator/prometheus
prometheus.io/port="8080"
```

Add the following dependency to the `pom.xml`:
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

In your web browser display `http://employee-service-piotr-dev.apps.qyt1tahi.eastus.aroapp.io/actuator/prometheus`. \
Go to the site `https://prometheus-k8s-openshift-monitoring.apps.qyt1tahi.eastus.aroapp.io` in your web browser. \
See the value for e.g. `http_requests_total`.