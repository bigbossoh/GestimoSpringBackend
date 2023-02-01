FROM openjdk:8-jdk-alpine
# ADD target/Gestimo*.jar app.jar
RUN mkdir -p /usr/GestimoSpringBackend
WORKDIR /usr/GestimoSpringBackend
COPY . /usr/GestimoSpringBackend/

# RUN mvn clean install
VOLUME /usr/GestimoSpringBackend/
WORKDIR /usr/GestimoSpringBackend
ENTRYPOINT ["java","-jar","target/GestimoSpringBackend-0.0.1-SNAPSHOT.jar"]
