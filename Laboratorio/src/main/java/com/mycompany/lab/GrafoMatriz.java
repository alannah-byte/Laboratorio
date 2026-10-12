package com.mycompany.lab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GrafoMatriz extends JPanel {
    private Grafo grafo = new Grafo();
    private final PanelGrafo dibujo = new PanelGrafo(grafo);
    private final PanelResultados resultados = new PanelResultados();
    private final JTable tabla = new JTable();
    private final JComboBox<String> accion = new JComboBox<>(
        new String[]{"Agregar nodos", "Conectar nodos", "Quitar conexión"});
    private final JLabel estado = new JLabel();
    private int seleccionado = -1;

    public GrafoMatriz(Principal ventana) {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));

        JLabel titulo = new JLabel("De grafo a matriz", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 28));
        JPanel cabecera = new JPanel(new BorderLayout(8, 12));
        cabecera.add(titulo, BorderLayout.NORTH);
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton limpiar = new JButton("Limpiar");
        controles.add(new JLabel("Acción:"));
        controles.add(accion);
        controles.add(limpiar);
        cabecera.add(controles, BorderLayout.CENTER);
        add(cabecera, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 16, 0));
        JScrollPane areaDibujo = new JScrollPane(dibujo);
        areaDibujo.setBorder(BorderFactory.createTitledBorder("Grafo"));
        JScrollPane areaTabla = new JScrollPane(tabla);
        areaTabla.setColumnHeaderView(tabla.getTableHeader());
        areaTabla.setBorder(BorderFactory.createTitledBorder("Matriz de adyacencia (automática)"));
        tabla.setRowHeight(30);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(JLabel.CENTER);
        tabla.setDefaultRenderer(Object.class, centrado);
        centro.add(areaDibujo);
        centro.add(areaTabla);
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

        accion.addActionListener(e -> cambiarAccion());
        limpiar.addActionListener(e -> limpiar());
        volver.addActionListener(e -> ventana.mostrarMenu());
        cerrar.addActionListener(e -> ventana.dispose());
        dibujo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    atenderClic(e.getX(), e.getY());
                }
            }
        });
        actualizarResultados();
        cambiarAccion();
    }

    private void cambiarAccion() {
        seleccionado = -1;
        dibujo.setSeleccionado(-1);
        if (accion.getSelectedIndex() == 0) {
            mostrarEstado("Haz clic en el espacio blanco para agregar nodos.", false);
        } else {
            mostrarEstado("Selecciona dos nodos. Para un lazo, selecciona el mismo dos veces.", false);
        }
    }

    private void atenderClic(int x, int y) {
        try {
            if (accion.getSelectedIndex() == 0) {
                if (!dibujo.hayEspacio(x, y)) {
                    mostrarEstado("Deja espacio entre los nodos y los bordes.", true);
                    return;
                }
                grafo.agregarNodo(x, y);
                mostrarEstado("Nodo agregado. La matriz y la lista se actualizaron.", false);
            } else {
                int indice = dibujo.buscarNodo(x, y);
                if (indice == -1) {
                    mostrarEstado("Haz clic sobre un nodo.", true);
                    return;
                }
                if (seleccionado == -1) {
                    seleccionado = indice;
                    dibujo.setSeleccionado(indice);
                    mostrarEstado("Seleccionado: " + grafo.getNodo(indice).getNombre()
                        + ". Ahora selecciona el otro nodo.", false);
                    return;
                }
                if (accion.getSelectedIndex() == 1) {
                    grafo.agregarArista(seleccionado, indice);
                    mostrarEstado("Conexión agregada.", false);
                } else {
                    grafo.eliminarArista(seleccionado, indice);
                    mostrarEstado("Conexión quitada.", false);
                }
                seleccionado = -1;
                dibujo.setSeleccionado(-1);
            }
            actualizarResultados();
        } catch (IllegalArgumentException error) {
            mostrarEstado(error.getMessage(), true);
        }
    }

    private void actualizarResultados() {
        int cantidad = grafo.getCantidadNodos();
        String[] columnas = new String[cantidad + 1];
        columnas[0] = "Nodo";
        Object[][] datos = new Object[cantidad][cantidad + 1];
        int[][] matriz = grafo.getMatrizAdyacencia();
        for (int i = 0; i < cantidad; i++) {
            columnas[i + 1] = grafo.getNodo(i).getNombre();
            datos[i][0] = grafo.getNodo(i).getNombre();
            for (int j = 0; j < cantidad; j++) {
                datos[i][j + 1] = matriz[i][j];
            }
        }
        tabla.setModel(new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        });
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(i == 0 ? 60 : 45);
        }
        resultados.setGrafo(grafo);
        dibujo.repaint();
    }

    private void limpiar() {
        grafo = new Grafo();
        dibujo.setGrafo(grafo);
        accion.setSelectedIndex(0);
        cambiarAccion();
        actualizarResultados();
    }

    private void mostrarEstado(String texto, boolean error) {
        estado.setText(texto);
        estado.setForeground(error ? new Color(160, 35, 35) : Color.DARK_GRAY);
    }
}
