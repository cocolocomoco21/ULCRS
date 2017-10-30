# ULCRS
ULCRS, or Undergraduate Learning Center (ULC) Resource Scheduler, is a tutor scheduling desktop application intended for the ULC at UW-Madison. ULCRS is a project for the CS 506: Software Engineering course during Fall 2017.

## Building
### Electron UI
To grab dependencies and run the UI, run:
``` 
cd electron-src
npm install
npm start
```

To run the tests:
```
npm test
```

### Java Server
To run the Java Server process which acts as the backend, you must have Java 8 and Gradle installed. 

Interacting with the ULCRS backend server from the command line:

Build:
```
./gradlew build
```
This compiles the code and pulls the necessary dependencies using Gradle and Maven, so you don't have to.

Run:
```
./gradlew run
```
This runs the backend service. The service exposes endpoints on `localhost:4567/ulcrs`. For instance, to get tutor information for all tutors, hit the `localhost:4567/ulcrs/tutor` endpoint with a get request to get all tutors.


