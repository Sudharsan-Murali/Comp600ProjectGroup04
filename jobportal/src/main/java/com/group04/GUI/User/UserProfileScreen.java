package com.group04.GUI.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.group04.DAO.User;
import com.group04.DAO.UserDAO;
import com.group04.GUI.JobPortalApplication; // Launches the login screen
import com.group04.GUI.User.Components.BaseScreen;
import com.group04.GUI.User.Components.ButtonFactory;

public class UserProfileScreen extends BaseScreen {

    private String loggedInEmail;
    private JLabel profilePicLabel;
    private JButton uploadButton, removeButton;
    private JPanel rightPanel; // Right panel uses CardLayout for smooth transitions.

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtCompany;
    private JTextField txtJobRole;
    private JTextField txtPreferences;
    private JTextField txtLinkedIn;
    private JTextField txtAvailability;

    private File resumeFile; // This will hold the selected resume file.

    public UserProfileScreen(String userEmail) {
        super("User Profile");
        this.loggedInEmail = userEmail;  // now the email is stored
        // TODO Auto-generated constructor stub
        txtFirstName = new JTextField(25);
        txtLastName = new JTextField(25);
        txtEmail = new JTextField(25);
        txtPhone = new JTextField(25);
        txtCompany = new JTextField(25);
        txtJobRole = new JTextField(25);
        txtPreferences = new JTextField(25);
        txtLinkedIn = new JTextField(25);
        txtAvailability = new JTextField(25);

        // Make non-updateable fields read-only.
        txtFirstName.setEditable(false);
        txtLastName.setEditable(false);
        txtEmail.setEditable(false);
        txtPhone.setEditable(false);
        txtPreferences.setEditable(false);
        
        // Now build the UI.
        initializeUIWithCards();
        // Other UI settings
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setLocationRelativeTo(null);

        // Load user data from the database.
        loadUserData();

        setVisible(true);
    }

    public User getUserProfile(String email) {
        String query = "SELECT first_name, last_name, email, mobile, dob, role_id, security_que, security_ans, company, job_role, preferences, linkedin_url, availability FROM users WHERE email = ?";
        User user = null;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setMobile(rs.getString("mobile"));
                user.setDob(rs.getString("dob"));
                user.setRole(rs.getInt("role_id"));
                user.setSecurityQuestion(rs.getString("security_que"));
                user.setSecurityAnswer(rs.getString("security_ans"));
                // New additional fields:
                user.setCompany(rs.getString("company"));
                user.setJobRole(rs.getString("job_role"));
                user.setPreferences(rs.getString("preferences"));
                user.setLinkedInUrl(rs.getString("linkedin_url")); // Ensure your setter is named accordingly.
                user.setAvailability(rs.getString("availability"));
            } else {
                System.out.println("No user found with email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while fetching user profile.");
        }

