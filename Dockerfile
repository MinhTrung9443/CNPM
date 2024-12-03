#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
# FROM openjdk:11-jdk-slim

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /target/CNPM-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
