#!/bin/sh
javac test/Functional.java -cp dependencies/junit.jar:. -d classes
java -cp .:dependencies/junit.jar:classes org.junit.runner.JUnitCore test.Functional
