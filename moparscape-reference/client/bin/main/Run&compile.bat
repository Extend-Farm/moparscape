@echo off
COLOR 08
::I only add this because JBuilder makes backup files =\
del *.java~*~
title HybridScape - Beta 1
javac *.java
java server
pause