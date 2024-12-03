FROM eclipse-temurin:21-jdk
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
COPY target/CNPM-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]