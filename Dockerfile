FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/cgtest-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} cgtest-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/cgtest-0.0.1-SNAPSHOT.jar"]