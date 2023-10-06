#
# Build stage
#
FROM maven:3.8.4-jdk-11 AS build
WORKDIR /app
COPY src ./src
COPY pom.xml .
RUN mvn clean package

#
# Package stage
#
FROM openjdk:11-jre
COPY --from=build /home/app/target/demo-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]