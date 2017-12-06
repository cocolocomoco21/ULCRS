@echo off

echo "Building shadowJar for backend server..."
call gradlew shadowJar

echo "\nStarting npm for frontend..."
cd electron-src
npm start
