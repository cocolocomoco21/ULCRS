#!/usr/bin/env bash
./gradlew build
./gradlew shadowJar
pip install -r scheduler-src/requirements.txt
cd electron-src
npm install