        return user;
    }

    private Connection getConnection() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
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
        leftPanel.add(Box.createVerticalGlue()); // This pushes the logout button to the bottom.
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
        saveButton.addActionListener(e -> saveProfileData()); 
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
        JLabel jobTitleLabel = new JLabel(
                "<html><b>Représentant des services techniques / Technical Services Representative Val-d’Or</b></html>");
        jobTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel jobLocationLabel = new JLabel("Val-d'Or, QC   |   Remote   |   29 applicants");
        JLabel jobShortDescLabel = new JLabel(
                "<html>How your profile and resume fits this job:<br>Get AI-powered advice on how to get started...<br>(Placeholder text)</html>");
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
                        "... (Placeholder text, from your old user screen or real data).");
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
        String[] columnNames = { "APP NO", "JOB TITLE", "APPLICATION DATE", "STATUS", "VIEW" };
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

        // Row 0: First Name and Last Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("<html>First Name <font color='red'>*</font>:</html>"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtFirstName, gbc); // Use instance variable

        gbc.gridx = 2;
        formPanel.add(new JLabel("<html>Last Name <font color='red'>*</font>:</html>"), gbc);

        gbc.gridx = 3;
        formPanel.add(txtLastName, gbc); // Use instance variable

        // Row 1: Email and Phone
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("<html>Email <font color='red'>*</font>:</html>"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc); // Use instance variable

        gbc.gridx = 2;
        formPanel.add(new JLabel("<html>Phone <font color='red'>*</font>:</html>"), gbc);

        gbc.gridx = 3;
        formPanel.add(txtPhone, gbc); // Use instance variable

        // Row 2: Current Company and Job Role
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("<html>Current Company <font color='red'>*</font>:</html>"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtCompany, gbc); // Use instance variable

        gbc.gridx = 2;
        formPanel.add(new JLabel("<html>Job Role <font color='red'>*</font>:</html>"), gbc);

        gbc.gridx = 3;
        formPanel.add(txtJobRole, gbc); // Use instance variable

        // Row 3: Preferences / Skills (single column)
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Preferences / Skills:"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtPreferences, gbc); // Use instance variable

        // Row 4: LinkedIn URL
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("LinkedIn URL:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLinkedIn, gbc); // Use instance variable

        // Row 5: Availability
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Availability:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtAvailability, gbc); // Use instance variable

        // Add the resume field and save button in subsequent rows
        addResumeField(formPanel, gbc);
        // addSaveButton(formPanel, gbc);

        return formPanel;
    }

    private void addResumeField(JPanel formPanel, GridBagConstraints gbc) {
        // Increment for resume field.
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Resume (Optional):"), gbc);

        // Panel to hold the two buttons (Upload and Delete)
        JPanel resumeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        // Upload Resume button
        JButton uploadResumeButton = new JButton("Upload");
        resumeButtonPanel.add(uploadResumeButton);

        // Delete Resume button
        JButton deleteResumeButton = new JButton("Delete");
        // Initially, hide or disable the Delete button since no file is selected.
        deleteResumeButton.setVisible(false);
        resumeButtonPanel.add(deleteResumeButton);

        // Add the panel to the form.
        gbc.gridx = 1;
        formPanel.add(resumeButtonPanel, gbc);

        // Action for the Upload button:
        uploadResumeButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            int returnValue = fileChooser.showOpenDialog(this); // show dialog relative to frame
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                resumeFile = fileChooser.getSelectedFile();
                if (resumeFile != null) {
                    // Update button text and show the Delete button.
                    uploadResumeButton.setText("Selected: " + resumeFile.getName());
                    deleteResumeButton.setVisible(true);
                }
            }
        });

        // Action for the Delete button:
        deleteResumeButton.addActionListener(e -> {
            // Clear the resume file variable.
            resumeFile = null;
            // Reset the Upload button text.
            uploadResumeButton.setText("Upload Resume");
            // Hide the Delete button.
            deleteResumeButton.setVisible(false);
        });
    }

    private void addSaveButton(JPanel formPanel, GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy++; // increment row for save button
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(100, 30));
        formPanel.add(saveButton, gbc);

        saveButton.addActionListener(e -> saveProfileData());
    }

    private void saveProfileData() {
        // Create a new User object for the update.
        User updatedUser = new User();
        
        // Set non-updateable (read-only) fields from pre-loaded data.
        updatedUser.setFirstName(txtFirstName.getText());              // Not editable
        updatedUser.setLastName(txtLastName.getText());                // Not editable
        updatedUser.setEmail(txtEmail.getText());                      // Not editable
        updatedUser.setMobile(txtPhone.getText());                     // Not editable
        updatedUser.setPreferences(txtPreferences.getText());          // Not editable
        
        // Set editable fields (which the user may have updated).
        updatedUser.setCompany(txtCompany.getText());
        updatedUser.setJobRole(txtJobRole.getText());

        updatedUser.setLinkedInUrl(txtLinkedIn.getText());
        updatedUser.setAvailability(txtAvailability.getText());

         // Debug prints:
        System.out.println("DEBUG: Company = " + updatedUser.getCompany());
        System.out.println("DEBUG: JobRole = " + updatedUser.getJobRole());
        System.out.println("DEBUG: LinkedIN URL = " + updatedUser.getLinkedInUrl());
        System.out.println("DEBUG: Availability = " + updatedUser.getAvailability());
            
        // The Email is used as a key; it remains the same.
        // Create a new UserDAO instance and update the record.
        UserDAO userDAO = new UserDAO();
        boolean updateSuccess = userDAO.updateUserProfile(updatedUser);
        
        if (updateSuccess) {
            JOptionPane.showMessageDialog(this, 
                    "Changes updated successfully.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Error updating changes.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Optionally, process the resume file if needed.
        if (resumeFile != null) {
            System.out.println("Resume file selected: " + resumeFile.getAbsolutePath());
            // Add additional resume processing here if needed.
        }
    }
    

    /**
     * Adds an individual form field (label and text field) to the given panel.
     * The label is formatted with an HTML string that appends a black colon and a
     * red asterisk.
     */
    private void addFormField(JPanel panel, String labelText, int row, int col, GridBagConstraints gbc) {
        JLabel label = new JLabel(
                "<html>" + labelText + " <font color='black'>:</font> <font color='red'>*</font></html>");
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
     * Opens a file chooser, scales the selected image, and updates the profile
     * picture.
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

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserProfileScreen("Pg991@example.com"));
    } */

    private void loadUserData() {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserProfile(loggedInEmail);

        if (user != null) {
            txtFirstName.setText(user.getFirstName());
            txtLastName.setText(user.getLastName());
            txtEmail.setText(user.getEmail());
            txtPhone.setText(user.getMobile());
            txtCompany.setText(user.getCompany());
            txtJobRole.setText(user.getJobRole());
            txtPreferences.setText(user.getPreferences());
            txtLinkedIn.setText(user.getLinkedInUrl());
            txtAvailability.setText(user.getAvailability());
        } else {
            JOptionPane.showMessageDialog(this, "User data not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("DEBUG: loggedInEmail = " + loggedInEmail);
    }
}
