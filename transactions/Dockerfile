# Use a base image with OpenJDK
FROM openjdk:21

# Set the working directory in the container
WORKDIR /app

# Copy  Spring Boot application's JAR file into the container
COPY target/transactions-0.0.1-SNAPSHOT.jar /app/transaction-app.jar


#ENV PORT 8081
# Expose the port that your Spring Boot app will run on

EXPOSE 8082

# Run the application
CMD ["java", "-jar", "/app/transaction-app.jar"]
