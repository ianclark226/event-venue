# ---------- Stage 1: Build with Maven ----------
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven wrapper and config
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Ensure wrapper is executable
RUN chmod +x mvnw

# Download dependencies (cached if pom.xml unchanged)
RUN ./mvnw dependency:go-offline -B

# Copy source code and build JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run the JAR ----------
FROM openjdk:17
WORKDIR /app

# Copy only the built jar from the build stage
COPY --from=build /app/target/event-venue-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Railway provides $PORT at runtime
CMD java -jar \
    -Dspring.datasource.url=${SPRING_DATASOURCE_URL} \
    -Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
    -Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
    -Dserver.port=${PORT} \
    app.jar