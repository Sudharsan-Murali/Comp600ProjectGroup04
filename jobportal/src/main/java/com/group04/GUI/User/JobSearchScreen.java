package com.group04.GUI.User;

import javax.swing.*;

import com.group04.GUI.BaseScreen;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class JobSearchScreen extends BaseScreen{
    public JobSearchScreen(String title) {
        super("Job Search");
        initializeUI();
    }

    public JobSearchScreen() { // Not private or protected
        super("Job Search");
        // initialization
    }

    private void initializeUI() {
        // Add your job search components here
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JobSearchScreen::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Job Search Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // === Side Panel (Left) ===
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(33, 37, 41));
        sidePanel.setPreferredSize(new Dimension(200, 600));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        // Reordered buttons for the side panel
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

        // === Title Panel ===
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel screenTitle;

        try {
            BufferedImage originalIcon = ImageIO.read(new File("jobportal\\src\\Assets\\Profile-Pic-Icon.png"));
            Image scaledIcon = originalIcon.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            screenTitle = new JLabel("Job Search Screen", new ImageIcon(scaledIcon), JLabel.LEFT);
        } catch (Exception e) {
            screenTitle = new JLabel("Job Search Screen"); // Fallback
        }

        screenTitle.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(screenTitle);

        // === Search Panel ===
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel searchLabel = new JLabel("Search Jobs:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        // === Combine Title and Search Panels ===
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(titlePanel);
        topContainer.add(searchPanel);

        // === Job Result Panels ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(300, 600));
        JLabel resultPlaceholder = new JLabel("Job results will appear here...");
        leftPanel.add(resultPlaceholder);

        JPanel rightPanel = new JPanel(new BorderLayout());
        JTextArea jobDetailsArea = new JTextArea("Select a job to view details");
        jobDetailsArea.setEditable(false);
        jobDetailsArea.setWrapStyleWord(true);
        jobDetailsArea.setLineWrap(true);
        jobDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane jobDetailsScroll = new JScrollPane(jobDetailsArea);
        rightPanel.add(jobDetailsScroll, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(500, 600));
        rightPanel.setVisible(false);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(300);

        // === Main Content Panel ===
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(topContainer, BorderLayout.NORTH);
        mainContentPanel.add(splitPane, BorderLayout.CENTER);

        // === Add everything to frame ===
        frame.add(sidePanel, BorderLayout.WEST);
        frame.add(mainContentPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        // === Button Actions ===
        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText().trim();
            leftPanel.removeAll();

            if (searchQuery.isEmpty()) {
                leftPanel.add(new JLabel("Please enter a search query."));
            } else {
                leftPanel.add(new JLabel("Searching for jobs related to: " + searchQuery));
                leftPanel.add(createJobCard("Job 1: " + searchQuery + " at Company A", jobDetailsArea, rightPanel, splitPane));
                leftPanel.add(createJobCard("Job 2: " + searchQuery + " at Company B", jobDetailsArea, rightPanel, splitPane));
                leftPanel.add(createJobCard("Job 3: " + searchQuery + " at Company C", jobDetailsArea, rightPanel, splitPane));
            }

            leftPanel.revalidate();
            leftPanel.repaint();
        });

        clearButton.addActionListener(e -> {
            searchField.setText("");
            leftPanel.removeAll();
            leftPanel.add(new JLabel("Job results will appear here..."));
            leftPanel.revalidate();
            leftPanel.repaint();
        });
    }

    private static JPanel createJobCard(String jobDescription, JTextArea jobDetailsArea, JPanel rightPanel, JSplitPane splitPane) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(280, 80));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        cardPanel.setBackground(Color.WHITE);

        JLabel jobLabel = new JLabel(jobDescription);
        jobLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cardPanel.add(jobLabel, BorderLayout.CENTER);

        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jobDetailsArea.setText("Detailed description of:\n" + jobDescription + "\n\nLorem ipsum dolor sit amet...");
                rightPanel.setVisible(true);
                splitPane.setDividerLocation(300);
            }
        });

        return cardPanel;
    }

    @Override
    protected void handleNavigation(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();
        dispose();
        
        switch(command) {
            case "Profile":
                new UserProfileScreen();
                break;
            case "Search Jobs":
                // Already on this screen
                break;
            case "Applications":
                new UserAppscreen();
                break;
            case "Logout":
                performLogout();
                break;
        }
    }
    
    private void performLogout() {
        // Similar logout logic
    }
}
