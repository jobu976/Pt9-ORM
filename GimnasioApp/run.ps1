#!/usr/bin/env pwsh
# Script para ejecutar GimnasioApp en PowerShell
# Requiere: Java 25.0.1+ y Maven 3.8.1+

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   GIMNASIO APP - Sistema de Gestion" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Obtener directorio del script
$scriptDir = Split-Path -Parent $MyInvocation.MyCommandPath
Set-Location $scriptDir

# Verificar Maven
try {
    mvn -v | Out-Null
} catch {
    Write-Host "ERROR: Maven no está instalado o no está en PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "Instrucciones de instalación:" -ForegroundColor Yellow
    Write-Host "1. Descarga Maven desde: https://maven.apache.org/download.cgi"
    Write-Host "2. Extrae el archivo ZIP"
    Write-Host "3. Añade la carpeta bin al PATH del sistema"
    Write-Host ""
    Read-Host "Presiona Enter para salir"
    exit 1
}

# Verificar Java
try {
    java -version 2>&1 | Out-Null
} catch {
    Write-Host "ERROR: Java no está instalado o no está en PATH" -ForegroundColor Red
    Write-Host ""
    Write-Host "Requiere Java 25.0.1 LTS o superior" -ForegroundColor Yellow
    Write-Host "Descarga desde: https://www.oracle.com/java/technologies/downloads/"
    Write-Host ""
    Read-Host "Presiona Enter para salir"
    exit 1
}

Write-Host "Compilando proyecto..." -ForegroundColor Yellow
Write-Host ""

# Usar ruta completa de Maven por si no está en PATH
$mavenBin = "C:\Users\jbuna\AppData\Local\maven\apache-maven-3.8.1\bin\mvn.cmd"
& $mavenBin clean compile dependency:copy-dependencies -DskipTests -q
if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "ERROR: Compilación fallida" -ForegroundColor Red
    Write-Host ""
    Read-Host "Presiona Enter para salir"
    exit 1
}

Write-Host "✓ Compilación exitosa" -ForegroundColor Green
Write-Host ""
Write-Host "Iniciando aplicación..." -ForegroundColor Yellow
Write-Host ""

# Ejecutar la aplicación
java -cp "target\classes;target\dependency\*" com.gimnasio.app.GimnasioApp

Write-Host ""
Write-Host "Aplicación finalizada." -ForegroundColor Gray
Write-Host ""
Read-Host "Presiona Enter para salir"
