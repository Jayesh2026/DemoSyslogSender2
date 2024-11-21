FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/sysSender-0.0.1-SNAPSHOT.jar syslog-sender.jar

# Download the OpenTelemetry Java agent
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.2.0/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar

EXPOSE 8081

ENTRYPOINT ["java", "-javaagent:/app/opentelemetry-javaagent.jar", "-jar", "syslog-sender.jar"]
# ENTRYPOINT ["java", "-jar", "syslog-sender.jar"]