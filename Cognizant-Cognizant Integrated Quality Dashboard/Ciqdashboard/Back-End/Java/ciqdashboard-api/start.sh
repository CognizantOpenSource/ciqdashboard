#!/bin/bash
./gradlew bootRun & echo $! > ./pid.file &
