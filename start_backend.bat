@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-17.0.18"
set "PATH=%JAVA_HOME%\bin;%PATH%"
start "GeoDspBackend" java -jar "%~dp0GeoDsp(1)\GeoDsp\target\dsp-0.0.1-SNAPSHOT.jar" --server.port=9966
