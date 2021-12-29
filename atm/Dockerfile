FROM openjdk:11-jdk-slim
EXPOSE 8080
COPY target/atm-service.jar atm-service.jar
ENTRYPOINT ["java", "-jar", "atm-service.jar"]