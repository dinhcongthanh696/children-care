# Use an official OpenJDK 8 runtime as a parent image
FROM openjdk:8-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at the specified working directory
COPY target/Children-Care-0.0.1-SNAPSHOT.jar /app/

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "Children-Care-0.0.1-SNAPSHOT.jar"]