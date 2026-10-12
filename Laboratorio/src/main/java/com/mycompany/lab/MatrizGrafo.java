package com.mycompany.lab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MatrizGrafo extends JPanel {
    private Grafo grafo;
    private final PanelGrafo dibujo = new PanelGrafo(null);
    private final PanelResultados resultados = new PanelResultados();
    private final JTable tabla = new JTable();
    private final JSpinner cantidad = new JSpinner(new SpinnerNumberModel(5, 1, Grafo.MAX_NODOS, 1));
    private final JButton btnGenerar = new JButton("Generar grafo");
    private final JLabel estado = new JLabel("Indica la cantidad de nodos y presiona Crear matriz.");

    public MatrizGrafo(Principal ventana) {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));

        JLabel titulo = new JLabel("De matriz a grafo", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 28));
        JPanel cabecera = new JPanel(new BorderLayout(8, 12));
        cabecera.add(titulo, BorderLayout.NORTH);
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton crear = new JButton("Crear matriz");
        JButton limpiar = new JButton("Limpiar");
        controles.add(new JLabel("Cantidad de nodos:"));
        controles.add(cantidad);
        controles.add(crear);
        controles.add(btnGenerar);
        controles.add(limpiar);
        cabecera.add(controles, BorderLayout.CENTER);
        cabecera.add(new JLabel("Escribe 0 o 1. A-B y B-A deben coincidir. Un 1 en la diagonal crea un lazo."),
            BorderLayout.SOUTH);
        add(cabecera, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 16, 0));
        JScrollPane areaTabla = new JScrollPane(tabla);
        areaTabla.setColumnHeaderView(tabla.getTableHeader());
        areaTabla.setBorder(BorderFactory.createTitledBorder("Matriz de adyacencia"));
        JScrollPane areaDibujo = new JScrollPane(dibujo);
        areaDibujo.setBorder(BorderFactory.createTitledBorder("Grafo"));
        tabla.setRowHeight(30);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tabla.addPropertyChangeListener("tableCellEditor", e -> {
            if (tabla.isEditing()) {
                invalidarGrafo();
            }
        });
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(JLabel.CENTER);
        tabla.setDefaultRenderer(Object.class, centrado);
        centro.add(areaTabla);
        centro.add(areaDibujo);
        add(centro, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(8, 10));
        inferior.add(resultados, BorderLayout.CENTER);
        JPanel pie = new JPanel(new BorderLayout(8, 0));
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton volver = new JButton("Volver");
        JButton cerrar = new JButton("Cerrar");
        botones.add(volver);
        botones.add(cerrar);
        pie.add(estado, BorderLayout.CENTER);
        pie.add(botones, BorderLayout.EAST);
        inferior.add(pie, BorderLayout.SOUTH);
        add(inferior, BorderLayout.SOUTH);

        crear.addActionListener(e -> crearMatriz());
        btnGenerar.addActionListener(e -> generarGrafo());
        limpiar.addActionListener(e -> limpiar());
        volver.addActionListener(e -> ventana.mostrarMenu());
        cerrar.addActionListener(e -> ventana.dispose());
        btnGenerar.setEnabled(false);
    }

    private void crearMatriz() {
        try {
            cantidad.commitEdit();
            int total = (Integer) cantidad.getValue();
            if (tabla.isEditing()) {
                tabla.getCellEditor().cancelCellEditing();
            }
            String[] columnas = new String[total + 1];
            columnas[0] = "Nodo";
            Object[][] datos = new Object[total][total + 1];
            for (int i = 0; i < total; i++) {
                columnas[i + 1] = String.valueOf((char) ('A' + i));
                datos[i][0] = columnas[i + 1];
                for (int j = 1; j <= total; j++) {
                    datos[i][j] = 0;
                }
            }
            DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return columna > 0;
                }
            };
            tabla.setModel(modelo);
            for (int i = 0; i < tabla.getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setPreferredWidth(i == 0 ? 60 : 45);
            }
            // Si se edita una celda, el dibujo anterior deja de ser válido.
            modelo.addTableModelListener(e -> invalidarGrafo());
            btnGenerar.setEnabled(true);
            invalidarGrafo();
            mostrarEstado("Matriz creada. Completa las conexiones y presiona Generar grafo.", false);
        } catch (ParseException error) {
            mostrarEstado("La cantidad debe ser un número entero entre 1 y 26.", true);
        }
    }

    private void generarGrafo() {
        if (tabla.isEditing() && !tabla.getCellEditor().stopCellEditing()) {
            return;
        }
        int total = tabla.getRowCount();
        if (total == 0) {
            mostrarEstado("Primero crea la matriz.", true);
            return;
        }

        try {
            int[][] matriz = new int[total][total];
            for (int i = 0; i < total; i++) {
                for (int j = 0; j < total; j++) {
                    Object valor = tabla.getValueAt(i, j + 1);
                    String texto = valor == null ? "" : valor.toString().trim();
                    if (!texto.equals("0") && !texto.equals("1")) {
                        throw new IllegalArgumentException("En la celda " + (char) ('A' + i)
                            + "-" + (char) ('A' + j) + " escribe 0 o 1.");
                    }
                    matriz[i][j] = Integer.parseInt(texto);
                }
            }
            Grafo nuevo = new Grafo();
            nuevo.establecerMatriz(matriz);
            grafo = nuevo;
            dibujo.setGrafo(grafo);
            dibujo.ordenarEnCirculo();
            resultados.setGrafo(grafo);
            mostrarEstado("Grafo generado. Ya puedes consultar ambos recorridos.", false);
        } catch (IllegalArgumentException error) {
            invalidarGrafo();
            mostrarEstado(error.getMessage(), true);
        }
    }

    private void invalidarGrafo() {
        grafo = null;
        dibujo.setGrafo(null);
        resultados.setGrafo(null);
        mostrarEstado("Matriz pendiente de generar. Presiona Generar grafo.", false);
    }

    private void limpiar() {
        if (tabla.isEditing()) {
            tabla.getCellEditor().cancelCellEditing();
        }
        tabla.setModel(new DefaultTableModel());
        cantidad.setValue(5);
        btnGenerar.setEnabled(false);
        invalidarGrafo();
        mostrarEstado("Indica la cantidad de nodos y presiona Crear matriz.", false);
    }

    private void mostrarEstado(String texto, boolean error) {
        estado.setText(texto);
        estado.setForeground(error ? new Color(160, 35, 35) : Color.DARK_GRAY);
    }
}
