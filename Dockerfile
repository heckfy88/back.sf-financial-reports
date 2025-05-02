FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy your already-built JAR into the image
COPY target/financial-reports-0.0.1-SNAPSHOT.jar app.jar

# Expose port (optional)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]