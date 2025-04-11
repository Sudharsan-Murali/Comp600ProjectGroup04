package com.group04.GUI.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import com.group04.GUI.User.Components.UIUtils;
import com.group04.GUI.User.Components.BaseScreen;
import com.group04.GUI.User.Components.ButtonFactory;

public class JobSearchScreen extends BaseScreen {

    private String userEmail;

    public JobSearchScreen(String email) {
        super("Job Search");
        this.userEmail = email;
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainContentPanel = new JPanel(new BorderLayout());

        // North container (Title + Search)
        JPanel northContainer = new JPanel();
        northContainer.setLayout(new BoxLayout(northContainer, BoxLayout.Y_AXIS));
        northContainer.add(UIUtils.createTitleLabel("Job Search", "src/Assets/Profile-Pic-Icon.png"));
        northContainer.add(createSearchPanel());

        // Main content
        mainContentPanel.add(northContainer, BorderLayout.NORTH);
        mainContentPanel.add(createJobContentSplitPane(), BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel searchLabel = new JLabel("Search Jobs:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = ButtonFactory.createSideButton("Search");
        JButton clearButton = ButtonFactory.createSideButton("Clear");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        // Button actions
        searchButton.addActionListener(e -> performSearch(searchField.getText()));
        clearButton.addActionListener(e -> clearSearch(searchField));

        return searchPanel;
    }

    private void performSearch(String searchQuery) {
        // Handle search results
    }

    private void clearSearch(JTextField searchField) {
        searchField.setText("");
    }

    private JSplitPane createJobContentSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Initialize the left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // Set layout for leftPanel
        leftPanel.setPreferredSize(new Dimension(350, 500));
        leftPanel.add(new JLabel("Results will appear here..."));

        // Right panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        JTextArea jobDetailsArea = new JTextArea("Select a job to view details");
        rightPanel.add(new JScrollPane(jobDetailsArea), BorderLayout.CENTER);

        // Set the left and right components of the split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        return splitPane;
    }

    @Override
    protected void handleNavigation(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();
        dispose();

        switch (command) {
            case "Profile":
                SwingUtilities.invokeLater(() -> new UserProfileScreen(userEmail));
                break;
            case "Search Jobs":
                SwingUtilities.invokeLater(() -> new JobSearchScreen(userEmail));
                break;
            case "Applications":
                SwingUtilities.invokeLater(() -> new UserAppscreen());
                break;
            case "Logout":
                performLogout();
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    private void performLogout() {
        System.out.println("Logging out...");
        dispose();
    }

    // @Override
    // protected void handleNavigation(ActionEvent e) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'handleNavigation'");
    // }
}
