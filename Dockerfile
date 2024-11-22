# Build Stage
FROM gradle:7.5-jdk17 AS build

WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src /app/src/

# Build the project
RUN gradle clean build -x test

# Final Stage
FROM openjdk:17-jdk-slim

WORKDIR /app
# Copy the JAR file dynamically (using a wildcard to select the JAR in the build/libs folder)
COPY --from=build /app/build/libs/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
