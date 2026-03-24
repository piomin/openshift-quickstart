# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a multi-module Maven project containing educational workshops for Java development on OpenShift. The repository demonstrates various Java frameworks and patterns for cloud-native applications.

## Technology Stack

- Java 21
- Spring Boot 3.5.x with Spring Cloud 2025.1.1
- Quarkus 3.30.8
- Apache Kafka with Spring Cloud Stream
- PostgreSQL databases
- Testcontainers for integration testing
- Maven 3.x for builds
- Dekorate/Quarkus extensions for OpenShift deployment

## Project Structure

The repository is organized into independent workshop modules:

- **basic-with-db**: Quarkus applications (person-app, insurance-app) demonstrating REST APIs with PostgreSQL
- **micro-springboot**: Spring Boot microservices (person-service, insurance-service) with database integration
- **event-driven**: Event-driven architecture using Kafka and Spring Cloud Stream, including:
  - Producer/consumer examples
  - Event gateway, order-service, payment-service, shipment-service
  - Order-query-service for CQRS pattern
  - Kafka Streams examples (consumer-streams, producer-streams)
- **messaging**: Simple ActiveMQ messaging examples (producer, consumer, counter)
- **micro-view**: Microservices view pattern (department, employee, organization)
- **camel-spring**: Apache Camel integration with Spring Boot
- **mesh-with-db**: Service mesh demonstrations with Quarkus
- **cpu-killer**: Utility application for resource testing

Each module is self-contained with its own pom.xml and can be built independently.

## Build Commands

Build entire project:
```bash
mvn clean install
```

Build specific module:
```bash
cd <module-name>
mvn clean install
```

Run tests:
```bash
mvn test
```

Run tests for specific module:
```bash
cd <module-name>
mvn test
```

Skip tests during build:
```bash
mvn clean install -DskipTests
```

Generate code coverage report (JaCoCo):
```bash
mvn test
# Reports generated in target/site/jacoco/
```

## Running Applications

### Spring Boot Applications (micro-springboot, event-driven, etc.)

Run locally:
```bash
cd <module-name>/<service-name>
mvn spring-boot:run
```

### Quarkus Applications (basic-with-db, mesh-with-db)

Run in dev mode:
```bash
cd <module-name>/<app-name>
mvn quarkus:dev
```

Build native image:
```bash
mvn clean package -Pnative
```

## Testing

All modules use JUnit 5 for testing. Integration tests leverage Testcontainers to spin up real dependencies (PostgreSQL, Kafka) in Docker containers.

Example test execution pattern:
- Spring Boot tests: Use `@SpringBootTest` with `@Testcontainers` and `@ServiceConnection`
- Quarkus tests: Use `@QuarkusTest` with Dev Services (auto-configured Testcontainers)

Run single test class:
```bash
mvn test -Dtest=<TestClassName>
```

## Configuration

### Spring Boot Applications
- Configuration in `src/main/resources/application.yml`
- Environment variables for database credentials:
  - `DATABASE_HOST`, `DATABASE_NAME`, `DATABASE_USER`, `DATABASE_PASS`
- Dekorate annotations in application.yml for OpenShift deployment configuration

### Quarkus Applications
- Configuration in `src/main/resources/application.properties`
- Profile-specific config using `%prod`, `%dev`, `%test` prefixes
- Environment variables (PostgreSQL):
  - `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB`
- Liquibase for database migrations (enabled by default)

## OpenShift Deployment

### Quarkus Deployment
Quarkus apps use the OpenShift extension for deployment:
```bash
mvn clean package -Dquarkus.kubernetes.deploy=true
```

Configuration in application.properties:
- `quarkus.kubernetes.deployment-target=openshift`
- `quarkus.openshift.route.expose=true`
- Secret mappings for database credentials

### Spring Boot Deployment
Spring Boot apps use Dekorate for generating OpenShift manifests:
- Manifests auto-generated in `target/classes/META-INF/dekorate/`
- Configuration via `dekorate.*` properties in application.yml
- Supports auto-deploy with `dekorate.openshift.auto-deploy-enabled=true`

## Key Architectural Patterns

### Event-Driven Architecture (event-driven module)
- Spring Cloud Stream for Kafka integration
- Functional programming model using `java.util.function.Function/Consumer`
- Topic bindings configured in application.yml
- CQRS pattern demonstrated in order-query-service
- Event sourcing patterns in order/payment/shipment services

### Database Integration
- JPA/Hibernate for Spring Boot applications
- Hibernate ORM with Panache for Quarkus applications
- Liquibase migrations for Quarkus (changeLog in db/changeLog.sql)
- Hibernate auto-DDL for Spring Boot (ddl-auto: create)

### Health Checks & Monitoring
- Spring Boot Actuator endpoints exposed at `/actuator/*`
- Quarkus health checks at `/q/health` (liveness/readiness)
- Prometheus metrics enabled
- Custom readiness probes include database connectivity

## CI/CD

The project uses CircleCI for continuous integration:
- Test job: Runs `mvn test` on all modules
- Analyze job: Runs SonarCloud analysis via `mvn verify sonar:sonar`
- Configuration in `.circleci/config.yml`

## Code Quality

- SonarCloud integration for code quality metrics
- JaCoCo plugin for code coverage tracking
- Coverage reports uploaded to SonarCloud during CI
