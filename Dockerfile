FROM openjdk:8-jdk-alpine
# ADD target/Gestimo*.jar app.jar
RUN mkdir -p /home/astairenazaire/spring-project/GestimoSpringBackend
RUN mkdir -p /home/astairenazaire/spring-project/GestimoSpringBackend/src/main/resources/etats/templates/print
WORKDIR /home/astairenazaire/spring-project/GestimoSpringBackend
COPY . /home/astairenazaire/spring-project/GestimoSpringBackend/

# RUN addgroup seve; adduser --ingroup seve --disabled-password seve
# RUN apk --update add fontconfig ttf-dejavu
# RUN mvn clean install
RUN apk add --no-cache msttcorefonts-installer fontconfig
RUN update-ms-fonts

# RUN mvn clean install
# VOLUME /usr/GestimoSpringBackend/
VOLUME /home/astairenazaire/spring-project/GestimoSpringBackend/

# WORKDIR /usr/GestimoSpringBackend
WORKDIR /home/astairenazaire/spring-project/GestimoSpringBackend
ENTRYPOINT ["java","-jar","target/GestimoSpringBackend-0.0.1-SNAPSHOT.jar"]
