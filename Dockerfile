# Use Maven + JDK image to build and run the app
FROM maven:3.9.3-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Package the application (skip tests)
RUN mvn clean package -DskipTests

# Expose port (default Spring Boot port)
EXPOSE 8080

# Run the JAR
CMD ["java", "-jar", "target/event-venue-0.0.1-SNAPSHOT.jar"]