import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class UserAppscreen {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JobApplicationScreen().createAndShowGUI());
    }
}

class JobApplicationScreen {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    public void createAndShowGUI() {
        frame = new JFrame("Applications Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(232, 218, 214));

        String[] columnNames = { "No", "Job Title", "Company Name", "Status", "Date of Application", "Withdraw",
                "Resume" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }
        };

        table = new JTable(tableModel);
        table.getColumn("Withdraw").setCellRenderer(new ButtonRenderer("Withdraw"));
        table.getColumn("Withdraw").setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, table, "Withdraw"));

        table.getColumn("Resume").setCellRenderer(new ButtonRenderer("View"));
        table.getColumn("Resume").setCellEditor(new ResumeButtonEditor(new JCheckBox(), tableModel, table));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(102, 51, 51));
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.CENTER);

        for (int i = 1; i <= 6; i++) {
            addRow(i, "Dev", "Tesla", "Applied", "20/03/2025", "Sample Resume for job " + i);
        }

        frame.setVisible(true);
    }

    private void addRow(int sn, String jobTitle, String company, String status, String date, String resumeText) {
        tableModel.addRow(new Object[] { sn, jobTitle, company, status, date, "Withdraw", resumeText });
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    private String label;

    public ButtonRenderer(String label) {
        this.label = label;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setBackground(new Color(200, 200, 200));
        if (label.equals("Withdraw") && "Withdrawn".equals(table.getValueAt(row, 3))) {
            button.setEnabled(false);
        }
        return button;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private JTable table;
    private DefaultTableModel tableModel;
    private int row;
    private String label;

    public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, JTable table, String label) {
        super(checkBox);
        this.tableModel = tableModel;
        this.table = table;
        this.label = label;
        button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setBackground(new Color(200, 200, 200));

        button.addActionListener(e -> {
            if (row != -1) {
                if (label.equals("Withdraw")) {
                    if (JOptionPane.showConfirmDialog(button, "Are you sure you want to withdraw this application?",
                            "Confirm Withdrawal", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        tableModel.setValueAt("Withdrawn", row, 3);
                    }
                }
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        button.setEnabled(!"Withdrawn".equals(table.getValueAt(row, 3)));
        return button;
    }
}

class ResumeButtonEditor extends DefaultCellEditor {
    private JButton button;
    private JTable table;
    private int row;

    public ResumeButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, JTable table) {
        super(checkBox);
        this.table = table;
        button = new JButton("View");
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setBackground(new Color(173, 216, 230));

        button.addActionListener(e -> {
            if (row != -1) {
                String resumeText = (String) table.getValueAt(row, 6);
                JTextArea textArea = new JTextArea(resumeText, 10, 30);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                int option = JOptionPane.showOptionDialog(button, scrollPane, "Resume", JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Download", "Close" }, "Close");
                if (option == JOptionPane.YES_OPTION) {
                    saveResumeToFile(resumeText);
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        return button;
    }

    private void saveResumeToFile(String resumeText) {
        try (FileWriter writer = new FileWriter("resume.txt")) {
            writer.write(resumeText);
            JOptionPane.showMessageDialog(button, "Resume downloaded as resume.txt", "Download Complete",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(button, "Error saving file: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
