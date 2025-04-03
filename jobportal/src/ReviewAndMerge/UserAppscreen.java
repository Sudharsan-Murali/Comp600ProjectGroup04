import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        // Panel for table with background color
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(232, 218, 214)); // Light pink background

        String[] columnNames = {"No", "Job Title", "Company Name", "Status", "Date of Application", "Withdraw"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the Withdraw button column is editable
            }
        };

        table = new JTable(tableModel);
        table.getColumn("Withdraw").setCellRenderer(new ButtonRenderer());
        table.getColumn("Withdraw").setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, table));

        // Apply header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(102, 51, 51)); // Dark reddish-brown
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.CENTER);

        // Sample Data
        for (int i = 1; i <= 6; i++) {
            addRow(i, "Dev", "Tesla", "Applied", "20/03/2025");
        }

        frame.setVisible(true);
    }

    private void addRow(int sn, String jobTitle, String company, String status, String date) {
        tableModel.addRow(new Object[]{sn, jobTitle, company, status, date, "Withdraw"});
    }
}

// Renderer for Withdraw Button
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setText("Withdraw");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JButton button = new JButton("Withdraw");
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setBackground(new Color(200, 200, 200)); // Light grey

        if ("Withdrawn".equals(table.getValueAt(row, 3))) {
            button.setEnabled(false);
        }

        return button;
    }
}

// Editor for Withdraw Button
class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private JTable table;
    private DefaultTableModel tableModel;
    private int row;

    public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, JTable table) {
        super(checkBox);
        this.tableModel = tableModel;
        this.table = table;
        button = new JButton("Withdraw");
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setBackground(new Color(200, 200, 200)); // Light grey

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (row != -1) {
                    if (JOptionPane.showConfirmDialog(button, "Are you sure you want to withdraw this application?", "Confirm Withdrawal", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        tableModel.setValueAt("Withdrawn", row, 3);
                        fireEditingStopped();
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        button.setEnabled(!"Withdrawn".equals(table.getValueAt(row, 3))); // Disable if already withdrawn
        return button;
    }
}