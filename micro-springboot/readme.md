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

With web console. \
Go to: https://console-openshift-console.apps.qyt1tahi.eastus.aroapp.io/. \
Switch do the `Developer` perspective. \
Choose `Add+` -> `From Git`. \
Type as Git Repo URL: `https://github.com/piomin/openshift-quickstart.git`, Context dir: `/micro-springboot/person-service`, Git reference: `workshops`.
Then choose Builder Image version: openjdk-11-ubi8. Then override 'Application name' and `Name` with `person-service`.
Leave default on the other fields. Click `Create`. 
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