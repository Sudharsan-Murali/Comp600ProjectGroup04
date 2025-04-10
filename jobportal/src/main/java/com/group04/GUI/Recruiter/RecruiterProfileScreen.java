package com.group04.GUI.Recruiter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import com.group04.GUI.JobPortalApplication; // For handling the logout action
import com.group04.GUI.User.Components.UIUtils;
import com.group04.GUI.User.Components.ButtonFactory;
import com.group04.GUI.User.Components.SidePanel;
import com.group04.GUI.User.Components.UIConstants;

public class RecruiterProfileScreen {

    private JLabel profilePicLabel;
    private JPanel rightPanel;
    private JButton uploadButton, deleteButton;
    private JFrame frame;
    private JLabel pageLabel; 
    private int currentPage = 1;

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
        jobpostsButton.addActionListener(e -> showJobPostsScreen());
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
    // Updated: Show Profile Screen with Uniform Layout and Labels with Colon and Red Asterisk
    private void showProfileScreen() {
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
        profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        profilePicLabel.setOpaque(true);
        profilePicLabel.setBackground(Color.LIGHT_GRAY);
    
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
            "First Name", 
            "Last Name", 
            "Email", 
            "Company Name", 
            "Company Address", 
            "Company Phone", 
            "Company LinkedIn", 
            "Company Website" 
        };
    
        for (int i = 0; i < labels.length; i++) {
            // Create label with colon in black and a red asterisk
            JLabel label = new JLabel("<html>" + labels[i] + " <font color='black'>:</font> <font color='red'>*</font></html>");
            label.setFont(new Font("SansSerif", Font.BOLD, 16));
            
            // Create text field with increased size and font
            JTextField textField = new JTextField(25);
            textField.setFont(new Font("SansSerif", Font.PLAIN, 16));
            textField.setPreferredSize(new Dimension(250, 30));
            
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(label, gbc); // Label on left
            
            gbc.gridx = 1;
            formPanel.add(textField, gbc); // Text field on right
        }
    
        // Save Button Section with reduced vertical gap inside the save panel
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        savePanel.setOpaque(false);
        JButton saveButton = new JButton("SAVE");
        saveButton.setPreferredSize(new Dimension(100, 30));
        savePanel.add(saveButton);
    
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
    // ----------------------------
    
