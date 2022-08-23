FROM adoptopenjdk/openjdk11:alpine-jre

RUN chmod +x ./gradlew

RUN ./gradlew build

WORKDIR /root

COPY build/libs/GST-BACKEND-0.0.1-SNAPSHOT.jar app.jar

CMD java -jar app.jar