# Start with a base image containing Maven, JDK, and bash
FROM maven:3.8.1-openjdk-17-slim AS build

# Install bash and other utilities (if needed)
RUN apt-get update && apt-get install -y \
    bash \
 && rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to download dependencies
COPY pom.xml .

# Download dependencies specified in pom.xml
RUN mvn -B -f pom.xml dependency:go-offline

# Copy the rest of the project files
COPY ./src /app/src
COPY ./target /app/target

# Build the application
RUN mvn -B -f pom.xml clean package

# Create a new stage for running the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage to the new stage
COPY --from=build /app/target/session-log-processor-1.0.jar /app/session-log-processor.jar

# Copy the log file into the container
COPY ./src/main/resources/sessiondatalog.txt /app/sessiondatalog.txt

# Set the entry point to execute the JAR file with the log file path as an argument
ENTRYPOINT ["java", "-jar", "/app/session-log-processor.jar", "/app/sessiondatalog.txt"]
