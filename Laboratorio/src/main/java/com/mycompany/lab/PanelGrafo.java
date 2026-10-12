package com.mycompany.lab;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

// Este panel se ocupa del dibujo, no de los recorridos.
public class PanelGrafo extends JPanel {
    private static final int RADIO = 20;
    private Grafo grafo;
    private int seleccionado = -1;

    public PanelGrafo(Grafo grafo) {
        this.grafo = grafo;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(460, 340));
    }

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
        seleccionado = -1;
        repaint();
    }

    public void setSeleccionado(int indice) {
        seleccionado = indice;
        repaint();
    }

    public int buscarNodo(int x, int y) {
        if (grafo != null) {
            for (int i = 0; i < grafo.getCantidadNodos(); i++) {
                Nodo nodo = grafo.getNodo(i);
                int dx = x - nodo.getX();
                int dy = y - nodo.getY();
                if (dx * dx + dy * dy <= RADIO * RADIO) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean hayEspacio(int x, int y) {
        if (x < 30 || y < 55 || x > getWidth() - 30 || y > getHeight() - 30) {
            return false;
        }
        for (int i = 0; i < grafo.getCantidadNodos(); i++) {
            Nodo nodo = grafo.getNodo(i);
            int dx = x - nodo.getX();
            int dy = y - nodo.getY();
            if (dx * dx + dy * dy < 65 * 65) {
                return false;
            }
        }
        return true;
    }

    // Fijamos el tamaño antes de calcular posiciones. Los grafos grandes tienen scroll.
    public void ordenarEnCirculo() {
        int cantidad = grafo.getCantidadNodos();
        int lado = Math.max(280, cantidad * 26);
        int ancho = Math.max(420, lado);
        setPreferredSize(new Dimension(ancho, lado));
        int centroX = ancho / 2;
        int centroY = lado / 2;
        int radio = lado / 2 - 55;

        for (int i = 0; i < cantidad; i++) {
            double angulo = -Math.PI / 2 + 2 * Math.PI * i / cantidad;
            int x = centroX + (int) (radio * Math.cos(angulo));
            int y = centroY + (int) (radio * Math.sin(angulo));
            if (cantidad == 1) {
                x = centroX;
                y = centroY;
            }
            grafo.getNodo(i).setPosicion(x, y);
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.DARK_GRAY);
        if (grafo == null || grafo.getCantidadNodos() == 0) {
            g.drawString("Aquí se mostrará el grafo.", 25, 35);
            return;
        }

        int[][] matriz = grafo.getMatrizAdyacencia();
        for (int i = 0; i < grafo.getCantidadNodos(); i++) {
            Nodo origen = grafo.getNodo(i);
            // Un 1 en la diagonal es una conexión del nodo consigo mismo.
            if (matriz[i][i] == 1) {
                g.drawOval(origen.getX() - 12, origen.getY() - 48, 24, 34);
            }
            for (int j = i + 1; j < grafo.getCantidadNodos(); j++) {
                if (matriz[i][j] == 1) {
                    Nodo destino = grafo.getNodo(j);
                    g.drawLine(origen.getX(), origen.getY(), destino.getX(), destino.getY());
                }
            }
        }

        for (int i = 0; i < grafo.getCantidadNodos(); i++) {
            Nodo nodo = grafo.getNodo(i);
            g.setColor(i == seleccionado ? new Color(208, 230, 250) : Color.WHITE);
            g.fillOval(nodo.getX() - RADIO, nodo.getY() - RADIO, RADIO * 2, RADIO * 2);
            g.setColor(Color.DARK_GRAY);
            g.drawOval(nodo.getX() - RADIO, nodo.getY() - RADIO, RADIO * 2, RADIO * 2);
            int ancho = g.getFontMetrics().stringWidth(nodo.getNombre());
            g.drawString(nodo.getNombre(), nodo.getX() - ancho / 2, nodo.getY() + 6);
        }
    }
}
