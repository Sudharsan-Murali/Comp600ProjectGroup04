package com.group04.GUI.Recruiter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import javax.swing.table.TableCellEditor;
import java.awt.event.*;

import java.util.List;
import com.group04.DAO.UserDAO;
import com.group04.GUI.Components.ButtonEditor;
import com.group04.GUI.Components.ButtonFactory;
import com.group04.GUI.Components.ButtonRenderer;

public class RecruiterProfileScreen {

    private JLabel profilePicLabel;
    private JPanel rightPanel;
    private JButton uploadButton, deleteButton;
    private JFrame frame;
    private JLabel pageLabel;
    private int currentPage = 1;
    private boolean isEditMode = false;
    private int editingJobId = -1; // We'll use this to pass the actual DB Job_ID
    private final int pageSize = 20;    
    private int totalPages = 1;

    private String recruiterEmail;
    private Map<String, String> recruiterData;

    // Editable fields
    private JTextField companyNameField;
    private JTextField companyAddressField;
    private JTextField companyPhoneField;
    private JTextField companyLinkedInField;
    private JTextField companyWebsiteField;

    // ADD JOB POST SCREEN
    private JTextField jobIdField;
    private JTextField jobTitleField;
    private JTextField minSalaryField; // Inside RecruiterProfileScreen class

    public class ButtonEditor extends DefaultCellEditor {
        public interface ButtonClickListener {
            void onClick(int row);
        }

        private final JButton button;
        private final JTable table;
        private final ButtonClickListener clickListener;

        public ButtonEditor(Icon icon, JTable table, ButtonClickListener listener) {
            super(new JTextField());
            this.table = table;
            this.clickListener = listener;
            this.button = new JButton(icon);
            setupButton();
        }

