#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM amazoncorretto:11-alpine-jdk
COPY --from=build /home/app/target/todo-0.0.1.jar /usr/local/lib/todo.jar
ENTRYPOINT ["java","-jar","/todo.jar"]