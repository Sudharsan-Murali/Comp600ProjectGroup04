package com.group04.GUI.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserManagementUI extends JFrame {
    public UserManagementUI() {
        setTitle("User Management");
        setSize(600, 500);
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
        
        JLabel menuLabel = new JLabel("View/ Manage User");
        menuLabel.setForeground(Color.GRAY);
        sidebar.add(menuLabel);
        
        // Content Panel
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBackground(new Color(255, 245, 230));
        
        // User Details Panel
        JPanel userDetailsPanel = new JPanel();
        userDetailsPanel.setLayout(new BorderLayout());
        userDetailsPanel.setBackground(new Color(220, 210, 210));
        userDetailsPanel.setBounds(100, 70, 300, 250);
        
        //JLabel userDetailsLabel = new JLabel("User Details");
        JLabel userDetailsLabel = new JLabel("View/ Manage User");
        userDetailsLabel.setForeground(Color.GRAY);
        userDetailsPanel.add(userDetailsLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"", "", "", "", "", ""};
        String[][] data = new String[2][6]; // Empty table
        JTable table = new JTable(new DefaultTableModel(data, columns));
        JScrollPane tableScrollPane = new JScrollPane(table);
        userDetailsPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        contentPanel.add(userDetailsPanel);
        
        // Adding components to main panel
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserManagementUI().setVisible(true);
        });
    }
}