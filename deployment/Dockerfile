FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY build/libs/ratings-service.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Xms256m -Xmx512m", "-jar","/app.jar"]
