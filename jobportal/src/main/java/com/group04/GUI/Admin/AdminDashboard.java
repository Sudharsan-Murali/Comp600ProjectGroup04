package com.group04.GUI.Admin;

import javax.swing.*;
import java.awt.*;
import com.group04.GUI.JobPortalApplication;  // Importing JobPortalApplication for the login screen

public class AdminDashboard {
    private JFrame frame;

    public void createAndShowGUI() {
        frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header panel (with logout button)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Right-aligned panel for the header
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Create Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.setPreferredSize(new Dimension(100, 30));
        
        // Add ActionListener to the logout button
        logoutButton.addActionListener(e -> {
            frame.dispose(); // Close the Admin Dashboard
            // SwingUtilities.invokeLater(() -> new JobPortalApplication()); // Reopen the login screen (assuming JobPortalApplication is the login screen)
        });

        // Add the logout button to the header panel
        headerPanel.add(logoutButton);

        // Add header panel to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Add main content (Tabbed Pane)
        JLabel header = new JLabel("Welcome to the Admin Dashboard", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(header, BorderLayout.CENTER); // We can use this for any additional content, like title

        // Tabbed Pane for different sections
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 16));

        tabs.addTab("User Management", createUserManagementPanel());
        tabs.addTab("Recruiter Management", createRecruiterPanel());
        tabs.addTab("System Logs", createLogsPanel());

        mainPanel.add(tabs, BorderLayout.CENTER); // Tabbed sections

        // Set up the frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Create the User Management Panel
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("User Management Section", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    // Create the Recruiter Management Panel
    private JPanel createRecruiterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Recruiter Management Section", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    // Create the System Logs Panel
    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("System Logs & Audit Trail", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }
}
