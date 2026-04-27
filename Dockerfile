FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build

# Copy parent POM
COPY pom.xml .

# Copy all module POMs
COPY analytics-service/pom.xml ./analytics-service/pom.xml
COPY auth-service/pom.xml ./auth-service/pom.xml
COPY gateway-service/pom.xml ./gateway-service/pom.xml
COPY project-service/pom.xml ./project-service/pom.xml

# Resolve all dependencies
RUN mvn dependency:go-offline

# Copy all source code
COPY analytics-service/src ./analytics-service/src
COPY auth-service/src ./auth-service/src
COPY gateway-service/src ./gateway-service/src
COPY project-service/src ./project-service/src

# Build all modules
RUN mvn clean package -DskipTests

# Runtime stage for gateway-service
FROM eclipse-temurin:17-jre AS gateway-runtime
WORKDIR /app
COPY --from=builder /build/gateway-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Runtime stage for auth-service
FROM eclipse-temurin:17-jre AS auth-runtime
WORKDIR /app
COPY --from=builder /build/auth-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

# Runtime stage for project-service
FROM eclipse-temurin:17-jre AS project-runtime
WORKDIR /app
COPY --from=builder /build/project-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

# Runtime stage for analytics-service
FROM eclipse-temurin:17-jre AS analytics-runtime
WORKDIR /app
COPY --from=builder /build/analytics-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]