FROM java:8-jdk-alpine

COPY ./target/it-berries-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch it-berries-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","it-berries--0.0.1-SNAPSHOT.jar"]  
