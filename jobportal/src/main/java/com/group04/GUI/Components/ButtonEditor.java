package com.group04.GUI.Components;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private int row;

    public interface ButtonClickListener {
        void onClick(int row);
    }

    public ButtonEditor(String label, JTable table, ButtonClickListener listener) {
        this.label = label;
        this.table = table;

        button = new JButton(label);
        button.setOpaque(true);

        button.addActionListener(e -> {
            if (isPushed) {
                listener.onClick(row);
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
        this.row = row;
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return label;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }
}
