FROM openjdk:17-jdk-slim
EXPOSE 8080
ARG JAR_FILE=target/UkPostalCodeDistanceService-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} UkPostalCodeDistanceService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/UkPostalCodeDistanceService-0.0.1-SNAPSHOT.jar"]
