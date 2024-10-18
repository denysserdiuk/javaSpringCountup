# First Stage: Build the Spring Boot application using Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests

# Second Stage: Copy the built JAR and run the application
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/spring-tutorial.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-tutorial.jar"]
