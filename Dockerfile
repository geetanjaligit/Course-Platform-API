# ---------- BUILD STAGE ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ---------- RUN STAGE ----------
# Using a slim image for the run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Specifically targeting the fat jar from pom.xml (artifact-version.jar)
COPY --from=builder /app/target/course-platform-0.0.1-SNAPSHOT.jar app.jar

# Render sets the PORT environment variable. 
# Spring Boot will automatically pick it up from ${PORT} in application.yml
EXPOSE 8080

ENTRYPOINT ["java","-Dserver.address=0.0.0.0","-jar","app.jar"]
