FROM maven:3.8-jdk-11 AS builder
WORKDIR /tmp
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY . .
RUN mvn package

FROM openjdk:11
WORKDIR /app
COPY --from=builder /tmp/target/auth-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "/app/auth-service-0.0.1-SNAPSHOT.jar"]
