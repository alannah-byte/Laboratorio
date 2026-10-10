/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lab;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class PanelGrafo extends JPanel {

    private Grafo grafo;

    public PanelGrafo() {
        grafo = new Grafo();
        setBackground(Color.WHITE);
    }

   public PanelGrafo(Grafo grafo) {
    this.grafo = grafo;
    setBackground(Color.WHITE);
    setPreferredSize(new java.awt.Dimension(400, 250));
}

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
        repaint();
    }

    public Grafo getGrafo() {
        return grafo;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (grafo == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;

        int[][] matriz = grafo.getMatrizAdyacencia();

        // Primero dibujamos las conexiones
        for (int i = 0; i < grafo.getNodos().size(); i++) {

            Nodo nodo1 = grafo.getNodos().get(i);

            for (int j = i + 1; j < grafo.getNodos().size(); j++) {

                if (matriz[i][j] == 1) {

                    Nodo nodo2 = grafo.getNodos().get(j);

                    g2.drawLine(
                            nodo1.getX(),
                            nodo1.getY(),
                            nodo2.getX(),
                            nodo2.getY()
                    );
                }
            }
        }

        // Después dibujamos los nodos
        for (Nodo nodo : grafo.getNodos()) {

            int radio = 20;

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    nodo.getX() - radio,
                    nodo.getY() - radio,
                    radio * 2,
                    radio * 2
            );

            g2.setColor(Color.BLACK);

            g2.drawOval(
                    nodo.getX() - radio,
                    nodo.getY() - radio,
                    radio * 2,
                    radio * 2
            );

            g2.drawString(
                    nodo.getNombre(),
                    nodo.getX() - 4,
                    nodo.getY() + 5
            );
        }
    }
}