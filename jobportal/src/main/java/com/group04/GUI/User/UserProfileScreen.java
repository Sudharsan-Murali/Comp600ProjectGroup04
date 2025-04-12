package com.group04.GUI.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private JLabel resumeStatusLabel; // To display resume upload status
    private JLabel resumeFileNameLabel;
    private File resumeFile; // Holds the selected resume file.

    public UserProfileScreen(String userEmail) {
        super("User Profile");
        this.loggedInEmail = userEmail; // now the email is stored

        // Initialize text fields.
        txtFirstName = new JTextField(25);
        txtLastName = new JTextField(25);
        txtEmail = new JTextField(25);
        txtPhone = new JTextField(25);
        txtCompany = new JTextField(25);
        txtJobRole = new JTextField(25);
        txtPreferences = new JTextField(25);
        txtLinkedIn = new JTextField(25);
        txtAvailability = new JTextField(25);

        // Initialize resume status label (for clickable file name).
        resumeStatusLabel = new JLabel("<html><u>No file selected</u></html>");
        resumeStatusLabel.setForeground(Color.BLUE);
        resumeStatusLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Build the UI.
        initializeUIWithCards();

        // Other UI settings.
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setLocationRelativeTo(null);

        // Load user data from the database.
        loadUserData();

        setVisible(true);
    }

    // Stub for getUserProfile; ideally, profile data is fetched via the DAO.
    // (This method is used only in the UI if needed.)
    public User getUserProfile(String email) {
        // You might not be using this method since your DAO already provides one.
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
                user.setCompany(rs.getString("company"));
                user.setJobRole(rs.getString("job_role"));
                user.setPreferences(rs.getString("preferences"));
                user.setLinkedInUrl(rs.getString("linkedin_url"));
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

    // Placeholder for getConnection() - make sure to implement or remove if using
    // DAO exclusively.
    private Connection getConnection() {
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
    private JPanel createSearchJobsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Title label at the top
        mainPanel.add(createTitleLabel("Search Jobs"), BorderLayout.NORTH);

        // Top Search Bar
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchBarPanel.setBackground(Color.WHITE);
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton filterButton = new JButton("Filter");
        searchBarPanel.add(searchLabel);
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        searchBarPanel.add(filterButton);

        // SplitPane divides the job list from job details.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setOneTouchExpandable(true);

        // Left Panel: Job Listing
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        String[] columnNames = { "Title", "Company", "Location", "Remote", "Date Posted" };
        Object[][] data = {
                { "Représentant des services tech", "Castolin Eutectic", "Val-d'Or", "Remote", "1 day ago" },
                { "General Consideration", "Riva", "Somewhere", "N/A", "2 days ago" },
                { "Remote Sales and Service Rep", "Company X", "Remote", "Remote", "3 days ago" },
                { "Executive Assistant", "ABC Corp", "Hybrid", "Partial", "4 days ago" }
        };
        JTable jobTable = new JTable(data, columnNames);
        jobTable.setRowHeight(30);
        jobTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jobTable.setGridColor(new Color(189, 195, 199));
        jobTable.setShowGrid(true);
        jobTable.setIntercellSpacing(new Dimension(5, 5));
        jobTable.setBackground(Color.WHITE);
        jobTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JTableHeader header = jobTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        JScrollPane tableScrollPane = new JScrollPane(jobTable);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Right Panel: Job Details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(Color.WHITE);
        JLabel jobTitleLabel = new JLabel(
                "<html><b>Représentant des services techniques / Technical Services Representative Val-d’Or</b></html>");
        jobTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel jobLocationLabel = new JLabel("Val-d'Or, QC   |   Remote   |   29 applicants");
        JLabel jobShortDescLabel = new JLabel(
                "<html>How your profile and resume fits this job:<br>Get AI-powered advice on how to get started...<br>(Placeholder text)</html>");
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(Color.WHITE);
        JButton easyApplyButton = new JButton("Easy Apply");
        JButton saveButtonJob = new JButton("Save");
        JButton shareButton = new JButton("Share");
        actionPanel.add(easyApplyButton);
        actionPanel.add(saveButtonJob);
        actionPanel.add(shareButton);
        JTextArea jobDetailArea = new JTextArea(
                "Full job description goes here...\n\n1) Key responsibilities\n2) Requirements\n3) Benefits\n...");
        jobDetailArea.setEditable(false);
        jobDetailArea.setLineWrap(true);
        jobDetailArea.setWrapStyleWord(true);
        jobDetailArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane jobDetailScroll = new JScrollPane(jobDetailArea);
        jobDetailScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
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
        String[] columnNames = { "APP NO", "JOB TITLE", "APPLICATION DATE", "STATUS", "VIEW" };
        Object[][] data = new Object[10][5]; // Placeholder data.
        JTable appTable = new JTable(data, columnNames);
        appTable.setRowHeight(30);
        appTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        appTable.setGridColor(new Color(189, 195, 199));
        appTable.setShowGrid(true);
        appTable.setIntercellSpacing(new Dimension(5, 5));
        appTable.setBackground(Color.WHITE);
        JTableHeader headerApp = appTable.getTableHeader();
        headerApp.setPreferredSize(new Dimension(headerApp.getPreferredSize().width, 35));
        headerApp.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerApp.setBackground(new Color(52, 73, 94));
        headerApp.setForeground(Color.WHITE);
        appTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane tableScrollPane = new JScrollPane(appTable);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
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
        formPanel.add(txtFirstName, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("<html>Last Name <font color='red'>*</font>:</html>"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtLastName, gbc);

        // Row 1: Email and Phone
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("<html>Email <font color='red'>*</font>:</html>"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("<html>Phone <font color='red'>*</font>:</html>"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtPhone, gbc);

        // Row 2: Current Company and Job Role
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("<html>Current Company <font color='red'>*</font>:</html>"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtCompany, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("<html>Job Role <font color='red'>*</font>:</html>"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtJobRole, gbc);

        // Row 3: Preferences / Skills (single column)
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Preferences / Skills:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPreferences, gbc);

        // Row 4: LinkedIn URL
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("LinkedIn URL:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLinkedIn, gbc);

        // Row 5: Availability
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Availability:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtAvailability, gbc);

        // Add the resume field.
        addResumeField(formPanel, gbc);

        return formPanel;
    }

    private void addResumeField(JPanel formPanel, GridBagConstraints gbc) {
        // Label: "Resume (Optional):"
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Resume (Optional):"), gbc);

        // Panel to hold Upload and Delete buttons for resume.
        JPanel resumeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        resumeButtonPanel.setOpaque(false);
        JButton uploadResumeButton = new JButton("Upload");
        resumeButtonPanel.add(uploadResumeButton);
        JButton deleteResumeButton = new JButton("Delete");
        deleteResumeButton.setVisible(false);
        resumeButtonPanel.add(deleteResumeButton);

        gbc.gridx = 1;
        formPanel.add(resumeButtonPanel, gbc);

        // Next row: Instead of adding a download button, add a clickable label for the
        // file name.
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Resume:"), gbc);

        // Create the clickable label.
        resumeFileNameLabel = new JLabel();
        resumeFileNameLabel.setForeground(Color.BLUE); // typical hyperlink color
        resumeFileNameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resumeFileNameLabel.setText("<html><u>No file selected</u></html>");

        // Add a mouse listener to trigger download when the label is clicked.
        resumeFileNameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                downloadResume();
            }
        });

        gbc.gridx = 1;
        formPanel.add(resumeFileNameLabel, gbc);

        // Action for the Upload button.
        uploadResumeButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File resumeFile = fileChooser.getSelectedFile();
                if (resumeFile != null) {
                    byte[] resumeBytes = fileToByteArray(resumeFile);
                    if (resumeBytes != null) {
                        UserDAO userDAO = new UserDAO();
                        boolean updated = userDAO.updateUserResume(loggedInEmail, resumeBytes);
                        if (updated) {
                            // Instead of showing a button, update the clickable label with the file name.
                            String fileName = resumeFile.getName();
                            resumeFileNameLabel.setText("<html><u>" + fileName + "</u></html>");
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update resume in database.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Action for the Delete button (optional).
        deleteResumeButton.addActionListener(e -> {
            // Optionally, call a method to delete resume from DB and then update the UI.
            resumeFileNameLabel.setText("<html><u>No file selected</u></html>");
        });
    }

    // Download the resume from DB and open it.
    private void downloadResume() {
        UserDAO userDAO = new UserDAO();
        byte[] resumeBytes = userDAO.getUserResume(loggedInEmail);
        if (resumeBytes != null && resumeBytes.length > 0) {
            try {
                File tempFile = File.createTempFile("resume_", ".pdf");
                tempFile.deleteOnExit();
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(resumeBytes);
                }
                Desktop.getDesktop().open(tempFile);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Could not open the resume file.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No resume found to download.",
                    "Resume Not Found",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserProfile(loggedInEmail);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.setFirstName(txtFirstName.getText());
        user.setLastName(txtLastName.getText());
        user.setEmail(txtEmail.getText());
        user.setMobile(txtPhone.getText());
        user.setCompany(txtCompany.getText());
        user.setJobRole(txtJobRole.getText());
        user.setPreferences(txtPreferences.getText());
        user.setLinkedInUrl(txtLinkedIn.getText());
        user.setAvailability(txtAvailability.getText());

        boolean success = userDAO.updateUserProfile(user);
        if (success) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] fileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            int readBytes = fis.read(data);
            System.out.println("Read " + readBytes + " bytes from resume file.");
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Upload profile picture
    private void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.exists()) {
                byte[] imageBytes = fileToByteArray(selectedFile);
                ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                Image image = imageIcon.getImage().getScaledInstance(
                        profilePicLabel.getWidth(), profilePicLabel.getHeight(), Image.SCALE_SMOOTH);
                profilePicLabel.setIcon(new ImageIcon(image));
                profilePicLabel.setText("");
                removeButton.setVisible(true);
                UserDAO userDAO = new UserDAO();
                boolean updated = userDAO.updateUserProfilePicture(loggedInEmail, imageBytes);
                if (!updated) {
                    JOptionPane.showMessageDialog(this, "Failed to update profile picture in database.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "File does not exist.",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Remove profile picture
    private void removeProfilePicture() {
        profilePicLabel.setIcon(null);
        profilePicLabel.setText("Add Profile Picture");
        removeButton.setVisible(false);
    }

    // Handles navigation events.
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

    // Logs out and closes the screen.
    private void performLogout() {
        System.out.println("Logging out...");
        dispose();
        // Optionally, launch the login screen again.
    }

    // Load user data from DB (profile picture, details, resume status).
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

            // Load profile picture.
            byte[] picBytes = userDAO.getUserProfilePicture(loggedInEmail);
            if (picBytes != null) {
                ImageIcon icon = new ImageIcon(picBytes);
                int labelWidth = profilePicLabel.getWidth();
                int labelHeight = profilePicLabel.getHeight();
                if (labelWidth == 0 || labelHeight == 0) {
                    Dimension pref = profilePicLabel.getPreferredSize();
                    labelWidth = (pref.width > 0) ? pref.width : 120;
                    labelHeight = (pref.height > 0) ? pref.height : 120;
                }
                Image image = icon.getImage().getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
                profilePicLabel.setIcon(new ImageIcon(image));
                profilePicLabel.setText("");
                removeButton.setVisible(true);
            }

            // Load resume.
            // Load resume.
            byte[] resumeBytes = userDAO.getUserResume(loggedInEmail);
            if (resumeBytes != null && resumeBytes.length > 0) {
                System.out.println("Resume is available, size: " + resumeBytes.length);
                // Update the clickable label. You can choose to display "View Resume" or a file
                // name if you stored it.
                resumeFileNameLabel.setText("<html><u>View by default Resume</u></html>");
            } else {
                System.out.println("No resume found for this user.");
                resumeFileNameLabel.setText("<html><u>No file selected</u></html>");
            }
        } else {
            JOptionPane.showMessageDialog(this, "User data not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("DEBUG: loggedInEmail = " + loggedInEmail);
    }
}
