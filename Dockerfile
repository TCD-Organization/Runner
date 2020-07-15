FROM maven:3.6.3-jdk-11

WORKDIR /runner

COPY . .

RUN mvn package

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/runner/target/core-1.0.jar"]

