#!/bin/bash

# Builds and installs Awgen into an output folder.
# This script is intended to be run from the main project folder.
# ./shell/build.sh

rm build
mkdir build
mkdir build/lib

mvn -B clean package

cp Awgen-Client/target/Awgen-Client-*.jar build
cp Awgen-Server/target/Awgen-Server-*.jar build
cp Awgen-Lib/target/Awgen-Lib-*.jar build/lib

cp shell/output/* build
chmod +x build/run.sh