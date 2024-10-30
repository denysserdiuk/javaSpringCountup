# First Stage: Build the application
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /financeApp
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Second Stage: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /financeApp

# Copy the built JAR file
COPY --from=build /financeApp/target/*.jar financeApp.jar

# Expose the port Cloud Run expects
EXPOSE 8080

# Use a non-root user for security (optional but recommended)
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

# Run the application
ENTRYPOINT ["java", "-jar", "target/financeApp.jar"]