#!/bin/bash

set -e

cd "$(dirname "$0")"

cd analytics-service
mvn clean install -DskipTests
cd ..

cd auth-service
mvn clean install -DskipTests
cd ..

cd gateway-service
mvn clean install -DskipTests
cd ..

cd project-service
mvn clean install -DskipTests
cd ..
