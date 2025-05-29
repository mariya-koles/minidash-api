# Start from an official JDK runtime image
FROM eclipse-temurin:21-jdk-alpine
LABEL authors="mariya-koles"

# Set the working directory in the container
WORKDIR /app

# Copy the jar from the host to the image
COPY target/*.jar app.jar


# Expose the port your app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]