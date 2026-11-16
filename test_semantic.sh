#!/usr/bin/env sh
set -e
COURSE_CLASSES=/classes/cs4351/cs4351_bau/pub/classes
export CLASSPATH=.:${COURSE_CLASSES}:${COURSE_CLASSES}/chap4
make
javac -g Parse/Parse.java Semant/*.java
java Semant.Main "${1:-test.c}"
