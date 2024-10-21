# Use an official OpenJDK runtime as the base image
FROM openjdk:8-alpine

# Create a directory inside the container for your app
WORKDIR /usr/local/app/jdbc

# Copy the jar file from your target directory to the container
COPY target/learning-jdbc-1.0.0-SNAPSHOT.jar /usr/local/app/jdbc/stockquote.jar

# Set the entry point for running the app
ENTRYPOINT ["java", "-jar", "/usr/local/app/jdbc/stockquote.jar"]
