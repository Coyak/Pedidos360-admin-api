# Dockerfile para Pedidos360 - Admin API (Puerto 8084)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENV SERVER_PORT=8084
EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
