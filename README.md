# ULCRS
ULCRS, or Undergraduate Learning Center (ULC) Resource Scheduler, is a tutor scheduling desktop application intended for the ULC at UW-Madison. ULCRS is a project for the CS 506: Software Engineering course during Fall 2017.


## Overview
ULCRS has frontend and backend local processes that communicate using a RESTful API. ULCRS uses the Electron framework to function as a cross-platform desktop application which uses technologies like a webapp does, but on the desktop. Accordingly, ULCRS uses React for the UI hosted within Electron and Java for a separate backend server process. Electron acts as its UI/frontend and Sparkjava is used to create a backend Java server. 


## Building
### Electron UI
**Dependencies:** TODO. npm

To grab dependencies and run the UI, run:
``` 
cd electron-src
sudo npm install
npm start
```

To run the tests:
```
npm test
```

### Java Server
**Dependencies:** To run the Java Server process which acts as the backend, you must have Java 8 and Gradle installed. 

There are two ways of running the backend server:
1) Use IntelliJ to manage and run Gradle tasks (including running the server) 
2) Interact with the ULCRS backend server from the command line

Here, we discuss (2), how to interact with the ULCRS backend server from the command line:

**Build:**
```
./gradlew build
```
This compiles the code and pulls the necessary dependencies using Gradle and Maven so you don't have to.

**Run:**
```
./gradlew run
```
This runs the backend service. This is discussed in the "Running" section below.


## Running
To run ULCRS, you currently must run the Electron UI and the Java backend server independently (this will eventually all be handled for the user). Follow these instructions to run ULCRS:

### Electron UI
To grab dependencies and run the UI, from the top-level directory, run:
``` 
cd electron-src
npm install
npm start
```

### Java Server
To grab dependencies, build, and run the backend server, from the top-level directory, run:
```
./gradlew run
```
This builds and runs the backend service. The service exposes endpoints on `localhost:4567/ulcrs`. For instance, to get tutor information for all tutors, hit the `localhost:4567/ulcrs/tutor/` endpoint with a get request to get all tutors, or `localhost:4567/ulcrs/tutor/{id}` to get the tutor with the specified id.
 
### Running Together
To run the Electron UI and Java server together, you must first run the Java server. Then, run the Electron UI server. This allows for communication between the two. 

To exit, simply kill the Java server and close the Electron UI window(s).


## References
### References to Spark and other dependencies used in the backend (for use by ULCRS team members):
Spark GitHub: https://github.com/perwendel/spark
Spark documentation: http://sparkjava.com/documentation
Spark review and overview: https://zeroturnaround.com/rebellabs/sparkjava-is-an-amazing-java-web-framework-do-you-really-need-it/
Spark and Gradle: https://www.twilio.com/blog/2015/09/getting-started-with-gradle-and-the-spark-framework-3.html