        private void setupButton() {
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(true);
            button.addActionListener(e -> {
                int row = table.getEditingRow();
                fireEditingStopped();
                clickListener.onClick(row);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    };

    private JTextField maxSalaryField;
    private JTextArea jobDescField;
    private JTextField jobLocationField;
    private JComboBox<String> jobTypeComboBox;

    private int userID;
    private int companyId;

    public RecruiterProfileScreen(String email) {
        this.recruiterEmail = email;

        UserDAO userDAO = new UserDAO();
        this.recruiterData = userDAO.getRecruiterInfoByEmail(recruiterEmail);
        userID = userDAO.getUserIdByEmail(recruiterEmail);
        companyId = userDAO.getCompanyIdByEmail(recruiterEmail); // You’ll implement this method

    }

    public void createAndShowGUI() {
        frame = new JFrame("Recruiter Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set full-screen view
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize);
        frame.setLocationRelativeTo(null);

        // Use BorderLayout with gaps for main container
        frame.setLayout(new BorderLayout(20, 20));

        // Left panel: Side Navigation with updated attractive UI
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(180, frame.getHeight()));
        // Updated background color and matte border
        leftPanel.setBackground(new Color(44, 62, 80));
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        // Create side panel buttons using ButtonFactory
        JButton profileButton = ButtonFactory.createSideButton("Recruiter Profile");
        JButton jobpostsButton = ButtonFactory.createSideButton("Job Posts");
        JButton addjobpostsButton = ButtonFactory.createSideButton("Add Job Posts");
        JButton applicationButton = ButtonFactory.createSideButton("Applications");
        JButton logoutButton = ButtonFactory.createSideButton("Logout");

        // Update button fonts and colors for contrast on dark background
        Font navFont = new Font("SansSerif", Font.BOLD, 14);
        profileButton.setFont(navFont);
        jobpostsButton.setFont(navFont);
        addjobpostsButton.setFont(navFont);
        applicationButton.setFont(navFont);
        logoutButton.setFont(navFont);
        profileButton.setForeground(Color.WHITE);
        jobpostsButton.setForeground(Color.WHITE);
        addjobpostsButton.setForeground(Color.WHITE);
        applicationButton.setForeground(Color.WHITE);
        logoutButton.setForeground(Color.WHITE);

        // Set action listeners for navigation buttons
        profileButton.addActionListener(e -> showProfileScreen());
        jobpostsButton.addActionListener(e -> {
            currentPage = 1; // <-- This resets to the first page
            showJobPostsScreen(); // <-- Then shows the Job Posts
        });
        addjobpostsButton.addActionListener(e -> showAddJobPostsScreen());
        applicationButton.addActionListener(e -> showApplicationScreen());
        logoutButton.addActionListener(e -> {
            frame.dispose(); // Close recruiter dashboard
            // Uncomment below to reopen login screen:
            // SwingUtilities.invokeLater(() -> new JobPortalApplication());
        });

        // Arrange buttons in left panel with spacing
        leftPanel.add(profileButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(jobpostsButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(addjobpostsButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(applicationButton);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(logoutButton);

        frame.add(leftPanel, BorderLayout.WEST);

        // Right Panel: Content area
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.setBackground(Color.WHITE);
        frame.add(rightPanel, BorderLayout.CENTER);

        // Initially show the Profile Screen
        showProfileScreen();

        frame.setVisible(true);
    }

    // ----------------------------
    // Helper: Create a uniform title label (left-aligned)
    private JLabel createTitleLabel(String text) {
        JLabel titleLabel = new JLabel(text, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return titleLabel;
    }

    // ----------------------------
    // Updated: Show Profile Screen with Uniform Layout and Labels with Colon and
    // Red Asterisk
    private void showProfileScreen() {
        uploadButton = new JButton("Upload");
        uploadButton.setPreferredSize(new Dimension(100, 30));

        deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setVisible(false); // Start hidden

        rightPanel.removeAll();
        // Use a vertical BoxLayout for the rightPanel with a modest empty border.
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Profile Picture Section
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.setOpaque(false); // Let the parent's background show

        // Panel to hold the profile picture itself
        JPanel profilePicPanel = new JPanel(new BorderLayout());
        profilePicPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePicPanel.setPreferredSize(new Dimension(150, 150));
        profilePicPanel.setMaximumSize(new Dimension(150, 150));
        profilePicPanel.setOpaque(false);

        profilePicLabel = new JLabel("Add Profile Picture", SwingConstants.CENTER);
        profilePicLabel.setPreferredSize(new Dimension(120, 120));
        // profilePicLabel.setMinimumSize(new Dimension(120, 120));
        profilePicLabel.setMaximumSize(new Dimension(120, 120));
        // profilePicLabel.setSize(new Dimension(120, 120));
        profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        profilePicLabel.setOpaque(true);
        profilePicLabel.setBackground(Color.LIGHT_GRAY);

        // Load profile picture from DB (if exists)
        UserDAO userDAO = new UserDAO();
        byte[] imageBytes = userDAO.getRecruiterProfilePicture(recruiterEmail);
        if (imageBytes != null && imageBytes.length > 0) {
            ImageIcon originalIcon = new ImageIcon(imageBytes);
            int width = profilePicLabel.getWidth() > 0 ? profilePicLabel.getWidth() : 150;
            int height = profilePicLabel.getHeight() > 0 ? profilePicLabel.getHeight() : 150;
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            profilePicLabel.setIcon(new ImageIcon(scaledImage));
            profilePicLabel.setText(""); // Clear the text
            deleteButton.setVisible(true); // Show delete if there's an image
        }

        profilePicPanel.add(profilePicLabel, BorderLayout.CENTER);

        // Panel for the Upload/Delete buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setOpaque(false);
        uploadButton = new JButton("Upload");
        uploadButton.setPreferredSize(new Dimension(100, 30));
        deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setVisible(false);

        buttonPanel.add(uploadButton);
        buttonPanel.add(deleteButton);

        profilePanel.add(profilePicPanel);
        // Reduced vertical gap: changed from 5 to 2 pixels
        profilePanel.add(Box.createVerticalStrut(2));
        profilePanel.add(buttonPanel);

        // Action listeners
        uploadButton.addActionListener(e -> uploadProfilePicture());
        deleteButton.addActionListener(e -> removeProfilePicture());

        // Form Panel (Recruiter-specific information)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Smaller insets for reduced spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {
                "First Name", "Last Name", "Email", "Company Name",
                "Company Address", "Company Phone", "Company LinkedIn", "Company Website"
        };

        String[] keys = {
                "First Name", "Last Name", "Email", "Company Name",
                "Company Address", "Company Phone", "Company LinkedIn", "Company Website"
        };

        // Define which fields should be non-editable
        String[] nonEditableFields = { "First Name", "Last Name", "Email", "Company Phone" };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(
                    "<html>" + labels[i] + " <font color='black'>:</font> <font color='red'>*</font></html>");
            label.setFont(new Font("SansSerif", Font.BOLD, 16));

            JTextField textField = new JTextField(25);
            textField.setFont(new Font("SansSerif", Font.PLAIN, 16));
            textField.setPreferredSize(new Dimension(250, 30));

            // Autofill using HashMap values
            String value = recruiterData.getOrDefault(keys[i], "");
            textField.setText(value);

            // Store references for editable fields only
            switch (labels[i]) {
                case "Company Name":
                    companyNameField = textField;
                    break;
                case "Company Address":
                    companyAddressField = textField;
                    break;
                case "Company Phone":
                    companyPhoneField = textField;
                    break;
                case "Company LinkedIn":
                    companyLinkedInField = textField;
                    break;
                case "Company Website":
                    companyWebsiteField = textField;
                    break;
                default:
                    textField.setEditable(false); // First Name, Last Name, Email
            }

            // Set non-editable if it's in the restricted list
            for (String nonEditable : nonEditableFields) {
                if (labels[i].equals(nonEditable)) {
                    textField.setEditable(false);
                    textField.setBackground(new Color(230, 230, 230)); // Optional: greyed out
                    break;
                }
            }

            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(label, gbc);

            gbc.gridx = 1;
            formPanel.add(textField, gbc);
        }

        // Save Button Section with reduced vertical gap inside the save panel
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        savePanel.setOpaque(false);
        JButton saveButton = new JButton("SAVE");
        saveButton.setPreferredSize(new Dimension(100, 30));
        savePanel.add(saveButton);

        saveButton.addActionListener(e -> saveRecruiterProfile());

        // Combine profile picture and form in a parent panel with reduced gaps
        JPanel parentPanel = new JPanel();
        parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
        parentPanel.setOpaque(false);

        parentPanel.add(profilePanel);
        // Reduced gap between sections: changed from 1 to 0 pixels (or minimal gap)
        parentPanel.add(Box.createVerticalStrut(0));
        parentPanel.add(formPanel);
        // Reduced gap above the SAVE button: changed from 8 to 2 pixels
        parentPanel.add(Box.createVerticalStrut(2));
        parentPanel.add(savePanel);

        rightPanel.add(parentPanel, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private void saveRecruiterProfile() {
        // Read updated values from fields
        String updatedCompanyName = companyNameField.getText().trim();
        String updatedCompanyAddress = companyAddressField.getText().trim();
        String updatedCompanyPhone = companyPhoneField.getText().trim();
        String updatedLinkedIn = companyLinkedInField.getText().trim();
        String updatedWebsite = companyWebsiteField.getText().trim();

        // Update the database
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateRecruiterProfile(
                recruiterEmail,
                updatedCompanyName,
                updatedCompanyAddress,
                updatedCompanyPhone,
                updatedLinkedIn,
                updatedWebsite);

        if (success) {
            JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to update profile.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ----------------------------

    // Helper method to add an individual form field (if needed elsewhere)
    private void addFormField(JPanel panel, String labelText, int row, int col, GridBagConstraints gbc) {
        // Label with colon in black and a required asterisk in red
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
        // 2. Get job posts
        private void showJobPostsScreen() {
        
            rightPanel.removeAll();
            rightPanel.setLayout(new BorderLayout());
            rightPanel.setBackground(Color.WHITE);
    
            UserDAO dao = new UserDAO();
    int totalJobs = dao.countJobPostsByUserId(userID);
    totalPages = (int) Math.ceil((double) totalJobs / pageSize);
    
            ImageIcon editIcon = null;
            ImageIcon deleteIcon = null;
    
            try {
                editIcon = loadScaledIcon("/icons/edit.png", 20, 20);
                deleteIcon = loadScaledIcon("/icons/delete.png", 20, 20);
            } catch (Exception e) {
                System.err.println("Icon load failed: " + e.getMessage());
            }
    
            // 1. Add Header (with ADD button)
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(Color.WHITE);
            JLabel titleLabel = new JLabel("Job Posts");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            headerPanel.add(titleLabel, BorderLayout.WEST);
    
            JButton addJobButton = new JButton("ADD");
            addJobButton.addActionListener(e -> showAddJobPostsScreen());
    
            JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            addButtonPanel.setBackground(Color.WHITE);
            addButtonPanel.add(addJobButton);
            headerPanel.add(addButtonPanel, BorderLayout.EAST);
    
            rightPanel.add(headerPanel, BorderLayout.NORTH);
    
            // 2. Get job posts
            List<Map<String, Object>> jobPosts = dao.getJobPostsByUserId(userID, currentPage, pageSize);
    
            if (jobPosts.isEmpty()) {
                JLabel noDataLabel = new JLabel("No Data Found", JLabel.CENTER);
                noDataLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
                noDataLabel.setForeground(Color.GRAY);
                rightPanel.add(noDataLabel, BorderLayout.CENTER);
            } else {
                // 3. Display job posts in table
                String[] columnNames = { "S.NO", "JOB ID", "JOB TITLE", "DATE POSTED", "LOCATION", "EDIT", "DELETE" };
                Object[][] data = new Object[jobPosts.size()][7];
    
                for (int i = 0; i < jobPosts.size(); i++) {
                    Map<String, Object> row = jobPosts.get(i);
                    data[i][0] = i + 1;
                    data[i][1] = row.get("Job_ID");
                    data[i][2] = row.get("Job_Title");
                    data[i][3] = row.get("Date_Of_Application");
                    data[i][4] = row.get("Job_location");
                    data[i][5] = "";
                    data[i][6] = "";
                }
    
                JTable jobTable = new JTable(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        // Only allow edit/delete columns to be editable
                        return column == 5 || column == 6;
                    }
    
                    @Override
                    public Class<?> getColumnClass(int column) {
                        return getValueAt(0, column) != null ? getValueAt(0, column).getClass() : Object.class;
                    }
                };
    
                jobTable.setRowHeight(30);
                jobTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
                jobTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
    
                jobTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer(editIcon));
                jobTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(editIcon, jobTable, row -> {
                    Map<String, Object> selectedJob = jobPosts.get(row);
                    showAddJobPostsScreen(selectedJob);
                }));
    
                jobTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer(deleteIcon));
                // jobTable.getColumnModel().getColumn(5).setCellEditor(new
                // ButtonEditor(deleteIcon, jobTable, row -> {
                // System.out.println("Delete clicked for row " + row);
                // }));
                jobTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(deleteIcon, jobTable, row -> {
                    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this job post?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Object jobIdObj = jobTable.getValueAt(row, 1); // Get Job_ID from hidden column
                        int jobId = Integer.parseInt(jobIdObj.toString());
    
                       // UserDAO dao = new UserDAO();
                        boolean deleted = dao.deleteJobPostById(jobId, userID);
    
                        if (deleted) {
                            JOptionPane.showMessageDialog(frame, "Job post deleted successfully.");
                            showJobPostsScreen(); // Refresh table
                        } else {
                            JOptionPane.showMessageDialog(frame, "Failed to delete job post.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }));
    
                jobTable.setCellSelectionEnabled(true);
                jobTable.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        Point point = e.getPoint();
                        int row = jobTable.rowAtPoint(point);
                        int column = jobTable.columnAtPoint(point);
    
                        if (row >= 0 && column >= 0 && jobTable.isCellEditable(row, column)) {
                            jobTable.editCellAt(row, column);
                            Component editor = jobTable.getEditorComponent();
                            if (editor != null) {
                                editor.requestFocus();
                            }
                        }
                    }
                });
    
                JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton prevButton = new JButton("<");
                JButton nextButton = new JButton(">");
                pageLabel = new JLabel("Page " + currentPage + " of " + totalPages);
    
                updatePaginationButtons(prevButton, nextButton);
    
    
            prevButton.addActionListener(e -> {
            if (currentPage > 1) {
            currentPage--;
            showJobPostsScreen(); // reloads data for previous page
        }
    });
    
    nextButton.addActionListener(e -> {
        if (currentPage < totalPages) {
            currentPage++;
            showJobPostsScreen(); // reloads data for next page
        }
    });
    JScrollPane scrollPane = new JScrollPane(jobTable);
    paginationPanel.add(prevButton);
    paginationPanel.add(pageLabel);
    paginationPanel.add(nextButton);
    
    
    rightPanel.add(scrollPane, BorderLayout.CENTER);
    rightPanel.add(paginationPanel, BorderLayout.SOUTH);
    
            }
    
            rightPanel.revalidate();
            rightPanel.repaint();
    
           
               }
    
               private void updatePaginationButtons(JButton prev, JButton next) {
                prev.setEnabled(currentPage > 1);
                next.setEnabled(currentPage < totalPages);
            }

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("❌ Icon not found at: " + path);
                return null;
            }
            Image original = new ImageIcon(url).getImage();
            Image scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ----------------------------
    // Uniform Add Job Posts Screen
    private void showAddJobPostsScreen(Map<String, Object> jobData) {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        // Create and add a uniform title label at the top.
        JLabel titleLabel = createTitleLabel("Add Job Post");
        rightPanel.add(titleLabel, BorderLayout.NORTH);

        // Create the form panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // JOB ID field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel jobIdLabel = new JLabel("<html>Job ID <font color='black'>:</font> <font color='red'>*</font></html>");
        jobIdLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobIdLabel, gbc);
        // gbc.gridx = 1;
        // formPanel.add(new JTextField(15), gbc);
        gbc.gridx = 1;
        jobIdField = new JTextField(15);
        jobIdField.setEditable(false); // Disable editing
        jobIdField.setBackground(new Color(230, 230, 230)); // Optional: greyed out background

        int generatedId = new UserDAO().getNextGlobalJobId();
        String genJobID = "JID0" + String.valueOf(generatedId);
        jobIdField.setText(genJobID);

        formPanel.add(jobIdField, gbc);

        // JOB TITLE field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel jobTitleLabel = new JLabel(
                "<html>Job Title <font color='black'>:</font> <font color='red'>*</font></html>");
        jobTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobTitleLabel, gbc);
        gbc.gridx = 1;
        jobTitleField = new JTextField(15);
        formPanel.add(jobTitleField, gbc);

        // JOB TYPE field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel jobTypeLabel = new JLabel(
                "<html>Job Type <font color='black'>:</font> <font color='red'>*</font></html>");
        jobTypeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobTypeLabel, gbc);
        gbc.gridx = 1;
        jobTypeComboBox = new JComboBox<>(new String[] { "Onsite", "Remote", "Hybrid" });
        formPanel.add(jobTypeComboBox, gbc);

