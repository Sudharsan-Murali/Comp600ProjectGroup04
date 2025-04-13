package com.group04.GUI.Admin;

import com.group04.DAO.UserDAO;
import com.group04.GUI.JobPortalApplication;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class AdminDashboard {
    private JFrame frame;

    public void createAndShowGUI() {
        frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(950, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Combined header panel with title and logout button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("Welcome to the Admin Dashboard", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(header, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.setPreferredSize(new Dimension(100, 30));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            // SwingUtilities.invokeLater(() -> new JobPortalApplication());
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 16));
        tabs.addTab("User Management", createUserManagementPanel());
        tabs.addTab("Recruiter Management", createRecruiterManagementPanel());
        tabs.addTab("System Logs", createLogsPanel());
        tabs.addTab("Stats", createStatsPanel());

        mainPanel.add(tabs, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Manage Registered Users", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(label, BorderLayout.NORTH);

        String[] columnNames = { "User ID", "Name", "Email", "Edit", "Delete" };
        List<Map<String, String>> users = UserDAO.getAllUsers();

        Object[][] data = new Object[users.size()][5];
        for (int i = 0; i < users.size(); i++) {
            Map<String, String> user = users.get(i);
            data[i][0] = user.get("user_id");
            data[i][1] = user.get("name");
            data[i][2] = user.get("email");
            data[i][3] = "Edit";
            data[i][4] = "Delete";
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };

        JTable table = new JTable(model);
        table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        table.getColumn("Edit").setCellEditor(new ButtonEditor("Edit", row -> {
            String userID = (String) model.getValueAt(row, 0);
            String currentEmail = (String) model.getValueAt(row, 2);
            String newEmail = JOptionPane.showInputDialog(frame, "Update Email for User " + userID, currentEmail);
            if (newEmail != null && !newEmail.trim().isEmpty()) {
                if (UserDAO.updateUserEmail(userID, newEmail)) {
                    model.setValueAt(newEmail, row, 2);
                    JOptionPane.showMessageDialog(frame, "Email updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update email.");
                }
            }
        }));

        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete").setCellEditor(new ButtonEditor("Delete", row -> {
            String userID = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete user " + userID + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (UserDAO.deleteUserById(userID)) {
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(frame, "User deleted.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete user.");
                }
            }
        }));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRecruiterManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Manage Recruiters", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(label, BorderLayout.NORTH);

        String[] columnNames = { "Recruiter ID", "Name", "Email", "Company", "Edit", "Delete" };
        List<Map<String, String>> recruiters = UserDAO.getAllRecruiters();

        Object[][] data = new Object[recruiters.size()][6];
        for (int i = 0; i < recruiters.size(); i++) {
            Map<String, String> recruiter = recruiters.get(i);
            data[i][0] = recruiter.get("user_id");
            data[i][1] = recruiter.get("name");
            data[i][2] = recruiter.get("email");
            data[i][3] = recruiter.get("company");
            data[i][4] = "Edit";
            data[i][5] = "Delete";
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5;
            }
        };

        JTable table = new JTable(model);
        table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        table.getColumn("Edit").setCellEditor(new ButtonEditor("Edit", row -> {
            String recruiterID = (String) model.getValueAt(row, 0);
            String currentEmail = (String) model.getValueAt(row, 2);
            String newEmail = JOptionPane.showInputDialog(frame, "Update Email for Recruiter " + recruiterID,
                    currentEmail);
            if (newEmail != null && !newEmail.trim().isEmpty()) {
                if (UserDAO.updateRecruiterEmail(recruiterID, newEmail)) {
                    model.setValueAt(newEmail, row, 2);
                    JOptionPane.showMessageDialog(frame, "Email updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update email.");
                }
            }
        }));

        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete").setCellEditor(new ButtonEditor("Delete", row -> {
            String recruiterID = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete recruiter " + recruiterID + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (UserDAO.deleteRecruiterById(recruiterID)) {
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(frame, "Recruiter deleted.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete recruiter.");
                }
            }
        }));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("System Logs & Audit Trail", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(label, BorderLayout.NORTH);

        JTextArea logsArea = new JTextArea();
        logsArea.setEditable(false);
        logsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        logsArea.setText("2025-04-13 10:45: Admin logged in\n2025-04-13 10:46: Viewed User List\n");

        JScrollPane scrollPane = new JScrollPane(logsArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private RowAction action;
        private int buttonRow;

        public ButtonEditor(String label, RowAction action) {
            super(new JCheckBox());
            this.label = label;
            this.action = action;
            button = new JButton(label);
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                action.execute(buttonRow);
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            button.setText(label);
            buttonRow = row;
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    interface RowAction {
        void execute(int row);
    }

    // For statistics tab
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Live Platform Statistics", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton refreshButton = new JButton("Refresh Now");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton.setFocusable(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(label, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] headers = {
                "Users", "Recruiters", "Jobs Applied", "Jobs Posted", "Rejected Applications", "Accepted Applications"
        };
        Object[][] data = new Object[1][headers.length];

        DefaultTableModel model = new DefaultTableModel(data, headers) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setGridColor(new Color(189, 195, 199));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(5, 5));
        table.setBackground(Color.WHITE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 35));
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        tableHeader.setBackground(new Color(52, 73, 94));
        tableHeader.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Function to update stats
        Runnable updateStats = () -> {
            Map<String, Integer> stats = UserDAO.getPlatformStats();

            model.setValueAt(stats.getOrDefault("users", 0), 0, 0);
            model.setValueAt(stats.getOrDefault("recruiters", 0), 0, 1);
            model.setValueAt(stats.getOrDefault("jobs_applied", 0), 0, 2);
            model.setValueAt(stats.getOrDefault("jobs_posted", 0), 0, 3);
            model.setValueAt(stats.getOrDefault("rejected", 0), 0, 4);
            model.setValueAt(stats.getOrDefault("accepted", 0), 0, 5);

            // model.fireTableDataChanged();

            System.out.println("Stats Fetched into Admin Dashboard:");
            stats.forEach((k, v) -> System.out.println(k + " = " + v));

        };

        // Trigger once on load
        updateStats.run();

        // Manual refresh button
        refreshButton.addActionListener(e -> updateStats.run());

        // Auto-refresh every 10 seconds
        Timer timer = new Timer(10000, e -> updateStats.run());
        timer.start();

        return panel;
    }

}