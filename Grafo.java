/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lab;

/**
 *
 * @author Alann
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Grafo {

    private ArrayList<Nodo> nodos;
    private int[][] matrizAdyacencia;

    public Grafo() {
        nodos = new ArrayList<>();
        matrizAdyacencia = new int[0][0];
    }

    // Agregar un nodo
    public void agregarNodo(Nodo nodo) {
        nodos.add(nodo);
        ampliarMatriz();
    }

    // Amplía la matriz cuando agregamos un nodo
    private void ampliarMatriz() {

        int cantidad = nodos.size();
        int[][] nuevaMatriz = new int[cantidad][cantidad];

        for (int i = 0; i < matrizAdyacencia.length; i++) {
            for (int j = 0; j < matrizAdyacencia.length; j++) {
                nuevaMatriz[i][j] = matrizAdyacencia[i][j];
            }
        }

        matrizAdyacencia = nuevaMatriz;
    }

    // Conectar dos nodos
    public void agregarArista(int origen, int destino) {

        if (origen >= 0 && destino >= 0
                && origen < nodos.size()
                && destino < nodos.size()) {

            matrizAdyacencia[origen][destino] = 1;
            matrizAdyacencia[destino][origen] = 1;
        }
    }

    // Eliminar una conexión
    public void eliminarArista(int origen, int destino) {

        if (origen >= 0 && destino >= 0
                && origen < nodos.size()
                && destino < nodos.size()) {

            matrizAdyacencia[origen][destino] = 0;
            matrizAdyacencia[destino][origen] = 0;
        }
    }

    // Cargar una matriz escrita por el usuario
    public void establecerMatriz(int[][] matriz) {
        matrizAdyacencia = matriz;
    }

    public ArrayList<Nodo> getNodos() {
        return nodos;
    }

    public int[][] getMatrizAdyacencia() {
        return matrizAdyacencia;
    }

    // Generar lista de adyacencia
    public String generarListaAdyacencia() {

        String resultado = "";

        for (int i = 0; i < nodos.size(); i++) {

            resultado += nodos.get(i).getNombre() + " -> ";

            boolean primero = true;

            for (int j = 0; j < nodos.size(); j++) {

                if (matrizAdyacencia[i][j] == 1) {

                    if (!primero) {
                        resultado += ", ";
                    }

                    resultado += nodos.get(j).getNombre();
                    primero = false;
                }
            }

            resultado += "\n";
        }

        return resultado;
    }

    // Recorrido por anchura
    public String recorridoAnchura(int inicio) {

        boolean[] visitados = new boolean[nodos.size()];
        Queue<Integer> cola = new LinkedList<>();

        String resultado = "";

        visitados[inicio] = true;
        cola.add(inicio);

        while (!cola.isEmpty()) {

            int actual = cola.poll();

            resultado += nodos.get(actual).getNombre() + " ";

            for (int i = 0; i < nodos.size(); i++) {

                if (matrizAdyacencia[actual][i] == 1
                        && !visitados[i]) {

                    visitados[i] = true;
                    cola.add(i);
                }
            }
        }

        return resultado;
    }

    // Recorrido por profundidad
    public String recorridoProfundidad(int inicio) {

        boolean[] visitados = new boolean[nodos.size()];
        StringBuilder resultado = new StringBuilder();

        dfs(inicio, visitados, resultado);

        return resultado.toString();
    }

    private void dfs(int actual,
                     boolean[] visitados,
                     StringBuilder resultado) {

        visitados[actual] = true;

        resultado.append(
                nodos.get(actual).getNombre()
        ).append(" ");

        for (int i = 0; i < nodos.size(); i++) {

            if (matrizAdyacencia[actual][i] == 1
                    && !visitados[i]) {

                dfs(i, visitados, resultado);
            }
        }
    }
}