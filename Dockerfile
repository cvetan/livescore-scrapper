FROM openjdk:8-alpine

COPY target/uberjar/livescore-scrapper.jar /livescore-scrapper/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/livescore-scrapper/app.jar"]
