#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
mkdir -p build
java -m jdk.compiler/com.sun.tools.javac.Main -encoding UTF-8 --release 17 -d build @fuentes.txt
java -m jdk.jartool/sun.tools.jar.Main --create --file Laboratorio.jar --main-class com.mycompany.lab.main -C build .
echo 'Compilación terminada. Ejecuta: java -jar Laboratorio.jar'
