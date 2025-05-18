# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and configuration files
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

# Grant execution permission to the Gradle wrapper
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew shadowJar

# Set the entry point to run the Consumer class
CMD ["java", "-cp", "build/libs/kafka-experiments-1.0-SNAPSHOT-all.jar", "com.alihmzyv.consumer.heartbeat.interval.ms.Consumer"]