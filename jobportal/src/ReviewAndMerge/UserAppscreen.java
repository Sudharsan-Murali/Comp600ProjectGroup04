import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

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
        frame.setSize(1000, 500);
        frame.setLayout(new BorderLayout());

        JLabel screenLabel = new JLabel("User Application Management", JLabel.CENTER);
        screenLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(screenLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(232, 218, 214));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = { "No", "Job Title", "Company Name", "Status", "Date of Application", "Update Date",
                "Withdraw", "Resume", "View Job Post" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7 || column == 8;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        table.getColumn("Withdraw").setCellRenderer(new ButtonRenderer("Withdraw"));
        table.getColumn("Withdraw")
                .setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, table, "Withdraw", frame));

        table.getColumn("Resume").setCellRenderer(new ButtonRenderer("View"));
        table.getColumn("Resume").setCellEditor(new ResumeButtonEditor(new JCheckBox(), tableModel, table, frame));

        table.getColumn("View Job Post").setCellRenderer(new ButtonRenderer("View"));
        table.getColumn("View Job Post")
                .setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, table, "View Job Post", frame));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(102, 51, 51));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(150, 500));
        sidePanel.setBackground(new Color(180, 180, 180));
        frame.add(sidePanel, BorderLayout.WEST);

        for (int i = 1; i <= 6; i++) {
            addRow(i, "Dev", "Tesla", "Applied", "20/03/2025", "25/03/2025", "Sample Resume for job " + i);
        }

        frame.setVisible(true);
    }

    private void addRow(int sn, String jobTitle, String company, String status, String date, String updateDate,
            String resumeText) {
        tableModel.addRow(
                new Object[] { sn, jobTitle, company, status, date, updateDate, "Withdraw", resumeText, "View Job" });
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    private String label;

    public ButtonRenderer(String label) {
        super(label);
        this.label = label;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        setFont(new Font("Arial", Font.BOLD, 10));
        setBackground(new Color(200, 200, 200));
        setEnabled(!(label.equals("Withdraw") && "Withdrawn".equals(table.getValueAt(row, 3))));
        return this;
    }
}

class ResumeButtonEditor extends DefaultCellEditor {
    public ResumeButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        // TODO Auto-generated constructor stub
    }

    private JButton button;
    private boolean clicked;
    private JTable table;
    private DefaultTableModel tableModel;
    private int row;

    public ResumeButtonEditorButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, JTable table, String label, JFrame frame) {
        this.tableModel = tableModel;
        this.table = table;
        button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setBackground(new Color(200, 200, 200));
        button.addActionListener(e -> {
            clicked = true;
            if (label.equals("Withdraw")) {
                tableModel.setValueAt("Withdrawn", row, 3);
                button.setEnabled(false);
            }
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        clicked = false;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}
