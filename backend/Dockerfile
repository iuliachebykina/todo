#
# Build stage
#


FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN apt-get update &&\
    apt-get install -y -q xserver-xorg-core wget &&\
    mkdir -p ~/.postgresql && \
    wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" -O ~/.postgresql/root.crt && \
    chmod 0600 ~/.postgresql/root.crt
    
RUN apt install nmap -y
CMD [ "nmap", "127.0.0.1" ]
# RUN mvn -f /home/app/pom.xml clean package -q

#
# Package stage
#
# FROM amazoncorretto:11-alpine-jdk
# COPY --from=build /home/app/target/todo-0.0.1.jar /usr/local/lib/todo.jar
# ENTRYPOINT ["java","-jar","/usr/local/lib/todo.jar"]
