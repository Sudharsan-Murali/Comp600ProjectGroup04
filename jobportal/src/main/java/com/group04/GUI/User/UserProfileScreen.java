package com.group04.GUI.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import com.group04.GUI.JobPortalApplication; // Launches the login screen
import com.group04.GUI.User.Components.BaseScreen;
import com.group04.GUI.User.Components.ButtonFactory;

public class UserProfileScreen extends BaseScreen {

    private JLabel profilePicLabel;
    private JButton uploadButton, removeButton;
    private JPanel rightPanel; // Right panel uses CardLayout for smooth transitions.
    
    public UserProfileScreen() {
        super("User Profile");
        initializeUIWithCards();
        
        // Force full-screen view.
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setLocationRelativeTo(null);
        
        setVisible(true);
    }
    
    /**
     * Initializes the UI using CardLayout in the right panel.
     */
    private void initializeUIWithCards() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout(20, 20));
        
        // LEFT PANEL: Side Navigation.
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        leftPanel.setPreferredSize(new Dimension(180, screenSize.height));
        leftPanel.setBackground(new Color(44, 62, 80));
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        
        JButton profileButton = ButtonFactory.createSideButton("Profile");
        JButton searchJobsButton = ButtonFactory.createSideButton("Search Jobs");
        JButton applicationsButton = ButtonFactory.createSideButton("Applications");
        JButton logoutButton = ButtonFactory.createSideButton("Logout");
        
        Font navFont = new Font("SansSerif", Font.BOLD, 14);
        profileButton.setFont(navFont);
        searchJobsButton.setFont(navFont);
        applicationsButton.setFont(navFont);
        logoutButton.setFont(navFont);
        profileButton.setForeground(Color.WHITE);
        searchJobsButton.setForeground(Color.WHITE);
        applicationsButton.setForeground(Color.WHITE);
        logoutButton.setForeground(Color.WHITE);
        
        // Navigation: use CardLayout-based switching.
        profileButton.addActionListener(e -> showCard("Profile"));
        searchJobsButton.addActionListener(e -> showCard("SearchJobs"));
        applicationsButton.addActionListener(e -> showCard("Applications"));
        logoutButton.addActionListener(e -> performLogout());
        
        leftPanel.add(profileButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(searchJobsButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(applicationsButton);
        leftPanel.add(Box.createVerticalGlue());  // This pushes the logout button to the bottom.
        leftPanel.add(logoutButton);
        container.add(leftPanel, BorderLayout.WEST);
        
        // RIGHT PANEL: Content area using CardLayout.
        rightPanel = new JPanel(new CardLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.setBackground(Color.WHITE);
        
        // Add cards.
        rightPanel.add(createProfileScreenPanel(), "Profile");
        rightPanel.add(createSearchJobsPanel(), "SearchJobs");
        rightPanel.add(createApplicationsPanel(), "Applications");
        container.add(rightPanel, BorderLayout.CENTER);
    }
    
    /**
     * Switches the visible card based on card name.
     */
    private void showCard(String cardName) {
        CardLayout cl = (CardLayout) rightPanel.getLayout();
        cl.show(rightPanel, cardName);
    }
    
    /**
     * Creates a uniform title label.
     */
    private JLabel createTitleLabel(String text) {
        JLabel titleLabel = new JLabel(text, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);
        return titleLabel;
    }
    
    /**
     * Creates the Profile Screen panel.
     */
    private JPanel createProfileScreenPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(createTitleLabel("User Profile"), BorderLayout.NORTH);
        
        // Content: Profile Picture and Form.
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        contentPanel.add(createProfilePicPanel());
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(createProfileFormPanel());
      
        // Save Button Panel
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        savePanel.setOpaque(false);
        JButton saveButton = new JButton("SAVE");
        saveButton.setPreferredSize(new Dimension(100, 30));
        // You can add an ActionListener to saveButton if needed.
        savePanel.add(saveButton);
        
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(savePanel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Creates the Search Jobs panel with a table.
     */
    /**
 * Creates an updated Search Jobs panel, inspired by LinkedIn’s style,
 * with a search bar, job listing on the left, and job details on the right.
 */
private JPanel createSearchJobsPanel() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);

    // Title label at the top
    mainPanel.add(createTitleLabel("Search Jobs"), BorderLayout.NORTH);

    // 1) Top Search Bar
    JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    searchBarPanel.setBackground(Color.WHITE);

    JLabel searchLabel = new JLabel("Search:");
    JTextField searchField = new JTextField(20);
    JButton searchButton = new JButton("Search");
    // Optional advanced filter button(s):
    JButton filterButton = new JButton("Filter");

    searchBarPanel.add(searchLabel);
    searchBarPanel.add(searchField);
    searchBarPanel.add(searchButton);
    searchBarPanel.add(filterButton);

    // 2) SplitPane divides the left job list from the right job detail
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setDividerLocation(300); // Initial divider position
    splitPane.setOneTouchExpandable(true);

    // 2a) Left Panel: Job Listing
    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.setBackground(Color.WHITE);

    // Create a placeholder table or list for job listings
    String[] columnNames = { "Title", "Company", "Location", "Remote", "Date Posted" };
    Object[][] data = {
        { "Représentant des services tech", "Castolin Eutectic", "Val-d'Or", "Remote", "1 day ago" },
        { "General Consideration", "Riva", "Somewhere", "N/A", "2 days ago" },
        { "Remote Sales and Service Rep", "Company X", "Remote", "Remote", "3 days ago" },
        { "Executive Assistant", "ABC Corp", "Hybrid", "Partial", "4 days ago" },
        // ... add more placeholder rows as needed
    };

    JTable jobTable = new JTable(data, columnNames);
    jobTable.setRowHeight(30);
    jobTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
    jobTable.setGridColor(new Color(189, 195, 199));
    jobTable.setShowGrid(true);
    jobTable.setIntercellSpacing(new Dimension(5, 5));
    jobTable.setBackground(Color.WHITE);
    jobTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    // Table header styling
    JTableHeader header = jobTable.getTableHeader();
    header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
    header.setFont(new Font("SansSerif", Font.BOLD, 16));
    header.setBackground(new Color(52, 73, 94));
    header.setForeground(Color.WHITE);

    JScrollPane tableScrollPane = new JScrollPane(jobTable);
    tableScrollPane.getViewport().setBackground(Color.WHITE);

    leftPanel.add(tableScrollPane, BorderLayout.CENTER);

    // 2b) Right Panel: Job Details
    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    rightPanel.setBackground(Color.WHITE);

    // Placeholder job detail
    JLabel jobTitleLabel = new JLabel("<html><b>Représentant des services techniques / Technical Services Representative Val-d’Or</b></html>");
    jobTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

    JLabel jobLocationLabel = new JLabel("Val-d'Or, QC   |   Remote   |   29 applicants");
    JLabel jobShortDescLabel = new JLabel("<html>How your profile and resume fits this job:<br>Get AI-powered advice on how to get started...<br>(Placeholder text)</html>");
    // Buttons for Easy Apply, Save, etc.
    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    actionPanel.setBackground(Color.WHITE);
    JButton easyApplyButton = new JButton("Easy Apply");
    JButton saveButton = new JButton("Save");
    JButton shareButton = new JButton("Share");

    actionPanel.add(easyApplyButton);
    actionPanel.add(saveButton);
    actionPanel.add(shareButton);

    // Larger text area for job description details
    JTextArea jobDetailArea = new JTextArea(
        "Full job description goes here...\n\n" +
        "1) Key responsibilities\n" +
        "2) Requirements\n" +
        "3) Benefits\n" +
        "... (Placeholder text, from your old user screen or real data)."
    );
    jobDetailArea.setEditable(false);
    jobDetailArea.setLineWrap(true);
    jobDetailArea.setWrapStyleWord(true);
    jobDetailArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

    JScrollPane jobDetailScroll = new JScrollPane(jobDetailArea);
    jobDetailScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // Add elements to the right panel
    JPanel topDetailPanel = new JPanel();
    topDetailPanel.setLayout(new BoxLayout(topDetailPanel, BoxLayout.Y_AXIS));
    topDetailPanel.setBackground(Color.WHITE);
    topDetailPanel.add(jobTitleLabel);
    topDetailPanel.add(Box.createVerticalStrut(5));
    topDetailPanel.add(jobLocationLabel);
    topDetailPanel.add(Box.createVerticalStrut(10));
    topDetailPanel.add(jobShortDescLabel);
    topDetailPanel.add(Box.createVerticalStrut(10));
    topDetailPanel.add(actionPanel);

    rightPanel.add(topDetailPanel, BorderLayout.NORTH);
    rightPanel.add(jobDetailScroll, BorderLayout.CENTER);

    // Add the left and right panels to the JSplitPane
    splitPane.setLeftComponent(leftPanel);
    splitPane.setRightComponent(rightPanel);

    // Combine the search bar (top) and the splitPane (center) in a parent panel
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.setBackground(Color.WHITE);
    centerPanel.add(searchBarPanel, BorderLayout.NORTH);
    centerPanel.add(splitPane, BorderLayout.CENTER);

    mainPanel.add(centerPanel, BorderLayout.CENTER);

    return mainPanel;
}
   
    /**
     * Creates the Applications panel with a table.
     */
    private JPanel createApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        panel.add(createTitleLabel("Applications"), BorderLayout.NORTH);
        
        // Create a table for user job applications with placeholder data.
        String[] columnNames = {"APP NO", "JOB TITLE", "APPLICATION DATE", "STATUS", "VIEW"};
        Object[][] data = new Object[10][5]; // Placeholder data.
        JTable appTable = new JTable(data, columnNames);
        
        appTable.setRowHeight(30);
        appTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        appTable.setGridColor(new Color(189, 195, 199));
        appTable.setShowGrid(true);
        appTable.setIntercellSpacing(new Dimension(5, 5));
        appTable.setBackground(Color.WHITE);
        
        JTableHeader header = appTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        
        appTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane tableScrollPane = new JScrollPane(appTable);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Footer for pagination (placeholder).
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(Color.WHITE);
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JLabel pageLabel = new JLabel("Page 1");
        prevButton.addActionListener(e -> System.out.println("Prev Page"));
        nextButton.addActionListener(e -> System.out.println("Next Page"));
        footerPanel.add(prevButton);
        footerPanel.add(pageLabel);
        footerPanel.add(nextButton);
        
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(footerPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    /**
     * Creates the Profile Picture section.
     */
    private JPanel createProfilePicPanel() {
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setOpaque(false);
        
        JPanel profilePicPanel = new JPanel(new BorderLayout());
        profilePicPanel.setMaximumSize(new Dimension(150, 150));
        profilePicPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePicPanel.setOpaque(false);
        
        profilePicLabel = new JLabel("Add Profile Picture", SwingConstants.CENTER);
        profilePicLabel.setPreferredSize(new Dimension(120, 120));
        profilePicLabel.setMinimumSize(new Dimension(120, 120));
        profilePicLabel.setMaximumSize(new Dimension(120, 120));
        profilePicLabel.setHorizontalAlignment(JLabel.CENTER);
        profilePicLabel.setVerticalAlignment(JLabel.CENTER);
        profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        profilePicLabel.setOpaque(true);
        profilePicLabel.setBackground(Color.LIGHT_GRAY);
        
        profilePicPanel.add(profilePicLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setOpaque(false);
        uploadButton = ButtonFactory.createSideButton("Upload");
        removeButton = ButtonFactory.createSideButton("Remove");
        removeButton.setVisible(false);
        
        uploadButton.addActionListener(e -> uploadProfilePicture());
        removeButton.addActionListener(e -> removeProfilePicture());
        
        buttonPanel.add(uploadButton);
        buttonPanel.add(removeButton);
        
        containerPanel.add(profilePicPanel);
        containerPanel.add(Box.createVerticalStrut(5));
        containerPanel.add(buttonPanel);
        return containerPanel;
    }
    
    /**
     * Creates the Profile Form panel for user details.
     */
    private JPanel createProfileFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Two-column fields.
        String[][] fields = {
            {"First Name", "Last Name"},
            {"Email", "Phone"},
            {"Current Company", "Job Role"}
        };
        
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                addFormField(formPanel, fields[i][j], i, j, gbc);
            }
        }
        
        // Additional single-column fields.
        addFormField(formPanel, "Preferences / Skills", fields.length, 0, gbc);
        addFormField(formPanel, "LinkedIn URL", fields.length + 1, 0, gbc);
        addFormField(formPanel, "Availability", fields.length + 2, 0, gbc);
        
        return formPanel;
    }
    
    /**
     * Adds an individual form field (label and text field) to the given panel.
     * The label is formatted with an HTML string that appends a black colon and a red asterisk.
     */
    private void addFormField(JPanel panel, String labelText, int row, int col, GridBagConstraints gbc) {
        JLabel label = new JLabel("<html>" + labelText + " <font color='black'>:</font> <font color='red'>*</font></html>");
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        JTextField textField = new JTextField(25);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(250, 30));
        
        gbc.gridx = col * 2;
        gbc.gridy = row;
        panel.add(label, gbc);
        
        gbc.gridx = col * 2 + 1;
        panel.add(textField, gbc);
    }
    
    /**
     * Opens a file chooser, scales the selected image, and updates the profile picture.
     */
    private void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.exists()) {
                ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                Image image = imageIcon.getImage().getScaledInstance(
                    profilePicLabel.getWidth(), profilePicLabel.getHeight(), Image.SCALE_SMOOTH);
                profilePicLabel.setIcon(new ImageIcon(image));
                profilePicLabel.setText("");
                removeButton.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "File does not exist.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Resets the profile picture.
     */
    private void removeProfilePicture() {
        profilePicLabel.setIcon(null);
        profilePicLabel.setText("Add Profile Picture");
        removeButton.setVisible(false);
    }
    
    /**
     * Handles navigation button events by switching cards in the right panel.
     */
    protected void handleNavigation(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();
        CardLayout cl = (CardLayout) rightPanel.getLayout();
        switch (command) {
            case "Profile":
                cl.show(rightPanel, "Profile");
                break;
            case "Search Jobs":
                cl.show(rightPanel, "SearchJobs");
                break;
            case "Applications":
                cl.show(rightPanel, "Applications");
                break;
            case "Logout":
                performLogout();
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }
    
    /**
     * Logs out of the application and launches the login screen.
     */
    private void performLogout() {
        System.out.println("Logging out...");
        dispose();
        // SwingUtilities.invokeLater(() -> new com.group04.GUI.JobPortalApplication());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserProfileScreen::new);
    }
}
