FROM openjdk:17
WORKDIR /app

COPY target/event-venue-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080   # or whatever port you want; Railway’s $PORT might be mapped

CMD java -jar \
    -Dspring.datasource.url=${SPRING_DATASOURCE_URL} \
    -Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
    -Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
    -Dserver.port=${PORT:8080} \
    app.jar