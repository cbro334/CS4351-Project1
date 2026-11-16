#!/usr/bin/env sh
set -e
COURSE_CLASSES=/classes/cs4351/cs4351_bau/pub/classes
export CLASSPATH=.:${COURSE_CLASSES}:${COURSE_CLASSES}/chap4
make
java Parse.Main "${1:-test.c}"
