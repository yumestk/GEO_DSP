@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-17.0.18"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
call mvnw compile
pause
