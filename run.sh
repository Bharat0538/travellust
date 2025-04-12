#!/bin/bash

echo "Starting TravelLust application..."
echo "Make sure you have Java 21 and Maven installed."

# Clean and package the application
mvn clean package -DskipTests

# Check if packaging was successful
if [ $? -eq 0 ]; then
    echo "Application built successfully. Starting server..."
    # Run the application
    java -jar target/travellust-0.0.1-SNAPSHOT.jar
else
    echo "Build failed. Please check the errors above."
    exit 1
fi 