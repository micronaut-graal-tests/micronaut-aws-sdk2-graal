#!/bin/bash
./gradlew nativeCompile
cp build/native/nativeCompile/aws-s3 .
