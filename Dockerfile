FROM arimacdigital/openjdk-maven:21-3.9.5-v1 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

RUN ls -l target/

FROM openjdk:21-jdk-slim

COPY --from=build /app/target/transactionmanager-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
