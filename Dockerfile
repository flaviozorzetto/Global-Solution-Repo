FROM eclipse-temurin:latest as build

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' mvnw

RUN /bin/sh mvnw install

EXPOSE 8080

CMD ["java", "-jar", "./target/gsproject-0.0.1-SNAPSHOT.jar"]