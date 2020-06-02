FROM maven:3.6.3-jdk-8 AS BUILD
#Kopiere alle Dateien
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/


RUN mvn package

#Finaler Container
FROM openjdk:11
EXPOSE 9090
COPY --from=BUILD /tmp/target /app/target
WORKDIR /app/target
RUN apt-get update && apt-get install telnet
ENTRYPOINT ["java","-jar","/app/target/ProviderServer-jar-with-dependencies.jar"]