FROM maven:3.8-amazoncorretto-17 AS build
COPY . .
RUN mvn clean package

FROM amazoncorretto:17-alpine-jdk
RUN apk update
COPY --from=build ./target/parser-SNAPSHOT.jar ./parser-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","./parser-SNAPSHOT.jar"]