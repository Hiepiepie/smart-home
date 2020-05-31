
#Finaler Container
FROM openjdk:11-jdk
COPY /tmp/target /app/target
WORKDIR /app
ENTRYPOINT ["java","-cp","/app/target/classes","ProviderServer.ProviderServer"]