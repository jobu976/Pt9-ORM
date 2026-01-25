@REM @echo off
setlocal

if "%OS%"=="Windows_NT" setlocal enableextensions enabledelayedexpansion

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

if exist "%APP_HOME%\.mvn\wrapper\maven-wrapper.jar" (
    echo Usando Maven Wrapper...
    java -cp "%APP_HOME%\.mvn\wrapper\maven-wrapper.jar" "-Dmaven.home=%APP_HOME%\.mvn\apache-maven-3.8.1" "-Dclassworlds.conf=%APP_HOME%\.mvn\wrapper\m2.conf" org.apache.maven.wrapper.MavenWrapperMain %*
    goto end
)

REM Si no existe Maven Wrapper, descargar y ejecutar
if not exist "%USERPROFILE%\.m2\wrapper" mkdir "%USERPROFILE%\.m2\wrapper"

set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\apache-maven-3.8.1
set MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd

if not exist "%MAVEN_CMD%" (
    echo Descargando Maven 3.8.1...
    powershell -Command "& {
        $ProgressPreference = 'SilentlyContinue'
        $url = 'https://archive.apache.org/dist/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.zip'
        $output = '%USERPROFILE%\.m2\wrapper\maven.zip'
        Invoke-WebRequest -Uri $url -OutFile $output
        Expand-Archive -Path $output -DestinationPath '%USERPROFILE%\.m2\wrapper'
        Remove-Item $output
    }"
)

REM Ejecutar Maven
"%MAVEN_HOME%\bin\mvn.cmd" %*

:end
endlocal & exit /b %errorlevel%
