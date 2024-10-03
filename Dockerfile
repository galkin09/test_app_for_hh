FROM openjdk:17

LABEL authors="Sanga"

WORKDIR .

COPY target/testApp-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java","-jar","/testApp-0.0.1-SNAPSHOT.jar"]