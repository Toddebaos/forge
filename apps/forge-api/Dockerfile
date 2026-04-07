# Steg 1 — bygg applikationen
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn ./.mvn
RUN ./mvnw package -DskipTests

# Steg 2 — kör applikationen
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Kör som non-root user
RUN addgroup -S forge && adduser -S forge -G forge
USER forge

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
