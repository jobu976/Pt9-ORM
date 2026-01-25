@echo off
REM Script para ejecutar GimnasioApp
REM Requiere: Java 25.0.1+ y Maven 3.8.1+

setlocal enabledelayedexpansion

cd /d "%~dp0"

echo.
echo ========================================
echo   GIMNASIO APP - Sistema de Gestion
echo ========================================
echo.

REM Verificar si Maven está instalado
mvn -v >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven no está instalado o no está en PATH
    echo.
    echo Instrucciones de instalación:
    echo 1. Descarga Maven desde: https://maven.apache.org/download.cgi
    echo 2. Extrae el archivo ZIP
    echo 3. Añade la carpeta bin al PATH del sistema
    echo.
    pause
    exit /b 1
)

REM Verificar si Java está instalado
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java no está instalado o no está en PATH
    echo.
    echo Requiere Java 25.0.1 LTS o superior
    echo Descarga desde: https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

echo Compilando proyecto...
echo.
mvn clean compile dependency:copy-dependencies -DskipTests -q

if errorlevel 1 (
    echo.
    echo ERROR: Compilación fallida
    echo.
    pause
    exit /b 1
)

echo.
echo ✓ Compilación exitosa
echo.
echo Iniciando aplicación...
echo.

REM Ejecutar la aplicación
java -cp "target\classes;target\dependency\*" com.gimnasio.app.GimnasioApp

echo.
echo Aplicación finalizada.
echo.
pause
