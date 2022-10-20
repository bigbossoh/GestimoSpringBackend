FROM openjdk:8-jdk-alpine
ADD target/Gestimo*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
