FROM amazoncorretto:11-alpine-jdk
COPY ./target/todo-0.0.1.jar todo-0.0.1.jar
ENTRYPOINT ["java","-jar","/todo-0.0.1.jar"]