        // SALARY RANGE field
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel salaryRangeLabel = new JLabel(
                "<html>Salary Range <font color='black'>:</font> <font color='red'>*</font></html>");
        salaryRangeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(salaryRangeLabel, gbc);
        gbc.gridx = 1;
        minSalaryField = new JTextField(7);
        formPanel.add(minSalaryField, gbc);
        gbc.gridx = 2;
        // "TO:" separator without required marker
        formPanel.add(new JLabel("<html>To <font color='black'>:</font> <font color='red'>*</font></html>"), gbc);
        gbc.gridx = 3;
        maxSalaryField = new JTextField(7);
        formPanel.add(maxSalaryField, gbc);

        // JOB DESCRIPTION field
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel jobDescLabel = new JLabel(
                "<html>Job Description <font color='black'>:</font> <font color='red'>*</font></html>");
        jobDescLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobDescLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        jobDescField = new JTextArea(4, 30);
        formPanel.add(new JScrollPane(jobDescField), gbc);
        gbc.gridwidth = 1; // Reset gridwidth and fill

        // REQUIRED EXPERIENCE field
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel experienceLabel = new JLabel(
                "<html>Required Experience <font color='black'>:</font> <font color='red'>*</font></html>");
        experienceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(experienceLabel, gbc);

        gbc.gridx = 1;
        JTextField experienceField = new JTextField(15); // Store this as a class-level variable if needed
        formPanel.add(experienceField, gbc);

