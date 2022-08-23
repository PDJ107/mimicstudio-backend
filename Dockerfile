FROM openjdk:11-jdk as builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=builder build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT [" java", "-jar", "app.jar"]
