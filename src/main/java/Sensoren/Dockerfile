FROM maven:3.6.3-jdk-8 AS BUILD
#Kopiere alle Dateien
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/

RUN mvn package

#Finaler Container
FROM openjdk:11
EXPOSE 1234/udp
COPY --from=BUILD /tmp/target /app/target
WORKDIR /app/target
ENTRYPOINT ["java","-jar","/app/target/mainClass-jar-with-dependencies.jar"]