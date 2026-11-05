@echo off
setlocal
set "BASE_DIR=%~dp0"
if defined JAVA_HOME (set "JAVA_BIN=%JAVA_HOME%\bin\java.exe") else (set "JAVA_BIN=java.exe")
"%JAVA_BIN%" -Dmaven.multiModuleProjectDirectory="%BASE_DIR%" -classpath "%BASE_DIR%\.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
endlocal
