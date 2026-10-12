package com.mycompany.lab;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

// Grafo no dirigido: si A se conecta con B, B también se conecta con A.
public class Grafo {
    public static final int MAX_NODOS = 26;

    private final ArrayList<Nodo> nodos = new ArrayList<>();
    private int[][] matriz = new int[0][0];

    public int getCantidadNodos() {
        return nodos.size();
    }

    public Nodo getNodo(int indice) {
        return nodos.get(indice);
    }

    // Devolvemos una copia para que la tabla no cambie el grafo por accidente.
    public int[][] getMatrizAdyacencia() {
        int cantidad = nodos.size();
        int[][] copia = new int[cantidad][cantidad];
        for (int i = 0; i < cantidad; i++) {
            for (int j = 0; j < cantidad; j++) {
                copia[i][j] = matriz[i][j];
            }
        }
        return copia;
    }

    public void agregarNodo(int x, int y) {
        int cantidad = nodos.size();
        if (cantidad == MAX_NODOS) {
            throw new IllegalArgumentException("Se permiten hasta 26 nodos, de A a Z.");
        }

        int[][] nueva = new int[cantidad + 1][cantidad + 1];
        for (int i = 0; i < cantidad; i++) {
            for (int j = 0; j < cantidad; j++) {
                nueva[i][j] = matriz[i][j];
            }
        }
        String nombre = String.valueOf((char) ('A' + cantidad));
        nodos.add(new Nodo(nombre, x, y));
        matriz = nueva;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= nodos.size()) {
            throw new IllegalArgumentException("Seleccione un nodo válido.");
        }
    }

    public void agregarArista(int origen, int destino) {
        validarIndice(origen);
        validarIndice(destino);
        matriz[origen][destino] = 1;
        matriz[destino][origen] = 1;
    }

    public void eliminarArista(int origen, int destino) {
        validarIndice(origen);
        validarIndice(destino);
        matriz[origen][destino] = 0;
        matriz[destino][origen] = 0;
    }

    // Primero validamos todo. Solo después reemplazamos el grafo anterior.
    public void establecerMatriz(int[][] nueva) {
        if (nueva == null || nueva.length < 1 || nueva.length > MAX_NODOS) {
            throw new IllegalArgumentException("La matriz debe tener entre 1 y 26 nodos.");
        }
        int cantidad = nueva.length;
        for (int i = 0; i < cantidad; i++) {
            if (nueva[i] == null || nueva[i].length != cantidad) {
                throw new IllegalArgumentException("La matriz debe ser cuadrada.");
            }
        }

        int[][] copia = new int[cantidad][cantidad];
        for (int i = 0; i < cantidad; i++) {
            for (int j = 0; j < cantidad; j++) {
                if (nueva[i][j] != 0 && nueva[i][j] != 1) {
                    throw new IllegalArgumentException("Solo se permiten valores 0 y 1.");
                }
                if (nueva[i][j] != nueva[j][i]) {
                    throw new IllegalArgumentException(
                        "La matriz debe ser simétrica: A-B debe coincidir con B-A.");
                }
                copia[i][j] = nueva[i][j];
            }
        }

        nodos.clear();
        for (int i = 0; i < cantidad; i++) {
            nodos.add(new Nodo(String.valueOf((char) ('A' + i)), 0, 0));
        }
        matriz = copia;
    }

    public String generarListaAdyacencia() {
        StringBuilder lista = new StringBuilder();
        for (int i = 0; i < nodos.size(); i++) {
            lista.append(nodos.get(i).getNombre()).append(" -> ");
            boolean primero = true;
            for (int j = 0; j < nodos.size(); j++) {
                if (matriz[i][j] == 1) {
                    if (!primero) {
                        lista.append(", ");
                    }
                    lista.append(nodos.get(j).getNombre());
                    primero = false;
                }
            }
            if (primero) {
                lista.append("Sin conexiones");
            }
            lista.append("\n");
        }
        return lista.toString();
    }

    public String recorridoAnchura(int inicio, boolean completo) {
        validarIndice(inicio);
        boolean[] visitados = new boolean[nodos.size()];
        String resultado = bfs(inicio, visitados);

        // Si se solicita todo el grafo, seguimos con los grupos desconectados.
        if (completo) {
            for (int i = 0; i < nodos.size(); i++) {
                if (!visitados[i]) {
                    resultado += " | " + bfs(i, visitados);
                }
            }
        }
        return resultado;
    }

    private String bfs(int inicio, boolean[] visitados) {
        Queue<Integer> cola = new LinkedList<>();
        StringBuilder resultado = new StringBuilder();
        visitados[inicio] = true;
        cola.add(inicio);

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            resultado.append(nodos.get(actual).getNombre()).append(" ");
            for (int i = 0; i < nodos.size(); i++) {
                if (matriz[actual][i] == 1 && !visitados[i]) {
                    visitados[i] = true;
                    cola.add(i);
                }
            }
        }
        return resultado.toString().trim();
    }

    public String recorridoProfundidad(int inicio, boolean completo) {
        validarIndice(inicio);
        boolean[] visitados = new boolean[nodos.size()];
        StringBuilder resultado = new StringBuilder();
        dfs(inicio, visitados, resultado);

        if (completo) {
            for (int i = 0; i < nodos.size(); i++) {
                if (!visitados[i]) {
                    resultado.append("| ");
                    dfs(i, visitados, resultado);
                }
            }
        }
        return resultado.toString().trim();
    }

    private void dfs(int actual, boolean[] visitados, StringBuilder resultado) {
        visitados[actual] = true;
        resultado.append(nodos.get(actual).getNombre()).append(" ");
        for (int i = 0; i < nodos.size(); i++) {
            if (matriz[actual][i] == 1 && !visitados[i]) {
                dfs(i, visitados, resultado);
            }
        }
    }
}