    // Helper method to add an individual form field (if needed elsewhere)
    private void addFormField(JPanel panel, String labelText, int row, int col, GridBagConstraints gbc) {
        // Label with colon in black and a required asterisk in red
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
    
    // ----------------------------
    // Uniform Job Posts Screen
    private void showJobPostsScreen() {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
    
        // Create a header panel combining a left-aligned title and the "ADD" button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Job Posts");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);
    
        JButton addJobButton = new JButton("ADD");
        // Add an ActionListener to redirect to the Add Job Post screen when clicked.
        addJobButton.addActionListener(e -> showAddJobPostsScreen());
        
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButtonPanel.setBackground(Color.WHITE);
        addButtonPanel.add(addJobButton);
        headerPanel.add(addButtonPanel, BorderLayout.EAST);
    
        // Job Table: styling similar to the Applications screen
        String[] columnNames = {"S.NO", "JOB TITLE", "DATE POSTED", "NUMBER", "STATUS", "EDIT", "DELETE", "DUE DATE"};
        Object[][] data = new Object[10][8]; // Placeholder data
        JTable jobTable = new JTable(data, columnNames);
        jobTable.setRowHeight(30);
        jobTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jobTable.setGridColor(new Color(189, 195, 199));
        jobTable.setShowGrid(true);
        jobTable.setIntercellSpacing(new Dimension(5, 5));
        jobTable.setBackground(Color.WHITE);
        
        // Style the table header
        JTableHeader header = jobTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        
        // Set proper column widths
        TableColumnModel columnModel = jobTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(70);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(80);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(7).setPreferredWidth(50);
        
        // Extend columns to fill available width.
        jobTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane tableScrollPane = new JScrollPane(jobTable);
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Footer for pagination
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(Color.WHITE);
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        pageLabel = new JLabel("Page " + currentPage);
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                updatePageLabel();
            }
        });
        nextButton.addActionListener(e -> {
            currentPage++;
            updatePageLabel();
        });
        footerPanel.add(prevButton);
        footerPanel.add(pageLabel);
        footerPanel.add(nextButton);
        
        rightPanel.add(headerPanel, BorderLayout.NORTH);
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);
        rightPanel.add(footerPanel, BorderLayout.SOUTH);
        
        rightPanel.revalidate();
        rightPanel.repaint();
    }    
    // ----------------------------
    
    // Uniform Add Job Posts Screen
    private void showAddJobPostsScreen() {
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
        JLabel jobIdLabel = new JLabel("<html>JOB ID <font color='black'>:</font> <font color='red'>*</font></html>");
        jobIdLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobIdLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(new JTextField(15), gbc);
    
        // JOB TITLE field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel jobTitleLabel = new JLabel("<html>JOB TITLE <font color='black'>:</font> <font color='red'>*</font></html>");
        jobTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobTitleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(new JTextField(15), gbc);
    
        // JOB TYPE field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel jobTypeLabel = new JLabel("<html>JOB TYPE <font color='black'>:</font> <font color='red'>*</font></html>");
        jobTypeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobTypeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(new JTextField(15), gbc);
    
        // SALARY RANGE field
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel salaryRangeLabel = new JLabel("<html>SALARY RANGE <font color='black'>:</font> <font color='red'>*</font></html>");
        salaryRangeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(salaryRangeLabel, gbc);
        gbc.gridx = 1;
        JTextField minSalaryField = new JTextField(7);
        formPanel.add(minSalaryField, gbc);
        gbc.gridx = 2;
        // "TO:" separator without required marker
        formPanel.add(new JLabel("<html>TO <font color='black'>:</font> <font color='red'>*</font></html>"), gbc);
        gbc.gridx = 3;
        JTextField maxSalaryField = new JTextField(7);
        formPanel.add(maxSalaryField, gbc);
    
        // JOB DESCRIPTION field
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel jobDescLabel = new JLabel("<html>JOB DESCRIPTION <font color='black'>:</font> <font color='red'>*</font></html>");
        jobDescLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobDescLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea jobDescField = new JTextArea(4, 30);
        formPanel.add(new JScrollPane(jobDescField), gbc);
        gbc.gridwidth = 1; // Reset gridwidth and fill
    
        // JOB LOCATION field
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel jobLocationLabel = new JLabel("<html>JOB LOCATION <font color='black'>:</font> <font color='red'>*</font></html>");
        jobLocationLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobLocationLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(new JTextField(15), gbc);
    
        // JOB MODE field
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel jobModeLabel = new JLabel("<html>JOB MODE <font color='black'>:</font> <font color='red'>*</font></html>");
        jobModeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(jobModeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(new JTextField(15), gbc);
    
        // Save Button Section
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("SAVE");
        saveButton.setPreferredSize(new Dimension(100, 30));
        formPanel.add(saveButton, gbc);
    
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    
    // ----------------------------
    
    // Uniform Applications Screen
    private void showApplicationScreen() {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
    
        JLabel titleLabel = createTitleLabel("Applications");
        rightPanel.add(titleLabel, BorderLayout.NORTH);
    
        String[] columnNames = {"APP NO", "JOB ID", "JOB TITLE", "APPLICANT NAME", "CONTACT", "VIEW JOB", "VIEW APPL", "STS"};
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
                updatePageLabel();
            }
        });
        nextButton.addActionListener(e -> {
            currentPage++;
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
                ImageIcon originalIcon = new ImageIcon(selectedFile.getAbsolutePath());
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                        profilePicLabel.getWidth(),
                        profilePicLabel.getHeight(),
                        Image.SCALE_SMOOTH);
                profilePicLabel.setIcon(new ImageIcon(scaledImage));
                profilePicLabel.setText("");
                deleteButton.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame,
                    "File does not exist.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void removeProfilePicture() {
        profilePicLabel.setIcon(null);
        profilePicLabel.setText("Add Profile Picture");
        deleteButton.setVisible(false);
    }
}
