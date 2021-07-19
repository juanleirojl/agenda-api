#FROM openjdk:12-jdk-alpine
#EXPOSE 8080
#RUN mkdir /app
#COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
#ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]

FROM openjdk:12-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ADD /build/libs/agenda-api-0.0.1-SNAPSHOT.jar agenda-api.jar
ENTRYPOINT ["java","-jar","agenda-api.jar"]