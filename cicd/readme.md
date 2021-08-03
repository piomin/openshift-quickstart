Adding privileged to the `pipeline` `ServiceAccount` is required for some tasks.
```shell
oc adm policy add-scc-to-user privileged -z pipeline -n piotr-cicd
```

For `sonarqube-scanner` task and SonarCloud we need to add `sonar.properties`.
```shell
oc create configmap sonar-properties --from-file="sonar-project.properties"
```