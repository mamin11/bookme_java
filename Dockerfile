FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/booking-api-0.0.1-SNAPSHOT.jar booking-api-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/booking-api-0.0.1-SNAPSHOT.jar"]