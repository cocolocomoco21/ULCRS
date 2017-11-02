# ULCRS
ULCRS, or Undergraduate Learning Center (ULC) Resource Scheduler, is a tutor scheduling desktop application intended for the ULC at UW-Madison. ULCRS is a project for the CS 506: Software Engineering course during the Fall 2017 semester.

The intent of ULCRS is to schedule 20-40 tutors to 50+ courses for the ULC's Drop-in Tutoring service. Here, tutors are scheduled to courses, rather than a typical tutor-to-student scheduling. Students needing help can then "drop in" for help with a course during that course's scheduled time. ULCRS aims to addresses this scheduling problem for the ULC.

About the name: Yes, it is pronounced "ulcers." Our client at the ULC has always wanted to have some software called "ULCers," which would incorporate their name of ULC in a comical way. We said "heck, we can do that," and ULCRS was born.


## Overview
ULCRS  mimics a webapp, but localized on a desktop. ULCRS has frontend and backend local processes that communicate using a RESTful API over localhost. ULCRS uses the [Electron](https://electron.atom.io/) framework to function as a cross-platform desktop application which uses technologies as a webapp does, but on the desktop. Accordingly, ULCRS uses React for the UI hosted within Electron. For the backend, ULCRS (i.e. Electron) spins off a separate Java process to act as a backend server. [Spark](http://sparkjava.com/) is used to handle the REST API and spin up the actual Jetty server for the backend. So, Electron acts as the UI/frontend and Spark is used to create a backend Java server. 


## Building
### Electron UI
**Dependencies:** Install npm. Download link: https://nodejs.org/en/

To grab dependencies and build the UI, from the top-level directory, run:
``` 
cd electron-src
npm install
```


### Java Server
**Dependencies:** You must have Java 8 and Gradle installed.

There are two ways of interacting with the backend server:
1) Use IntelliJ to manage and run Gradle tasks (including building, running, and testing the server)
2) Interact with the ULCRS backend server from the command line

Option 1 should be straightforward using Gradle, so we discuss Option 2, how to interact with the ULCRS backend server from the command line:

**Build:**
```
./gradlew build
```
This compiles the code and pulls the necessary dependencies using Gradle and Maven so you don't have to.


## Running
To run ULCRS, you currently must run the Electron UI and the Java backend server **independently** (this will eventually all be managed by Electron so the user does not need to). Follow these instructions to run ULCRS:

### Running Together
Running each process is discussed below. However, to run ULCRS, you must first run the Java server. Once the server is running, then run the Electron UI. This allows for communication between the two.

To exit, simply kill the Java server and close the Electron UI window(s).

### Java Server
To grab dependencies, build, and run the backend server, from the top-level directory, run:
```
./gradlew run
```
This builds and runs the backend server.

The backend server exposes the `localhost:4567/ulcrs` resource. For instance, to get tutor information for all tutors, hit the `localhost:4567/ulcrs/tutor/` endpoint with a get request to get all tutors, or `localhost:4567/ulcrs/tutor/{id}` to get the tutor with the specified id.

### Electron UI
To run the UI, from the electron-src/ under the top-level directory, run:
```
npm start
```
This will pull in dependencies and launch the Electron application.


