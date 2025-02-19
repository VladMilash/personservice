FROM eclipse-temurin:21-jammy
WORKDIR /opt/personservice
COPY build/libs/personservice-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]