package com.group04.GUI.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.group04.GUI.Components.ButtonColumn;
import com.group04.DAO.User;
import com.group04.DAO.UserDAO;
import com.group04.GUI.Components.BaseScreen;
import com.group04.GUI.Components.ButtonFactory;

public class UserProfileScreen extends BaseScreen {

    private String loggedInEmail;
    private JLabel profilePicLabel;
    private JButton uploadButton, removeButton;
    private JPanel rightPanel;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtCompany;
    private JTextField txtJobRole;
    private JTextField txtPreferences;
    private JTextField txtLinkedIn;
    private JTextField txtAvailability;
    
    private JLabel resumeFileNameLabel;
    private JTable appTable;
    private ActionListener viewAction, withdrawAction, resumeAction;
    private int currentAppPage = 1;
    private final int appPageSize = 20;    
    private final String[] appColumnNames = { "APP NO", "JOB TITLE", "APPLICATION DATE", "STATUS", "VIEW", "WITHDRAW",
            "RESUME" };    

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

        // Make non-editable fields.
        txtFirstName.setEditable(false);
        txtLastName.setEditable(false);
        txtEmail.setEditable(false);
        txtPhone.setEditable(false);
        txtPreferences.setEditable(false);

        // Initialize resume label.
        resumeFileNameLabel = new JLabel("<html><u>No file selected</u></html>");
        resumeFileNameLabel.setForeground(Color.BLUE);
        resumeFileNameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resumeFileNameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                downloadResume();
            }
        });
        // ... initialize fields ...
        initializeUIWithCards();
        initializeActionListeners(); // <--- Add this here
    
        // Other UI settings.
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        loadUserData();
        setVisible(true);
    }

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

    private Connection getConnection() {
        throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
    }

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
        for (JButton btn : new JButton[] { profileButton, searchJobsButton, applicationsButton, logoutButton }) {
            btn.setFont(navFont);
            btn.setForeground(Color.WHITE);
        }

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

    private void showCard(String cardName) {
        CardLayout cl = (CardLayout) rightPanel.getLayout();
        cl.show(rightPanel, cardName);
    }

    private JLabel createTitleLabel(String text) {
        JLabel titleLabel = new JLabel(text, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);
        return titleLabel;
    }

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

    private JPanel createSearchJobsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(createTitleLabel("Search Jobs"), BorderLayout.NORTH);

        // Top Search Bar: Only contains Filter and Reset Filter buttons.
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchBarPanel.setBackground(Color.WHITE);
        JButton filterButton = new JButton("Filter");
        JButton resetFilterButton = new JButton("Reset Filter");
        searchBarPanel.add(filterButton);
        searchBarPanel.add(resetFilterButton);

        // SplitPane divides the job list from job details.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setOneTouchExpandable(true);

        // LEFT PANEL: Job Listing
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        // Define the table columns.
        final String[] columnNames = { "Title", "Company", "Location", "Job Type" };
        // Setup pagination variables.
        final int pageSize = 20; // Number of records per page.
        final int[] currentPage = { 1 }; // Mutable current page stored in an array

        UserDAO userDAO = new UserDAO();
        Object[][] data = userDAO.getJobListings(currentPage[0], pageSize);

        // Create table with non-editable model.
        final JTable jobTable = new JTable(new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
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

        // Pagination Panel at the bottom of leftPanel.
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(Color.WHITE);
        JButton prevButton = new JButton("Prev");
        JButton nextButton = new JButton("Next");
        final JLabel pageLabel = new JLabel("Page " + currentPage[0]);
        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);
        leftPanel.add(paginationPanel, BorderLayout.SOUTH);

        // Prev button action.
        prevButton.addActionListener(e -> {
            if (currentPage[0] > 1) {
                currentPage[0]--;
                Object[][] newData = userDAO.getJobListings(currentPage[0], pageSize);
                jobTable.setModel(new DefaultTableModel(newData, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                pageLabel.setText("Page " + currentPage[0]);
            }
        });

        // Next button action.
        nextButton.addActionListener(e -> {
            Object[][] newData = userDAO.getJobListings(currentPage[0] + 1, pageSize);
            if (newData.length > 0) {
                currentPage[0]++;
                jobTable.setModel(new DefaultTableModel(newData, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                pageLabel.setText("Page " + currentPage[0]);
            }
        });

        // RIGHT PANEL: Job Details
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel jobTitleLabel = new JLabel();
        jobTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel companyLabel = new JLabel();
        JLabel locationLabel = new JLabel();
        JLabel jobTypeLabel = new JLabel();
        JLabel dateLabel = new JLabel(); // For Date of Application
        JLabel dueDateLabel = new JLabel(); // For Due Date (if available)
        JTextArea jobDescArea = new JTextArea();
        jobDescArea.setEditable(false);
        jobDescArea.setLineWrap(true);
        jobDescArea.setWrapStyleWord(true);
        jobDescArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane descScroll = new JScrollPane(jobDescArea);
        descScroll.setPreferredSize(new Dimension(300, 150));

        // Create an "Apply here" button.
        JButton applyButton = new JButton("Apply here");
        applyButton.setEnabled(false);

        detailPanel.add(jobTitleLabel);
        detailPanel.add(Box.createVerticalStrut(5));
        detailPanel.add(companyLabel);
        detailPanel.add(Box.createVerticalStrut(5));
        detailPanel.add(locationLabel);
        detailPanel.add(Box.createVerticalStrut(5));
        detailPanel.add(jobTypeLabel);
        detailPanel.add(Box.createVerticalStrut(5));
        detailPanel.add(dateLabel);
        detailPanel.add(Box.createVerticalStrut(5));
        detailPanel.add(dueDateLabel);
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(new JLabel("Job Description:"));
        detailPanel.add(descScroll);
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(applyButton);

        JPanel rightWrapperPanel = new JPanel(new BorderLayout());
        rightWrapperPanel.setBackground(Color.WHITE);
        rightWrapperPanel.add(detailPanel, BorderLayout.CENTER);

        // Update detail panel when a row is selected.
        jobTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jobTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String selectedJobTitle = jobTable.getValueAt(selectedRow, 0).toString();
                    java.util.Map<String, String> details = userDAO.getJobDetail(selectedJobTitle);
                    if (details != null) {
                        jobTitleLabel.setText("Job Title: " + details.get("Job_Title"));
                        companyLabel.setText("Company: " + details.get("Company_name"));
                        locationLabel.setText("Location: " + details.get("Job_location"));
                        jobTypeLabel.setText("Job Type: " + details.get("Job_Type"));
                        dateLabel.setText("Date of Application: " + details.get("Date_Of_Application"));

                        StringBuilder desc = new StringBuilder();
                        desc.append(details.get("Job_Description") != null ? details.get("Job_Description")
                                : "No Description Provided");
                        desc.append("\n\nRequired Experience: "
                                + (details.get("Required_Experience") != null ? details.get("Required_Experience")
                                        : "Not Specified"));
                        jobDescArea.setText(desc.toString());

                        applyButton.setEnabled(true);
                        applyButton.putClientProperty("jobTitle", details.get("Job_Title"));
                    } else {
                        jobTitleLabel.setText("");
                        companyLabel.setText("");
                        locationLabel.setText("");
                        jobTypeLabel.setText("");
                        dateLabel.setText("");
                        dueDateLabel.setText("");
                        jobDescArea.setText("");
                        applyButton.setEnabled(false);
                    }
                }
            }
        });

        // Apply button functionality.
        applyButton.addActionListener(e -> {
            String jobTitle = (String) applyButton.getClientProperty("jobTitle");
            if (jobTitle != null && !jobTitle.isEmpty()) {
                boolean applied = userDAO.applyForJob(jobTitle, loggedInEmail);
                if (applied) {
                    JOptionPane.showMessageDialog(mainPanel, "Application successful!", "Submitted", JOptionPane.INFORMATION_MESSAGE);
                    currentAppPage = 1;
                    refreshApplicationTable();
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Failed to apply.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainPanel, "No job selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightWrapperPanel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(searchBarPanel, BorderLayout.NORTH);
        centerPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Filter button functionality: Open filter dialog and update table data.
        filterButton.addActionListener(e -> {
            JPanel filterPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            JTextField titleField = new JTextField();
            JTextField companyField = new JTextField();
            JTextField locationField = new JTextField();
            JTextField jobTypeField = new JTextField();

            filterPanel.add(new JLabel("Job Title:"));
            filterPanel.add(titleField);
            filterPanel.add(new JLabel("Company Name:"));
            filterPanel.add(companyField);
            filterPanel.add(new JLabel("Job Location:"));
            filterPanel.add(locationField);
            filterPanel.add(new JLabel("Job Type:"));
            filterPanel.add(jobTypeField);

            int result = JOptionPane.showConfirmDialog(mainPanel, filterPanel, "Filter Jobs", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Object[][] filteredData = userDAO.getFilteredJobListings(
                        titleField.getText().trim(), companyField.getText().trim(),
                        locationField.getText().trim(), jobTypeField.getText().trim());
                jobTable.setModel(new DefaultTableModel(filteredData, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                // Reset page number when filtering.
                currentPage[0] = 1;
                pageLabel.setText("Page " + currentPage[0]);
            }
        });

        // Reset Filter button functionality: Reload the current page data.
        resetFilterButton.addActionListener(e -> {
            Object[][] allData = userDAO.getJobListings(currentPage[0], pageSize);
            jobTable.setModel(new DefaultTableModel(allData, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
        });

        return mainPanel;
    }

    private void refreshApplicationTable() {
        UserDAO userDAO = new UserDAO();
        Object[][] updatedData = userDAO.getUserApplications(loggedInEmail, currentAppPage, appPageSize);
        int n = updatedData.length;
        Object[][] newData = new Object[n][7];
        for (int i = 0; i < n; i++) {
            newData[i][0] = updatedData[i][0];
            newData[i][1] = updatedData[i][1];
            newData[i][2] = updatedData[i][2];
            newData[i][3] = updatedData[i][3];
            newData[i][4] = "View";
            newData[i][5] = "Withdraw";
            newData[i][6] = "Resume";
        }
        DefaultTableModel newModel = new DefaultTableModel(newData, appColumnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 4 || col == 5 || col == 6);
            }
        };
        appTable.setModel(newModel);
        appTable.createDefaultColumnsFromModel();
            new ButtonColumn(appTable, viewAction, 4);
            new ButtonColumn(appTable, withdrawAction, 5);
            new ButtonColumn(appTable, resumeAction, 6);
    }

    private void initializeActionListeners() {
        // Define the view action listener
        viewAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String jobTitle = appTable.getValueAt(selectedRow, 1).toString();
                    Map<String, String> details = new UserDAO().getJobDetail(jobTitle);
                    if (details != null) {
                        String info = "Job Title: " + details.get("Job_Title") +
                                "\nCompany: " + details.get("Company_name") +
                                "\nLocation: " + details.get("Job_location") +
                                "\nJob Type: " + details.get("Job_Type") +
                                "\nDescription: " + details.get("Job_Description") +
                                "\nExperience: " + details.get("Required_Experience") +
                                "\nApplied On: " + details.get("Date_Of_Application") +
                                "\nDue Date: N/A"; // or your due date if available
                        JOptionPane.showMessageDialog(null, info, "Job Details", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No details found.", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        };

        // Define the withdraw action listener
        withdrawAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Check if already withdrawn
                    if ("Withdrawn".equals(appTable.getValueAt(selectedRow, 5).toString())) {
                        return;
                    }
                    int appId = Integer.parseInt(appTable.getValueAt(selectedRow, 0).toString());
                    int option = JOptionPane.showConfirmDialog(null,
                            "Do you want to withdraw application ID: " + appId + "?",
                            "Withdraw Application", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        boolean withdrawn = new UserDAO().withdrawApplication(appId, loggedInEmail);
                        if (withdrawn) {
                            JOptionPane.showMessageDialog(null, "Application status updated to Withdrawn.", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            // Refresh the table after status update.
                            refreshApplicationTable();
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to update application status.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };

        // Define the resume action listener
        resumeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int option = JOptionPane.showOptionDialog(null,
                            "Choose an option:", "Resume Options",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[] { "View Resume", "Update Resume" },
                            "View Resume");
                    if (option == 0) { // View Resume
                        byte[] resumeBytes = new UserDAO().getUserResume(loggedInEmail);
                        if (resumeBytes != null && resumeBytes.length > 0) {
                            try {
                                File tempFile = File.createTempFile("resume_", ".pdf");
                                tempFile.deleteOnExit();
                                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                                    fos.write(resumeBytes);
                                }
                                Desktop.getDesktop().open(tempFile);
                            } catch (IOException ioEx) {
                                ioEx.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Error opening resume.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No resume attached.", "Resume",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (option == 1) { // Update Resume
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser
                                .setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
                        int retVal = fileChooser.showOpenDialog(null);
                        if (retVal == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            try (FileInputStream fis = new FileInputStream(file)) {
                                byte[] fileBytes = new byte[(int) file.length()];
                                fis.read(fileBytes);
                                boolean updated = new UserDAO().updateUserResume(loggedInEmail, fileBytes);
                                if (updated) {
                                    JOptionPane.showMessageDialog(null, "Resume updated successfully.", "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Failed to update resume.", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException ioEx) {
                                ioEx.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
    }

    private JPanel createApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(createTitleLabel("Applications"), BorderLayout.NORTH);

        UserDAO userDAO = new UserDAO();
        Object[][] data = userDAO.getUserApplications(loggedInEmail, currentAppPage, appPageSize);
        int n = data.length;
        Object[][] finalData = new Object[n][7];
        for (int i = 0; i < n; i++) {
            finalData[i][0] = data[i][0]; // Application ID
            finalData[i][1] = data[i][1]; // Job Title
            finalData[i][2] = data[i][2]; // Application Date
            finalData[i][3] = data[i][3]; // Status
            finalData[i][4] = "View";
            finalData[i][5] = "Withdraw";
            finalData[i][6] = "Resume";
        }

        DefaultTableModel model = new DefaultTableModel(finalData, appColumnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 4 || col == 5 || col == 6);
            }
        };
        // Create and set up the applications table.
        appTable = new JTable();
        appTable.setModel(model);
        appTable.createDefaultColumnsFromModel();

        appTable.setRowHeight(30);
        appTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        appTable.setGridColor(new Color(189, 195, 199));
        appTable.setShowGrid(true);
        appTable.setIntercellSpacing(new Dimension(5, 5));
        appTable.setBackground(Color.WHITE);
        appTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = appTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(appTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Install ButtonColumn for VIEW (column index 4)
        new ButtonColumn(appTable, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String jobTitle = appTable.getValueAt(selectedRow, 1).toString();
                    Map<String, String> details = userDAO.getJobDetail(jobTitle);
                    if (details != null) {
                        String info = "Job Title: " + details.get("Job_Title") +
                                "\nCompany: " + details.get("Company_name") +
                                "\nLocation: " + details.get("Job_location") +
                                "\nJob Type: " + details.get("Job_Type") +
                                "\nDescription: " + details.get("Job_Description") +
                                "\nExperience: " + details.get("Required_Experience") +
                                "\nApplied On: " + details.get("Date_Of_Application") +
                                "\nDue Date: N/A"; // You may update this if due date exists.
                        JOptionPane.showMessageDialog(panel, info, "Job Details", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(panel, "No details found.", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }, 4);

        // Install ButtonColumn for WITHDRAW (column index 5)
        new ButtonColumn(appTable, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appTable.getSelectedRow();
                if (selectedRow >= 0) {
                    if ("Withdrawn".equals(appTable.getValueAt(selectedRow, 5).toString())) {
                        // Already withdrawn; do nothing.
                        return;
                    }
                    int appId = Integer.parseInt(appTable.getValueAt(selectedRow, 0).toString());
                    int option = JOptionPane.showConfirmDialog(panel,
                            "Do you want to withdraw application ID: " + appId + "?",
                            "Withdraw Application", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        boolean withdrawn = userDAO.withdrawApplication(appId, loggedInEmail);
                        if (withdrawn) {
                            JOptionPane.showMessageDialog(panel, "Application status updated to Withdrawn.", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            // Refresh the application table.
                            refreshApplicationTable();
                        } else {
                            JOptionPane.showMessageDialog(panel, "Failed to update application status.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }, 5);

        // Install ButtonColumn for RESUME (column index 6)
        new ButtonColumn(appTable, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int option = JOptionPane.showOptionDialog(panel,
                            "Choose an option:", "Resume Options",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[] { "View Resume", "Update Resume" },
                            "View Resume");
                    if (option == 0) { // View Resume
                        byte[] resumeBytes = userDAO.getUserResume(loggedInEmail);
                        if (resumeBytes != null && resumeBytes.length > 0) {
                            try {
                                File tempFile = File.createTempFile("resume_", ".pdf");
                                tempFile.deleteOnExit();
                                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                                    fos.write(resumeBytes);
                                }
                                Desktop.getDesktop().open(tempFile);
                            } catch (IOException ioEx) {
                                ioEx.printStackTrace();
                                JOptionPane.showMessageDialog(panel, "Error opening resume.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(panel, "No resume attached.", "Resume",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (option == 1) { // Update Resume
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                        int retVal = fileChooser.showOpenDialog(panel);
                        if (retVal == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            try (FileInputStream fis = new FileInputStream(file)) {
                                byte[] fileBytes = new byte[(int) file.length()];
                                fis.read(fileBytes);
                                boolean updated = userDAO.updateUserResume(loggedInEmail, fileBytes);
                                if (updated) {
                                    JOptionPane.showMessageDialog(panel, "Resume updated successfully.", "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(panel, "Failed to update resume.", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException ioEx) {
                                ioEx.printStackTrace();
                            }
                        }
                    }
                }
            }
        }, 6);

        // Pagination controls for the Applications panel.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        JButton prevButton = new JButton("Prev");
        JButton nextButton = new JButton("Next");
        final JLabel pageLabel = new JLabel("Page " + currentAppPage);
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAppPage > 1) {
                    currentAppPage--;
                    refreshApplicationTable();
                    pageLabel.setText("Page " + currentAppPage);
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[][] nextData = userDAO.getUserApplications(loggedInEmail, currentAppPage + 1, appPageSize);
                if (nextData.length > 0) {
                    currentAppPage++;
                    refreshApplicationTable();
                    pageLabel.setText("Page " + currentAppPage);
                }
            }
        });
        bottomPanel.add(prevButton);
        bottomPanel.add(pageLabel);
        bottomPanel.add(nextButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Refresh the table when the panel is shown
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshApplicationTable();
            }
        });

        return panel;
    }

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

    private JPanel createProfileFormPanel() {
        // Create a new panel with a GridBagLayout.
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        // Create and configure the grid bag constraints.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Row 0: First Name and Last Name ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblFirstName = new JLabel("<html>First Name <font color='red'>*</font>:</html>");
        formPanel.add(lblFirstName, gbc);

        gbc.gridx = 1;
        // (Make sure txtFirstName has been initialized in the constructor.)
        formPanel.add(txtFirstName, gbc);

        gbc.gridx = 2;
        JLabel lblLastName = new JLabel("<html>Last Name <font color='red'>*</font>:</html>");
        formPanel.add(lblLastName, gbc);

        gbc.gridx = 3;
        // (Make sure txtLastName is not null.)
        formPanel.add(txtLastName, gbc);

        // --- Row 1: Email and Phone ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblEmail = new JLabel("<html>Email <font color='red'>*</font>:</html>");
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 2;
        JLabel lblPhone = new JLabel("<html>Phone <font color='red'>*</font>:</html>");
        formPanel.add(lblPhone, gbc);

        gbc.gridx = 3;
        formPanel.add(txtPhone, gbc);

        // --- Row 2: Current Company and Job Role ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblCompany = new JLabel("<html>Current Company <font color='red'>*</font>:</html>");
        formPanel.add(lblCompany, gbc);

        gbc.gridx = 1;
        formPanel.add(txtCompany, gbc);

        gbc.gridx = 2;
        JLabel lblJobRole = new JLabel("<html>Job Role <font color='red'>*</font>:</html>");
        formPanel.add(lblJobRole, gbc);

        gbc.gridx = 3;
        formPanel.add(txtJobRole, gbc);

        // --- Row 3: Preferences / Skills ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPreferences = new JLabel("Preferences / Skills:");
        formPanel.add(lblPreferences, gbc);

        gbc.gridx = 1;
        formPanel.add(txtPreferences, gbc);

        // --- Row 4: LinkedIn URL ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblLinkedIn = new JLabel("LinkedIn URL:");
        formPanel.add(lblLinkedIn, gbc);

        gbc.gridx = 1;
        formPanel.add(txtLinkedIn, gbc);

        // --- Row 5: Availability ---
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblAvailability = new JLabel("Availability:");
        formPanel.add(lblAvailability, gbc);

        gbc.gridx = 1;
        formPanel.add(txtAvailability, gbc);

        // --- Row 6: Resume Field ---
        // Call a helper function to add the resume-related components.
        addResumeField(formPanel, gbc);

        return formPanel;
    }

    private void addResumeField(JPanel formPanel, GridBagConstraints gbc) {
        // Row for the "Resume (Optional):" label.
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblResumeOptional = new JLabel("Resume (Optional):");
        formPanel.add(lblResumeOptional, gbc);

        // Row for the resume button panel (upload and delete buttons).
        gbc.gridx = 1;
        JPanel resumeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        resumeButtonPanel.setOpaque(false);
        JButton uploadResumeButton = new JButton("Upload");
        JButton deleteResumeButton = new JButton("Delete");
        deleteResumeButton.setVisible(false); // Initially hidden.
        resumeButtonPanel.add(uploadResumeButton);
        resumeButtonPanel.add(deleteResumeButton);
        formPanel.add(resumeButtonPanel, gbc);

        // Row for the label that displays the resume file name.
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblResume = new JLabel("Resume:");
        formPanel.add(lblResume, gbc);

        gbc.gridx = 1;
        // Create the resume file name label.
        resumeFileNameLabel = new JLabel();
        resumeFileNameLabel.setForeground(Color.BLUE); // Typical hyperlink color.
        resumeFileNameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resumeFileNameLabel.setText("<html><u>No file selected</u></html>");
        // Add a mouse listener to allow viewing the resume.
        resumeFileNameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                downloadResume();
            }
        });
        formPanel.add(resumeFileNameLabel, gbc);

        // Action for the upload button.
        uploadResumeButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File resumeFile = fileChooser.getSelectedFile();
                if (resumeFile != null) {
                    byte[] resumeBytes = fileToByteArray(resumeFile);
                    if (resumeBytes != null) {
                        boolean updated = new UserDAO().updateUserResume(loggedInEmail, resumeBytes);
                        if (updated) {
                            String fileName = resumeFile.getName();
                            resumeFileNameLabel.setText("<html><u>" + fileName + "</u></html>");
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update resume in database.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // (Optional) Action for the delete button can be added here.
        deleteResumeButton.addActionListener(e -> {
            // For example, you might update the database to delete the resume.
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
                Dimension size = profilePicLabel.getSize();
                if (size.width == 0 || size.height == 0) {
                    size = new Dimension(120, 120);
                }
                Image image = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
                profilePicLabel.setIcon(new ImageIcon(image));
                profilePicLabel.setText("");
                removeButton.setVisible(true);
            }

            // Load resume.
            byte[] resumeBytes = userDAO.getUserResume(loggedInEmail);
            if (resumeBytes != null && resumeBytes.length > 0) {
                resumeFileNameLabel.setText("<html><u>View Resume</u></html>");
            } else {
                resumeFileNameLabel.setText("<html><u>No file selected</u></html>");
            }
        } else {
            JOptionPane.showMessageDialog(this, "User data not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
