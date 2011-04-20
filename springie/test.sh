#!/bin/sh
java -cp dependencies/junit.jar:. org.junit.runner.JUnitCore test.tensegrity.Functional
