#!/usr/bin/env sh
clear
export CLASSPATH=$(pwd)/lib/classes
export CLASSPATH=$CLASSPATH:$(pwd)
make clean
make
java Parse.Main test.c
