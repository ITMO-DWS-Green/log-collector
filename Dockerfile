# Один Dockerfile на оба приложения: MODULE=app или MODULE=generator
FROM eclipse-temurin:21-jdk AS build
ARG MODULE
WORKDIR /src
COPY . .
RUN ./gradlew :${MODULE}:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre
ARG MODULE
WORKDIR /opt/app
COPY --from=build /src/${MODULE}/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
