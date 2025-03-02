FROM openjdk:22-slim

WORKDIR /app

RUN apt-get update && apt-get install -y curl
COPY target/crm-0.0.1-SNAPSHOT.jar /app/crm-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "crm-0.0.1-SNAPSHOT.jar"]
