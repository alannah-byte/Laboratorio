# Laboratorio de grafos

Aplicación de escritorio hecha con Java y Swing. Permite dibujar un grafo para obtener su matriz, o escribir una matriz para generar el grafo. En ambos casos muestra la lista de adyacencia y los recorridos BFS y DFS.

## Abrir el programa

1. Extrae todo el ZIP.
2. En Windows, abre `Ejecutar.bat` dentro de la carpeta `Laboratorio`.
3. Necesitas Java 17 o una versión posterior. El archivo `Laboratorio.jar` ya está compilado.

También se puede iniciar desde una terminal ubicada en esa carpeta:

```text
java -jar Laboratorio.jar
```

## Abrir y editar en NetBeans o IntelliJ

Abre la carpeta `Laboratorio`, que contiene `pom.xml`, como proyecto Maven. Selecciona un JDK 17 o posterior. La clase principal es `com.mycompany.lab.main` y está en `src/main/java/com/mycompany/lab/main.java`.

Las ventanas están escritas directamente en Java con `BorderLayout`, `GridLayout` y `FlowLayout`. Ya no se usan los archivos `.form` del diseño anterior. Para cambiar la interfaz, edita las clases Java; no existe una vista de diseño de NetBeans para estas pantallas.

El proyecto no utiliza bibliotecas externas. Maven puede descargar sus herramientas de compilación la primera vez. Para compilar sin Maven, ejecuta `Compilar.bat` en Windows o `sh compilar.sh` en Linux/macOS. Estos scripts necesitan un JDK y actualizan el JAR de esta carpeta. Si compilas con Maven (`mvn package`), el JAR queda en `target/Laboratorio.jar`.

## Dibujar un grafo

1. En el menú, selecciona **Dibujar grafo y obtener matriz**.
2. Con la acción **Agregar nodos**, haz clic en el área blanca. Deja espacio entre los nodos y los bordes.
3. Cambia a **Conectar nodos** y selecciona los dos extremos de cada conexión. El primer nodo se resalta.
4. Para crear un lazo, selecciona el mismo nodo dos veces.
5. Para quitar una conexión, elige **Quitar conexión** y selecciona sus extremos.

La matriz y la lista se actualizan automáticamente. **Limpiar** comienza un grafo nuevo. **Volver** regresa al menú y descarta el trabajo de esta pantalla. **Cerrar**, o la X de la ventana, termina el programa.

## Ingresar una matriz

1. Selecciona **Ingresar matriz y obtener grafo**.
2. Indica entre 1 y 26 nodos y presiona **Crear matriz**.
3. Escribe `0` si no existe una conexión y `1` si existe. Los nombres de las filas y columnas indican los nodos.
4. La matriz debe ser simétrica: si A-B vale 1, B-A también debe valer 1.
5. Presiona **Generar grafo**. Un `1` en la diagonal se dibuja como un lazo.

Las celdas vacías, las letras, los valores distintos de 0 y 1, y las matrices no simétricas muestran un mensaje abajo. Al comenzar a editar una celda o crear otra matriz, se borran el dibujo y los resultados anteriores. Genera nuevamente el grafo para calcular sus recorridos.

## Recorridos

Escoge el nodo de **Inicio** y presiona **Anchura (BFS)** o **Profundidad (DFS)**. Los resultados aparecen en campos separados. Si son largos, puedes seleccionar el campo y desplazarte con las flechas; al dejar el mouse encima también aparece el texto completo.

- **BFS:** usa una cola. Visita primero los vecinos cercanos y después los siguientes niveles.
- **DFS:** usa recursión. Sigue un camino hasta donde puede y después regresa a buscar otro.
- Los vecinos se revisan en orden A, B, C... para que el resultado sea consistente.
- Normalmente solo se visitan los nodos alcanzables desde el inicio.
- **Recorrer todo el grafo** también incluye las partes desconectadas. Primero se recorre el grupo del nodo elegido y después los grupos pendientes en orden alfabético. El símbolo `|` separa esos grupos.

Cambiar el inicio, la opción de recorrer todo o las conexiones borra los recorridos anteriores para evitar resultados desactualizados.

## Cómo se divide el código

| Clase | Para qué sirve |
| --- | --- |
| `main` | Inicia el programa y establece el tamaño de letra. |
| `Principal` | Muestra el menú y cambia entre las dos pantallas. |
| `Nodo` | Guarda una letra y la posición del nodo. |
| `Grafo` | Guarda las conexiones, valida la matriz y calcula la lista, BFS y DFS. |
| `PanelGrafo` | Dibuja los nodos, las conexiones y los lazos. |
| `GrafoMatriz` | Maneja los clics para dibujar y muestra la matriz. |
| `MatrizGrafo` | Lee la tabla y genera el grafo. |
| `PanelResultados` | Comparte la lista y los controles de recorridos entre las dos pantallas. |

Los comentarios usan `//`. Se mantienen estructuras habituales de un nivel intermedio: arreglos, `ArrayList`, una cola, ciclos, condiciones y recursión. Los manejadores `e -> metodo()` indican qué método se ejecuta al usar un control.

## Ejemplo para comprobarlo

Crea cinco nodos y las conexiones A-B, A-C, B-D y C-E. La matriz es:

| Nodo | A | B | C | D | E |
| --- | --- | --- | --- | --- | --- |
| A | 0 | 1 | 1 | 0 | 0 |
| B | 1 | 0 | 0 | 1 | 0 |
| C | 1 | 0 | 0 | 0 | 1 |
| D | 0 | 1 | 0 | 0 | 0 |
| E | 0 | 0 | 1 | 0 | 0 |

Desde A, BFS debe mostrar **A B C D E** y DFS debe mostrar **A B D C E**.

La lista debe mostrar A conectado con B y C; B con A y D; C con A y E; D con B; y E con C.

Para comprobar las partes desconectadas, crea cuatro nodos con A-B y C-D. Desde A, sin marcar la casilla, ambos recorridos dan **A B**. Al marcarla, ambos dan **A B | C D**.

## Alcance

Se trabaja con grafos no dirigidos, sin pesos ni conexiones múltiples entre el mismo par de nodos. Se permiten lazos y nodos aislados. El máximo de 26 nodos mantiene los nombres entre A y Z; las tablas y los dibujos grandes tienen barras de desplazamiento. El programa no guarda grafos en disco.
