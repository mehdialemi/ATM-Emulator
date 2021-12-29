FROM openjdk:11-jdk-slim
EXPOSE 8888
COPY target/bank-service.jar bank-service.jar
ENTRYPOINT ["java", "-jar", "bank-service.jar"]
