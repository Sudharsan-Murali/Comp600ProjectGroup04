package com.group04.GUI.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JobSearchScreen {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JobSearchScreen::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Job Search Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // === Split Panel ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Left Panel: Job Results List
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(300, 600)); // Set width of the left panel

        // Placeholder for search results (can be updated dynamically based on user input)
        JLabel resultPlaceholder = new JLabel("Job results will appear here...");
        leftPanel.add(resultPlaceholder);

        // Right Panel: Detailed Job View
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        JTextArea jobDetailsArea = new JTextArea("Select a job to view details");
        jobDetailsArea.setEditable(false);
        jobDetailsArea.setWrapStyleWord(true);
        jobDetailsArea.setLineWrap(true);
        jobDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane jobDetailsScroll = new JScrollPane(jobDetailsArea);
        rightPanel.add(jobDetailsScroll, BorderLayout.CENTER);

        // Set minimum size for right panel, so it's visible once unhidden
        rightPanel.setPreferredSize(new Dimension(500, 600));  // Give it a fixed width
        rightPanel.setVisible(false); // Initially hide the right panel

        // === Search Field and Button ===
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));  // Adjust the layout with spacing
        JLabel searchLabel = new JLabel("Search Jobs:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(clearButton);

        // === Action for Search Button ===
        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText().trim();

            // Clear previous results in the left panel
            leftPanel.removeAll();

            if (searchQuery.isEmpty()) {
                leftPanel.add(new JLabel("Please enter a search query."));
            } else {
                // Placeholder for job search results
                leftPanel.add(new JLabel("Searching for jobs related to: " + searchQuery));

                // Example of results (In a real scenario, you would query a database or API here)
                leftPanel.add(createJobCard("Job 1: " + searchQuery + " at Company A", jobDetailsArea, rightPanel, splitPane));
                leftPanel.add(createJobCard("Job 2: " + searchQuery + " at Company B", jobDetailsArea, rightPanel, splitPane));
                leftPanel.add(createJobCard("Job 3: " + searchQuery + " at Company C", jobDetailsArea, rightPanel, splitPane));
            }

            leftPanel.revalidate();
            leftPanel.repaint();
        });

        // === Action for Clear Button ===
        clearButton.addActionListener(e -> {
            searchField.setText("");  // Clear search field
            leftPanel.removeAll();  // Clear search results
            leftPanel.add(new JLabel("Job results will appear here..."));
            leftPanel.revalidate();
            leftPanel.repaint();
        });

        // Automatically focus on the search field
        searchField.requestFocusInWindow();

        // === Add Panels to Split Pane ===
        splitPane.setLeftComponent(leftPanel);  // Left panel with job search results
        splitPane.setRightComponent(rightPanel);  // Right panel with job details
        splitPane.setDividerLocation(300);  // Set initial divider position

        // === Add Panels to Frame ===
        frame.add(topPanel, BorderLayout.NORTH);  // Add search field and button at the top
        frame.add(splitPane, BorderLayout.CENTER);  // Add split panel in the center

        frame.setVisible(true);
    }

    // Method to create a card-like panel for job results
    private static JPanel createJobCard(String jobDescription, JTextArea jobDetailsArea, JPanel rightPanel, JSplitPane splitPane) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(280, 80)); // Set the size of the card
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Add a border around the card
        cardPanel.setBackground(Color.WHITE);

        JLabel jobLabel = new JLabel(jobDescription);
        jobLabel.setFont(new Font("Arial", Font.BOLD, 14));  // Set the font for the job description
        jobLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Adding padding inside the card
        JPanel paddingPanel = new JPanel();
        paddingPanel.setPreferredSize(new Dimension(10, 10));

        cardPanel.add(paddingPanel, BorderLayout.WEST);
        cardPanel.add(jobLabel, BorderLayout.CENTER);

        // Action listener to display job details when the card is clicked
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // When the card is clicked, display job details in the detailed view area
                jobDetailsArea.setText("Details for: " + jobDescription + "\n\nJob Description:\n\nThis is a detailed description of the job.");

                // Make the right panel visible
                rightPanel.setVisible(true);

                // Adjust the divider to ensure the right panel is fully visible
                splitPane.setDividerLocation(300); // You can change this value to control the initial width of the right panel
            }
        });

        return cardPanel;
    }
}
