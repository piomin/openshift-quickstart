# Workshops: Java Development on OpenShift [![Twitter](https://img.shields.io/twitter/follow/piotr_minkowski.svg?style=social&logo=twitter&label=Follow%20Me)](https://twitter.com/piotr_minkowski)

[![CircleCI](https://circleci.com/gh/piomin/openshift-quickstart.svg?style=svg)](https://circleci.com/gh/piomin/openshift-quickstart)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=piomin_openshift-quickstart)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=piomin_openshift-quickstart&metric=bugs)](https://sonarcloud.io/dashboard?id=piomin_openshift-quickstart)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=piomin_openshift-quickstart&metric=coverage)](https://sonarcloud.io/dashboard?id=piomin_openshift-quickstart)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=piomin_openshift-quickstart&metric=ncloc)](https://sonarcloud.io/dashboard?id=piomin_openshift-quickstart)


In this repo you can find several workshops introducing the topic "Java development on OpenShift". \
For accessing it you will have to switch to the dedicated branch in this repository following instructions below. \
You can go through these workshops using:
* [CRC (Code Ready Containers)](https://console.redhat.com/openshift/create/local) - a single node OpenShift on your laptop
* [On-line Developer Sandbox](https://developers.redhat.com/developer-sandbox/get-started) - only for workshops 1 and 2
* Dedicated instance of OpenShift - in that case you need to [contact me](#contact-info) for temporary access 
Currently there are 4 different workshops available:

## 1. Microservices on OpenShift with Spring Boot
Introduction to the Java development on OpenShift based on the Spring Boot framework [~3h]. \
You will learn how to:
- easily deploy and debug applications on OpenShift using `odo`
- follow best practices for running apps on Kubernetes-native platform
- run and monitor microservices properly

[Instruction](https://github.com/piomin/openshift-quickstart/tree/workshops/micro-springboot)

## 2. Introduction to Quarkus
Introduction to the Java development on OpenShift based on the Quarkus framework [~3h]. \
You will learn how to:
- build and run a simple REST-based apps on OpenShift
- deploy Quarkus applications on OpenShift using Maven command
- use interesting Quarkus features during development

[Instruction](https://github.com/piomin/openshift-quickstart/tree/master/basic-with-db)

## 3. Event-driven architecture with Kafka and Spring Cloud Stream
Introduction to the event-driven architecture on OpenShift based on the Kafka and Spring Cloud Stream framework [~8h]. \
You will learn how to:
- integrate your Spring Boot application with Kafka using Spring Cloud Stream
- basics about Kafka partitioning, consumer grouping, offsets etc. using Kafka binder for Spring Cloud Stream
- use event-driven design patterns
- use Kafka Streams

[Instruction](https://github.com/piomin/openshift-quickstart/tree/event-driven/event-driven)

## 4. Knative Eventing on OpenShift with Quarkus
Building event-driven application with Quarkus and Kafka and running them on OpenShift Serverless. [~3h]. \
You will learn how to:
- deploy application on Knative easily
- integrate Quarkus with the Kafka broker
- configure Knative event routing

[Instruction](https://github.com/piomin/openshift-quickstart/tree/serverless/serverless)

## Contact info
For more information or help you can contact me directly via e-mail: piotr.minkowski@gmail.com.