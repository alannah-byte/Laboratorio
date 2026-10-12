@echo off
cd /d "%~dp0"
java -jar "Laboratorio.jar"
if errorlevel 1 (
    echo.
    echo Necesitas Java 17 o una version posterior para ejecutar este programa.
    echo Si modificaste el codigo, ejecuta primero Compilar.bat con un JDK instalado.
    pause
)
