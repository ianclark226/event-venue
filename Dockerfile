FROM openjdk:17
WORKDIR /app

# Copy the JAR built on the host
COPY target/event-venue-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8088

# Default environment variables (can be overridden in docker-compose.yml)
ENV DB_URL=jdbc:mysql://mysql:3306/event_venue_DB
ENV DB_USER=root
ENV DB_PASSWORD=rootpw

CMD java -jar \
    -Dspring.datasource.url=${DB_URL} \
    -Dspring.datasource.username=${DB_USER} \
    -Dspring.datasource.password=${DB_PASSWORD} \
    app.jar