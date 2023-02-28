FROM openjdk:8-jdk-alpine
# ADD target/Gestimo*.jar app.jar
RUN mkdir -p /usr/GestimoSpringBackend
RUN mkdir -p /usr/GestimoSpringBackend/src/main/resources/etats/templates/print
WORKDIR /usr/GestimoSpringBackend
COPY . /usr/GestimoSpringBackend/

# RUN addgroup seve; adduser --ingroup seve --disabled-password seve
# RUN apk --update add fontconfig ttf-dejavu
# RUN mvn clean install
RUN apk add --no-cache msttcorefonts-installer fontconfig
RUN update-ms-fonts

# RUN mvn clean install
VOLUME /usr/GestimoSpringBackend/
WORKDIR /usr/GestimoSpringBackend
ENTRYPOINT ["java","-jar","target/GestimoSpringBackend-0.0.1-SNAPSHOT.jar"]
