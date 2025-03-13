#!/bin/bash

mvn package -Dmaven.test.skip

java -javaagent:src/main/resources/elastic-apm-agent-1.51.0.jar \
-Delastic.apm.service_name=apm-demo-application \
-Delastic.apm.server_urls=http://localhost:8200 \
-Delastic.apm.secret_token= \
-Delastic.apm.environment=development \
-Delastic.apm.application_packages=com.cosmin.tutorials.apm \
-jar target/apm-0.0.1-SNAPSHOT.jar