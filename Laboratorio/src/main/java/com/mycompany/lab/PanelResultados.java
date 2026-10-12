package com.mycompany.lab;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// Ambas pantallas comparten este panel para no repetir el mismo código.
public class PanelResultados extends JPanel {
    private Grafo grafo;
    private final JTextArea lista = new JTextArea(5, 20);
    private final JComboBox<String> inicio = new JComboBox<>();
    private final JCheckBox completo = new JCheckBox("Recorrer todo el grafo");
    private final JButton btnBFS = new JButton("Anchura (BFS)");
    private final JButton btnDFS = new JButton("Profundidad (DFS)");
    private final JTextField resultadoBFS = new JTextField();
    private final JTextField resultadoDFS = new JTextField();

    public PanelResultados() {
        setLayout(new GridLayout(1, 2, 16, 0));
        lista.setEditable(false);
        lista.setFont(new Font("Monospaced", Font.PLAIN, 15));
        lista.setMargin(new java.awt.Insets(8, 8, 8, 8));
        JPanel vecinos = new JPanel(new BorderLayout());
        vecinos.setBorder(BorderFactory.createTitledBorder("Lista de adyacencia"));
        vecinos.add(new JScrollPane(lista));
        add(vecinos);

        JPanel recorridos = new JPanel(new BorderLayout(8, 8));
        recorridos.setBorder(BorderFactory.createTitledBorder("Recorridos"));
        JPanel opciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        opciones.add(new JLabel("Inicio:"));
        opciones.add(inicio);
        opciones.add(completo);
        completo.setToolTipText("Incluye los grupos desconectados, separados por |.");
        recorridos.add(opciones, BorderLayout.NORTH);

        JPanel salidas = new JPanel(new GridLayout(2, 1, 0, 8));
        JPanel anchura = new JPanel(new BorderLayout(8, 0));
        JPanel profundidad = new JPanel(new BorderLayout(8, 0));
        resultadoBFS.setEditable(false);
        resultadoDFS.setEditable(false);
        btnBFS.setPreferredSize(btnDFS.getPreferredSize());
        anchura.add(btnBFS, BorderLayout.WEST);
        anchura.add(resultadoBFS, BorderLayout.CENTER);
        profundidad.add(btnDFS, BorderLayout.WEST);
        profundidad.add(resultadoDFS, BorderLayout.CENTER);
        salidas.add(anchura);
        salidas.add(profundidad);
        recorridos.add(salidas, BorderLayout.CENTER);
        recorridos.add(new JLabel("El símbolo | separa grupos sin conexión."), BorderLayout.SOUTH);
        add(recorridos);

        btnBFS.addActionListener(e -> mostrarAnchura());
        btnDFS.addActionListener(e -> mostrarProfundidad());
        inicio.addActionListener(e -> limpiarRecorridos());
        completo.addActionListener(e -> limpiarRecorridos());
        setGrafo(null);
    }

    // Cada cambio del grafo actualiza la lista y borra los recorridos anteriores.
    public void setGrafo(Grafo grafo) {
        int anterior = inicio.getSelectedIndex();
        this.grafo = grafo;
        inicio.removeAllItems();
        boolean disponible = grafo != null && grafo.getCantidadNodos() > 0;
        if (disponible) {
            for (int i = 0; i < grafo.getCantidadNodos(); i++) {
                inicio.addItem(grafo.getNodo(i).getNombre());
            }
            if (anterior >= 0 && anterior < inicio.getItemCount()) {
                inicio.setSelectedIndex(anterior);
            }
            lista.setText(grafo.generarListaAdyacencia());
        } else {
            lista.setText("");
        }
        lista.setCaretPosition(0);
        inicio.setEnabled(disponible);
        completo.setEnabled(disponible);
        btnBFS.setEnabled(disponible);
        btnDFS.setEnabled(disponible);
        limpiarRecorridos();
    }

    private void limpiarRecorridos() {
        resultadoBFS.setText("");
        resultadoDFS.setText("");
        resultadoBFS.setToolTipText(null);
        resultadoDFS.setToolTipText(null);
    }

    private void mostrarAnchura() {
        if (grafo != null && inicio.getSelectedIndex() >= 0) {
            String recorrido = grafo.recorridoAnchura(inicio.getSelectedIndex(), completo.isSelected());
            resultadoBFS.setText(recorrido);
            resultadoBFS.setCaretPosition(0);
            resultadoBFS.setToolTipText(recorrido);
        }
    }

    private void mostrarProfundidad() {
        if (grafo != null && inicio.getSelectedIndex() >= 0) {
            String recorrido = grafo.recorridoProfundidad(inicio.getSelectedIndex(), completo.isSelected());
            resultadoDFS.setText(recorrido);
            resultadoDFS.setCaretPosition(0);
            resultadoDFS.setToolTipText(recorrido);
        }
    }
}
