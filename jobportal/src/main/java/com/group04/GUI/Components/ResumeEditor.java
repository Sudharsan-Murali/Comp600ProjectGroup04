package com.group04.GUI.Components;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ResumeEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private final JButton button;
    private final JTable table;

    public ResumeEditor(JTable table) {
        this.table = table;
        button = new JButton("View Resume");
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                   boolean isSelected, int row, int column) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "View Resume";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.getEditingRow();
        if (row < 0) {
            fireEditingStopped();
            return;
        }
        // Retrieve resume bytes from the RESUME column (index 5)
        Object resumeObj = table.getValueAt(row, 5);
        if (resumeObj == null) {
            JOptionPane.showMessageDialog(table, "No resume available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
            return;
        }
        byte[] resumeBytes = (byte[]) resumeObj;
        if (resumeBytes.length == 0) {
            JOptionPane.showMessageDialog(table, "No resume found.", "Info", JOptionPane.WARNING_MESSAGE);
            fireEditingStopped();
            return;
        }
        try {
            // Write resume bytes to a temporary file (e.g., PDF file)
            File tempFile = File.createTempFile("resume_", ".pdf");
            tempFile.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(resumeBytes);
            }
            Desktop.getDesktop().open(tempFile);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(table, "Error opening resume: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        fireEditingStopped();
    }
}

