@echo off
cd /d "%~dp0"
if not exist build mkdir build
java -m jdk.compiler/com.sun.tools.javac.Main -encoding UTF-8 --release 17 -d build @fuentes.txt
if errorlevel 1 (
    echo No se pudo compilar. Comprueba que tengas un JDK 17 o posterior.
    pause
    exit /b 1
)
java -m jdk.jartool/sun.tools.jar.Main --create --file Laboratorio.jar --main-class com.mycompany.lab.main -C build .
if errorlevel 1 (
    echo No se pudo crear el archivo JAR.
    pause
    exit /b 1
)
echo Compilacion terminada. Abre Ejecutar.bat para iniciar el programa.
pause
