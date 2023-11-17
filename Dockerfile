FROM maven:3.8.7-amazoncorretto-19
COPY build/libs/mancala-app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
