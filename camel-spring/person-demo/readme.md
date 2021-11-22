## Spring Boot Camel REST Example

### Start Oracle database instance

To start it on Docker:
```shell
docker run -d --name oracle -p 1521:1521 container-registry.oracle.com/database/express:latest
```

### Run application locally

To start Spring Boot application in dev mode:
```shell
$ mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
```

Before launching the app change credentials to your database in `application-dev.yaml`:
```yaml
spring:
  application:
    name: person-service
  datasource:
    url: jdbc:oracle:thin:@localhost:1521/XE
    username: ${USER}
    password: ${PASSWORD}
```

Then, you can access your application on `8080` port. To test REST API you can access Swagger UI:
```shell
http://localhost:8080/swagger-ui.html
```

To access Hawtio console:
```shell
http://localhost:8080/actuator/hawtio
```

### Run your application on OpenShift

You can use `oc` client to launch from Git or `odo` to run from local code. First, create app with `odo`:
```shell
odo create java --s2i person-demo
```

Then push your app to OpenShift:
```shell
odo push
```

Verify logs:
```shell
odo log -f
```