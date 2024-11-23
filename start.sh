#!/bin/bash

# check for JDK 17
if ! command -v java &> /dev/null || ! java --version 2>&1 | grep -q "17\." ; then
    echo "Java JDK 17 is not installed. Installing..."

    sudo apt update
    sudo apt install -y openjdk-17-jdk
fi

# check for Maven
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Installing..."

    sudo apt install -y maven
fi


echo "Building the Spring Boot application..."
mvn clean install

# check for correct build
if [ $? -ne 0 ]; then
    echo "Build failed. Please fix the errors and try again."
    exit 1
fi

JAR_FILE=$(ls target/*.jar | head -n 1)

# check for findable .jar file
if [ -z "$JAR_FILE" ]; then
    echo "JAR file not found in the target directory. Build might have failed."
    exit 1
fi

echo "Running the JAR file: $JAR_FILE"

java -jar "$JAR_FILE"
