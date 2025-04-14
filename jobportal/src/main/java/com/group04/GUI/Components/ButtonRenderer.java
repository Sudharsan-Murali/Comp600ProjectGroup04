package com.group04.GUI.Components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer(Icon icon) {
        setIcon(icon);
        setOpaque(true);
        setContentAreaFilled(true); // Make it look like a button
        setBorderPainted(true); // Show border
        setFocusPainted(false);
        setHorizontalAlignment(JLabel.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Pointer cursor
    }

    public ButtonRenderer() {
        setText("View Resume");
        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(true);
        setFocusPainted(false);
        setHorizontalAlignment(JLabel.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this;
    }
}