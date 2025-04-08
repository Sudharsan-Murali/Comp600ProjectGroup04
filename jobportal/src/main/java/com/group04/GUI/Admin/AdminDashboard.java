package com.group04.GUI.Admin;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard {
    private JFrame frame;

    public void createAndShowGUI() {
        frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Welcome to the Admin Dashboard", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 16));

        tabs.addTab("User Management", createUserManagementPanel());
        tabs.addTab("Recruiter Management", createRecruiterPanel());
        tabs.addTab("System Logs", createLogsPanel());

        mainPanel.add(tabs, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("User Management Section", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRecruiterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Recruiter Management Section", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("System Logs & Audit Trail", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }
}
