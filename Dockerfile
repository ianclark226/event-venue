# Use Maven with JDK to build and run the app
FROM maven:3.9.3-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Package the application (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Expose the port (Railway injects PORT automatically)
EXPOSE 8080

# Default: Run with Railway config (no profile override needed)
CMD ["java", "-jar", "target/event-venue-0.0.1-SNAPSHOT.jar"]

# To run locally with a profile, you can override the CMD like:
# docker run -e SPRING_PROFILES_ACTIVE=local <image-name>