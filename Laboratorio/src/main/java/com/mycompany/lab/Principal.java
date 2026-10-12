package com.mycompany.lab;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Usamos una sola ventana. Solo cambia el panel que se muestra dentro.
public class Principal extends JFrame {
    public Principal() {
        super("Laboratorio de grafos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mostrarMenu();
    }

    public void mostrarMenu() {
        JPanel menu = new JPanel(new BorderLayout(20, 30));
        menu.setBorder(BorderFactory.createEmptyBorder(35, 45, 35, 45));
        JLabel titulo = new JLabel("Laboratorio de grafos", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 30));
        menu.add(titulo, BorderLayout.NORTH);

        JPanel opciones = new JPanel(new GridLayout(3, 1, 12, 16));
        JButton dibujar = new JButton("Dibujar grafo y obtener matriz");
        JButton ingresar = new JButton("Ingresar matriz y obtener grafo");
        JButton cerrar = new JButton("Cerrar");
        dibujar.addActionListener(e -> mostrarPanel(new GrafoMatriz(this)));
        ingresar.addActionListener(e -> mostrarPanel(new MatrizGrafo(this)));
        cerrar.addActionListener(e -> dispose());
        opciones.add(dibujar);
        opciones.add(ingresar);
        opciones.add(cerrar);
        menu.add(opciones, BorderLayout.CENTER);

        setContentPane(menu);
        setMinimumSize(new java.awt.Dimension(580, 390));
        setSize(650, 420);
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    private void mostrarPanel(JPanel panel) {
        setContentPane(panel);
        setMinimumSize(new java.awt.Dimension(980, 690));
        setSize(1080, 760);
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }
}
