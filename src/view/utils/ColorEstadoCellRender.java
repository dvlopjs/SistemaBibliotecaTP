package view.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ColorEstadoCellRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        JLabel label = new JLabel();
        label.setOpaque(true); // Necesario para que se muestren los colores de fondo

        if (value != null) {
            String estado = value.toString().toLowerCase();

            // Aplica estilos dependiendo del estado
            switch (estado) {
                case "prestado":
                    label.setText("Prestado");
                    label.setBackground(new Color(255, 200, 200)); // Rojo suave
                    label.setForeground(Color.RED);
                    break;
                case "disponible":
                    label.setText("Disponible");
                    label.setBackground(new Color(200, 255, 200)); // Verde suave
                    label.setForeground(new Color(0, 100, 0)); // Verde oscuro
                    break;
                default:
                    label.setText(estado);
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
                    break;
            }
        }

        // Estilo adicional para bordes redondeados
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), // Borde externo
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));    // Espaciado interno

        // Cambia el fondo si está seleccionada la fila
        if (isSelected) {
            label.setBackground(table.getSelectionBackground());
            label.setForeground(table.getSelectionForeground());
        }

        return label;
    }
}
