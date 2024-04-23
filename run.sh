#!/bin/bash -ex

javac TCPDumpParser.java

java TCPDumpParser sampletcpdump.txt

cat output.txt