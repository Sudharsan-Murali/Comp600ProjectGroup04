package com.group04.GUI.Admin;

import javax.swing.*;
import java.awt.*;

public class ManageJobPostsUI extends JFrame {
    public ManageJobPostsUI() {
        setTitle("Manage Job Posts");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 245, 230));
        
        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(120, getHeight()));
        sidebar.setBackground(new Color(200, 180, 180));
        sidebar.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel menuLabel = new JLabel("Manage Job Posts");
        menuLabel.setForeground(Color.GRAY);
        sidebar.add(menuLabel);
        
        // Content Panel
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBackground(new Color(255, 245, 230));
        
        // Job Posts Panel
        JPanel jobPostsPanel = new JPanel();
        jobPostsPanel.setLayout(new BorderLayout());
        jobPostsPanel.setBackground(new Color(220, 210, 210));
        jobPostsPanel.setBounds(100, 70, 300, 180);
        
        contentPanel.add(jobPostsPanel);
        
        // Adding components to main panel
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManageJobPostsUI().setVisible(true);
        });
    }
}
