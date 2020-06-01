FROM maven:3.6.3-jdk-8 AS BUILD
#Kopiere alle Dateien
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/


RUN mvn compile

FROM openjdk:11-jdk

COPY --from=BUILD /tmp/target /app/target
WORKDIR /app
ENTRYPOINT ["java","-cp","/app/target/classes","Zentral.Zentral"]