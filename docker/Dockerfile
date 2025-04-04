FROM maven:3.9.4-eclipse-temurin-17 AS exception-handler-starter-build
WORKDIR /app
COPY /exception-handler-starter/pom.xml pom.xml
COPY /exception-handler-starter/src src
RUN mvn clean install -DskipTests

FROM maven:3.9.4-eclipse-temurin-17 AS logging-starter-build
WORKDIR /app
COPY /logging-starter/pom.xml pom.xml
COPY /logging-starter/src src
RUN mvn clean install -DskipTests

FROM maven:3.9.4-eclipse-temurin-17 AS build

ARG SERVICE_NAME

COPY --from=exception-handler-starter-build /root/.m2 /root/.m2
COPY --from=logging-starter-build /root/.m2 /root/.m2
COPY ${SERVICE_NAME}/pom.xml pom.xml
COPY ${SERVICE_NAME}/src src

RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre AS builder

WORKDIR /app

COPY --from=build target/*.jar ./application.jar
RUN java -Djarmode=layertools -jar ./application.jar extract

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]