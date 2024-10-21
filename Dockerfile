FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/TestForJavaCode-1.0-SNAPSHOT.jar /app/TestForJavaCode.jar

ENTRYPOINT ["java", "-jar", "/app/TestForJavaCode.jar"]

EXPOSE 8080