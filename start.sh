#!/bin/bash

echo "Building shadowJar for backend server..."
./gradlew shadowJar

echo "\nStarting npm for frontend..."
cd electron-src
npm start