        // JOB LOCATION field
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel jobLocationLabel = new JLabel(
                "<html>Job Location <font color='black'>:</font> <font color='red'>*</font></html>");
        jobLocationLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobLocationLabel, gbc);
        gbc.gridx = 1;
        jobLocationField = new JTextField(15);
        formPanel.add(jobLocationField, gbc);

        // Save Button Section
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("SAVE");
        saveButton.setPreferredSize(new Dimension(100, 30));

        saveButton.addActionListener(e -> {
            try {
                String jobTitle = jobTitleField.getText().trim();

                String selectedType = jobTypeComboBox.getSelectedItem().toString();
                int jobTypeId = switch (selectedType) {
                    case "Onsite" -> 1;
                    case "Remote" -> 2;
                    case "Hybrid" -> 3;
                    default -> 0; // or throw error
                };

                double minSalary = Double.parseDouble(minSalaryField.getText().trim());
                double maxSalary = Double.parseDouble(maxSalaryField.getText().trim());
                String jobDesc = jobDescField.getText().trim();
                String jobLocation = jobLocationField.getText().trim();

                // Optional: basic validation
                if (jobTitle.isEmpty() || jobDesc.isEmpty() || jobLocation.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all required fields.", "Missing Data",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String requiredExperience = experienceField.getText().trim();
                Date dateOfApplication = Date.valueOf(LocalDate.now());

                boolean success = new UserDAO().addJobPost(
                        userID, companyId, jobTitle, jobTypeId,
                        minSalary, maxSalary, jobDesc,
                        jobLocation, requiredExperience, dateOfApplication);

                if (success) {
                    JOptionPane.showMessageDialog(frame, "Job Post Added Successfully!");
                    currentPage=1;
                    showJobPostsScreen(); // refresh the job post list and reloads table dynamically
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add job post.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid salary input. Please enter numeric values.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Unexpected error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(saveButton, gbc);

        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();

        if (jobData != null) {
            jobIdField.setText(jobData.get("Job_ID").toString());
            jobTitleField.setText((String) jobData.get("Job_Title"));
            minSalaryField.setText(jobData.get("Min_Salary").toString());
            maxSalaryField.setText(jobData.get("Max_Salary").toString());
            jobDescField.setText((String) jobData.get("Job_Description"));
            jobLocationField.setText((String) jobData.get("Job_location"));
            experienceField.setText((String) jobData.get("Required_Experience"));

            int jobType = Integer.parseInt(jobData.get("Job_Type_ID").toString());
            switch (jobType) {
                case 1 -> jobTypeComboBox.setSelectedItem("Onsite");
                case 2 -> jobTypeComboBox.setSelectedItem("Remote");
                case 3 -> jobTypeComboBox.setSelectedItem("Hybrid");
            }
        }
    }

    private void showAddJobPostsScreen() {
        showAddJobPostsScreen(null); // Just call with null for new post
    }

    // ----------------------------

    // Uniform Applications Screen
    private void showApplicationScreen() {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JLabel titleLabel = createTitleLabel("Applications");
        rightPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = { "APP NO", "JOB ID", "JOB TITLE", "APPLICANT NAME", "CONTACT", "VIEW JOB", "VIEW APPL",
                "STS" };
        Object[][] data = new Object[10][8]; // Placeholder data
        JTable applicationTable = new JTable(data, columnNames);
        applicationTable.setRowHeight(30);
        applicationTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        applicationTable.setGridColor(new Color(189, 195, 199));
        applicationTable.setShowGrid(true);
        applicationTable.setIntercellSpacing(new Dimension(5, 5));

        JTableHeader header = applicationTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);

        TableColumnModel columnModel = applicationTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(80);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(7).setPreferredWidth(50);

        // Extend columns to fill available width.
        applicationTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane tableScrollPane = new JScrollPane(applicationTable);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(Color.WHITE);
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        pageLabel = new JLabel("Page " + currentPage);
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                showJobPostsScreen();
                updatePageLabel();
            }
        });
        nextButton.addActionListener(e -> {
            currentPage++;
            showJobPostsScreen();
            updatePageLabel();
        });
        footerPanel.add(prevButton);
        footerPanel.add(pageLabel);
        footerPanel.add(nextButton);

        rightPanel.add(tableScrollPane, BorderLayout.CENTER);
        rightPanel.add(footerPanel, BorderLayout.SOUTH);

        rightPanel.revalidate();
        rightPanel.repaint();
    }
    // ----------------------------

    private void updatePageLabel() {
        pageLabel.setText("Page " + currentPage);
    }

    private void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));

        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.exists()) {
                try {
                    // Display scaled image
                    ImageIcon originalIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image scaledImage = originalIcon.getImage().getScaledInstance(
                            120, 120, Image.SCALE_SMOOTH);

                    profilePicLabel.setIcon(new ImageIcon(scaledImage));
                    profilePicLabel.setText("");
                    deleteButton.setVisible(true);

                    // Save to database
                    byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                    UserDAO userDAO = new UserDAO();
                    boolean success = userDAO.updateRecruiterProfilePicture(recruiterEmail, imageBytes);

                    if (!success) {
                        JOptionPane.showMessageDialog(frame, "Failed to save profile picture to database.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error loading image.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "File does not exist.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeProfilePicture() {
        profilePicLabel.setIcon(null);
        profilePicLabel.setText("Add Profile Picture");
        deleteButton.setVisible(false);
    }
}