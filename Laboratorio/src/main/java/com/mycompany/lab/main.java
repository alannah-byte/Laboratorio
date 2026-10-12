package com.mycompany.lab;

import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class main {
    public static void main(String[] args) {
        // La misma letra para todos los controles, sin configurar cada uno.
        Font fuente = new Font("SansSerif", Font.PLAIN, 16);
        String[] controles = {"Button", "Label", "ComboBox", "TextField", "TextArea",
            "Table", "TableHeader", "Spinner", "CheckBox", "TitledBorder", "OptionPane"};
        for (String control : controles) {
            UIManager.put(control + ".font", fuente);
        }

        // Swing debe crear las ventanas en su propio hilo de eventos.
        SwingUtilities.invokeLater(() -> new Principal().setVisible(true));
    }
}
