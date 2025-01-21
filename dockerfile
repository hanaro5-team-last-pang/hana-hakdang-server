FROM openjdk:17.0.2-slim as base
FROM base as builder
WORKDIR /server

COPY . .

RUN ./gradlew bootJar


FROM base as runner
WORKDIR /server

COPY --from=builder /server/build/libs/hana-hakdang-server-0.0.1-SNAPSHOT.jar server.jar
COPY .env.prod .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "server.jar"]