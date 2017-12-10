#!/bin/bash

echo "Building shadowJar for backend server..."
./gradlew shadowJar

echo 
echo "Starting npm for frontend..."
cd electron-src
npm start
