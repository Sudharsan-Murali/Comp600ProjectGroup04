package jobportal.src.ReviewAndMerge;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

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

        // ================== Modified Side Panel ==================
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(200, 500));
        sidePanel.setBackground(new Color(52, 58, 64));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Add buttons
        String[] buttonNames = { "Profile", "Applications", "Search Jobs", "Logout" };
        for (String name : buttonNames) {
            JButton button = new JButton(name);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(180, 40));
            button.setBackground(new Color(52, 58, 64));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setFont(new Font("Arial", Font.PLAIN, 14));

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(73, 80, 87));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(52, 58, 64));
                }
            });

            sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
            sidePanel.add(button);
        }

        // ================== Modified Title Panel ==================
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel screenTitle;

        try {
            BufferedImage originalIcon = ImageIO.read(new File("jobportal\\src\\Assets\\Profile-Pic-Icon.png"));
            Image scaledIcon = originalIcon.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            screenTitle = new JLabel("User Application Management", new ImageIcon(scaledIcon), JLabel.LEFT);
        } catch (Exception e) {
            screenTitle = new JLabel("User Application Management"); // Fallback
        }

        screenTitle.setFont(new Font("Arial", Font.BOLD, 24));
        screenTitle.setForeground(new Color(52, 58, 64));
        titlePanel.add(screenTitle);

        // Add components to frame
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(sidePanel, BorderLayout.WEST);

        // ================== Rest of Original Code ==================
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
        table.setIntercellSpacing(new Dimension(5, 5));

        table.getColumn("Withdraw").setCellRenderer(new ButtonRenderer("Withdraw"));
        table.getColumn("Withdraw")
                .setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, table, "Withdraw", frame));

        table.getColumn("Resume").setCellRenderer(new ButtonRenderer("Download"));
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

        for (int i = 1; i <= 6; i++) {
            addRow(i, "Developer", "Tech Corp " + i, "Applied", "2023-08-" + (10 + i), "2023-08-" + (15 + i),
                    "Resume content for position " + i);
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
    private JButton button;
    private JTable table;
    private DefaultTableModel tableModel;
    private int row;
    private JFrame frame;

    public ResumeButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, JTable table, JFrame frame) {
        super(checkBox);
        this.tableModel = tableModel;
        this.table = table;
        this.frame = frame;
        button = new JButton("View");
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setBackground(new Color(200, 200, 200));
        button.addActionListener(e -> {
            String resumeText = (String) tableModel.getValueAt(row, 7);

            int response = JOptionPane.showConfirmDialog(frame,
                    "Do you want to download this resume?",
                    "Download Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                try {
                    String jobTitle = (String) tableModel.getValueAt(row, 1);
                    String company = (String) tableModel.getValueAt(row, 2);
                    String fileName = "Resume_" + sanitizeFilename(jobTitle) + "_" +
                            sanitizeFilename(company) + ".txt";

                    File downloadsDir = new File(System.getProperty("user.home"), "Downloads");
                    if (!downloadsDir.exists())
                        downloadsDir.mkdirs();

                    File file = new File(downloadsDir, fileName);

                    try (OutputStreamWriter writer = new OutputStreamWriter(
                            new FileOutputStream(file), StandardCharsets.UTF_8)) {
                        writer.write(resumeText);
                    }

                    JOptionPane.showMessageDialog(frame,
                            "Resume downloaded to:\n" + file.getAbsolutePath(),
                            "Download Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Error saving resume: " + ex.getMessage(),
                            "Download Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            fireEditingStopped();
        });
    }

    private String sanitizeFilename(String input) {
        return input.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.row = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "View";
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private JTable table;
    private DefaultTableModel tableModel;
    private int row;
    private JFrame frame;

    public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, JTable table, String label, JFrame frame) {
        super(checkBox);
        this.tableModel = tableModel;
        this.table = table;
        this.frame = frame;

        button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setBackground(new Color(200, 200, 200));
        button.addActionListener(e -> {
            if (label.equals("Withdraw")) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to withdraw this application?",
                        "Confirm Withdrawal",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.setValueAt("Withdrawn", row, 3);
                    JOptionPane.showMessageDialog(frame,
                            "Application withdrawn successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (label.equals("View Job Post")) {
                JOptionPane.showMessageDialog(frame,
                        "Displaying full job post details...",
                        "Job Post",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.row = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}