package com.group04.GUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;

public class temp extends JFrame {
    
    // UIConstants class
    private static class UIConstants {
        public static final Color DARK_BG = new Color(33, 37, 41);
        public static final Color BUTTON_BG = new Color(52, 58, 64);
        public static final Color BUTTON_HOVER = new Color(73, 80, 87);
        public static final Dimension SIDE_PANEL_SIZE = new Dimension(200, 600);
        public static final Dimension BUTTON_SIZE = new Dimension(180, 40);
        public static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 14);
        public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
        public static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    }

    // HoverEffectMouseAdapter
    private static class HoverEffectMouseAdapter extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            ((JComponent) e.getSource()).setBackground(UIConstants.BUTTON_HOVER);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((JComponent) e.getSource()).setBackground(UIConstants.BUTTON_BG);
        }
    }

    // ButtonFactory
    private static class ButtonFactory {
        public static JButton createSideButton(String text) {
            JButton button = new JButton(text);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(UIConstants.BUTTON_SIZE);
            button.setBackground(UIConstants.BUTTON_BG);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setFont(UIConstants.BUTTON_FONT);
            button.addMouseListener(new HoverEffectMouseAdapter());
            return button;
        }
    }

    // UIUtils
    private static class UIUtils {
        public static JLabel createTitleLabel(String title, String iconPath) {
            try {
                BufferedImage icon = ImageIO.read(new File(iconPath));
                Image scaledIcon = icon.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                JLabel label = new JLabel(title, new ImageIcon(scaledIcon), JLabel.LEFT);
                label.setFont(UIConstants.TITLE_FONT);
                return label;
            } catch (Exception e) {
                JLabel label = new JLabel(title);
                label.setFont(UIConstants.TITLE_FONT);
                return label;
            }
        }
    }

    // SidePanel
    private static class SidePanel extends JPanel {
        public SidePanel(ActionListener listener) {
            setBackground(UIConstants.DARK_BG);
            setPreferredSize(UIConstants.SIDE_PANEL_SIZE);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(Box.createRigidArea(new Dimension(0, 40)));
            
            String[] buttons = {"Profile", "Applications", "Search Jobs", "Logout"};
            for (String btn : buttons) {
                JButton button = ButtonFactory.createSideButton(btn);
                button.addActionListener(listener);
                add(Box.createRigidArea(new Dimension(0, 20)));
                add(button);
            }
        }
    }

    // BaseScreen
    private abstract static class BaseScreen extends JFrame {
        public BaseScreen(String title) {
            super(title);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(900, 700);
            setLayout(new BorderLayout());
            add(new SidePanel(this::handleNavigation), BorderLayout.WEST);
        }
        
        protected abstract void handleNavigation(ActionEvent e);
    }

    // UserProfileScreen
    private static class UserProfileScreen extends BaseScreen {
        private JLabel profilePicLabel;
        private JButton editButton, removeButton;
        private JPanel buttonPanel = new JPanel();

        public UserProfileScreen() {
            super("User Profile");
            initializeUI();
        }

        private void initializeUI() {
            JPanel rightPanel = createRightPanel();
            add(rightPanel, BorderLayout.CENTER);
            setVisible(true);
        }

        private JPanel createRightPanel() {
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            rightPanel.add(createTitlePanel());
            rightPanel.add(createProfilePicPanel());
            rightPanel.add(createFormPanel());

            return rightPanel;
        }

        private JPanel createTitlePanel() {
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            titlePanel.setPreferredSize(new Dimension(800, 50));
            // Note: You might need to adjust the path to your icon
            titlePanel.add(UIUtils.createTitleLabel("User Profile", "src/Assets/Profile-Pic-Icon.png"));
            return titlePanel;
        }

        private JPanel createProfilePicPanel() {
            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
            containerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel imagePanel = new JPanel();
            imagePanel.setMaximumSize(new Dimension(120, 120));
            imagePanel.setPreferredSize(new Dimension(120, 120));
            imagePanel.setLayout(new BorderLayout());

            profilePicLabel = new JLabel("Profile Picture", SwingConstants.CENTER);
            profilePicLabel.setPreferredSize(new Dimension(120, 120));
            profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            profilePicLabel.setOpaque(true);
            profilePicLabel.setBackground(Color.LIGHT_GRAY);

            imagePanel.add(profilePicLabel, BorderLayout.CENTER);

            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            editButton = new JButton("Upload");
            removeButton = new JButton("Remove");
            removeButton.setVisible(false);

            editButton.addActionListener(e -> uploadProfilePicture());
            removeButton.addActionListener(e -> removeProfilePicture());

            buttonPanel.add(editButton);
            buttonPanel.add(removeButton);

            containerPanel.add(imagePanel);
            containerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            containerPanel.add(buttonPanel);

            return containerPanel;
        }

        private JPanel createFormPanel() {
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            String[][] fields = { { "First Name", "Last Name" },
                    { "Email", "Phone" },
                    { "Current Company", "Job Role" } };
            for (int i = 0; i < fields.length; i++) {
                for (int j = 0; j < 2; j++) {
                    addFormField(formPanel, fields[i][j], i, j, gbc);
                }
            }

            addFormField(formPanel, "Preferences / Skills", fields.length, 0, gbc);
            addFormField(formPanel, "LinkedIn URL", fields.length + 1, 0, gbc);
            addFormField(formPanel, "Availability", fields.length + 2, 0, gbc);

            addResumeField(formPanel, gbc);
            addSaveButton(formPanel, gbc);

            return formPanel;
        }

        private void addFormField(JPanel panel, String labelText, int row, int col, GridBagConstraints gbc) {
            JLabel label = new JLabel("<html>" + labelText + " <font color='red'>*</font>:</html>");
            JTextField textField = new JTextField(15);

            gbc.gridx = col * 2;
            gbc.gridy = row;
            panel.add(label, gbc);

            gbc.gridx = col * 2 + 1;
            panel.add(textField, gbc);
        }

        private void addResumeField(JPanel formPanel, GridBagConstraints gbc) {
            JLabel resumeLabel = new JLabel("Default Resume *:");
            JButton uploadButton = new JButton("Attach");
            JLabel fileNameLabel = new JLabel();
            JButton removeFileButton = new JButton("X");
            removeFileButton.setVisible(false);

            uploadButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter(
                        "PDF & Word Documents", "pdf", "doc", "docx"));
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileNameLabel.setText(selectedFile.getName());
                    removeFileButton.setVisible(true);
                }
            });

            removeFileButton.addActionListener(e -> {
                fileNameLabel.setText("");
                removeFileButton.setVisible(false);
            });

            gbc.gridy++;
            gbc.gridx = 0;
            formPanel.add(resumeLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(uploadButton, gbc);
            gbc.gridx = 2;
            formPanel.add(fileNameLabel, gbc);
            gbc.gridx = 3;
            formPanel.add(removeFileButton, gbc);
        }

        private void addSaveButton(JPanel formPanel, GridBagConstraints gbc) {
            JButton saveButton = new JButton("Save");
            saveButton.setPreferredSize(new Dimension(120, 30));
            gbc.gridy++;
            gbc.gridx = 1;
            formPanel.add(saveButton, gbc);
        }

        private void uploadProfilePicture() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Profile Picture");
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                    "JPEG Images", "jpg", "jpeg", "png"));
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                ImageIcon imageIcon = new ImageIcon(selectedFile.getPath());
                Image image = imageIcon.getImage().getScaledInstance(
                        profilePicLabel.getWidth(),
                        profilePicLabel.getHeight(),
                        Image.SCALE_SMOOTH);
                profilePicLabel.setIcon(new ImageIcon(image));
                removeButton.setVisible(true);
            }
        }

        private void removeProfilePicture() {
            profilePicLabel.setIcon(null);
            removeButton.setVisible(false);
        }

        @Override
        protected void handleNavigation(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();
            dispose(); // Close current window

            switch (command) {
                case "Profile":
                    SwingUtilities.invokeLater(() -> new UserProfileScreen());
                    break;
                case "Search Jobs":
                    SwingUtilities.invokeLater(() -> new JobSearchScreen());
                    break;
                case "Applications":
                    SwingUtilities.invokeLater(() -> new UserAppScreen());
                    break;
                case "Logout":
                    performLogout();
                    break;
                default:
                    System.out.println("Unknown command: " + command);
            }
        }

        private void performLogout() {
            // Add logout logic here
            System.out.println("Logging out...");
            dispose();
            // new LoginScreen(); // Assuming you have a login screen
        }
    }
    

    // JobSearchScreen
    private static class JobSearchScreen extends BaseScreen {
        private JTextField searchField;
        private JPanel jobResultsPanel;
        private JTextArea jobDetailsArea;
        private JPanel detailsPanel;
        
        public JobSearchScreen() {
            super("Job Search");
            initializeUI();
        }

        private void initializeUI() {
            // Main content panel
            JPanel mainContentPanel = new JPanel(new BorderLayout());
            
            // Create north container panel for title + search
            JPanel northContainer = new JPanel();
            northContainer.setLayout(new BoxLayout(northContainer, BoxLayout.Y_AXIS));
            
            // Add components to north container
            northContainer.add(createTitlePanel());
            northContainer.add(createSearchPanel());
            
            // Add north container and job content
            mainContentPanel.add(northContainer, BorderLayout.NORTH);
            mainContentPanel.add(createJobContentSplitPane(), BorderLayout.CENTER);
            
            add(mainContentPanel, BorderLayout.CENTER);
            setVisible(true);
        }

        private JPanel createTitlePanel() {
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            JLabel screenTitle;

            try {
                BufferedImage originalIcon = ImageIO.read(new File("src/Assets/Profile-Pic-Icon.png"));
                Image scaledIcon = originalIcon.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                screenTitle = new JLabel("Job Search Screen", new ImageIcon(scaledIcon), JLabel.LEFT);
            } catch (Exception e) {
                screenTitle = new JLabel("Job Search Screen"); // Fallback
            }

            screenTitle.setFont(UIConstants.TITLE_FONT);
            titlePanel.add(screenTitle);
            
            return titlePanel;
        }

        private JPanel createSearchPanel() {
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
            JLabel searchLabel = new JLabel("Search Jobs:");
            searchField = new JTextField(20);
            JButton searchButton = new JButton("Search");
            JButton clearButton = new JButton("Clear");

            searchPanel.add(searchLabel);
            searchPanel.add(searchField);
            searchPanel.add(searchButton);
            searchPanel.add(clearButton);
            
            // Allow panel to expand horizontally
            searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPanel.getPreferredSize().height));
            
            // Add action listeners
            searchButton.addActionListener(e -> performSearch());
            clearButton.addActionListener(e -> clearSearch());
            
            return searchPanel;
        }
        
        private void performSearch() {
            String searchQuery = searchField.getText().trim();
            jobResultsPanel.removeAll();

            if (searchQuery.isEmpty()) {
                jobResultsPanel.add(new JLabel("Please enter a search query."));
            } else {
                jobResultsPanel.add(new JLabel("Results for: " + searchQuery));
                
                // Create some sample job cards
                for (int i = 1; i <= 5; i++) {
                    JPanel jobCard = createJobCard("Job " + i + ": " + searchQuery + " Developer at Company " + (char)('A' + i - 1), 
                        "Location: City " + i + "\nSalary: $" + (80000 + i * 5000) + "\nPosted: " + i + " days ago",
                        "This is a detailed description for Job " + i + " related to " + searchQuery + 
                        ".\n\nResponsibilities:\n- Develop " + searchQuery + " applications\n- Work with team members\n" +
                        "- Participate in design discussions\n\nRequirements:\n- " + (i+2) + "+ years of experience\n" +
                        "- Strong knowledge of " + searchQuery + "\n- Good communication skills");
                    jobResultsPanel.add(jobCard);
                    jobResultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            jobResultsPanel.revalidate();
            jobResultsPanel.repaint();
        }
        
        private void clearSearch() {
            searchField.setText("");
            jobResultsPanel.removeAll();
            jobResultsPanel.add(new JLabel("Enter a search term and click 'Search' to find jobs"));
            jobResultsPanel.revalidate();
            jobResultsPanel.repaint();
            
            // Hide details panel
            detailsPanel.setVisible(false);
        }
        
        private JSplitPane createJobContentSplitPane() {
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            
            // Left panel - Job Results
            jobResultsPanel = new JPanel();
            jobResultsPanel.setLayout(new BoxLayout(jobResultsPanel, BoxLayout.Y_AXIS));
            JScrollPane resultsScrollPane = new JScrollPane(jobResultsPanel);
            resultsScrollPane.setPreferredSize(new Dimension(350, 500));
            
            // Initial placeholder text
            jobResultsPanel.add(new JLabel("Enter a search term and click 'Search' to find jobs"));
            
            // Right panel - Job Details
            detailsPanel = new JPanel(new BorderLayout());
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Create a title for the details panel
            JLabel detailsTitle = new JLabel("Job Details");
            detailsTitle.setFont(UIConstants.TITLE_FONT);
            detailsPanel.add(detailsTitle, BorderLayout.NORTH);
            
            // Create a text area for the job details
            jobDetailsArea = new JTextArea();
            jobDetailsArea.setEditable(false);
            jobDetailsArea.setWrapStyleWord(true);
            jobDetailsArea.setLineWrap(true);
            jobDetailsArea.setFont(UIConstants.NORMAL_FONT);
            jobDetailsArea.setText("Select a job to view details");
            
            // Add an apply button
            JButton applyButton = new JButton("Apply Now");
            applyButton.setPreferredSize(new Dimension(150, 40));
            applyButton.addActionListener(e -> {
                // In a real application, this would open an application form
                JFrame popup = new JFrame("Application");
                popup.setSize(400, 200);
                popup.setLocationRelativeTo(null);
                popup.setLayout(new BorderLayout());
                popup.add(new JLabel("Application submitted successfully!", SwingConstants.CENTER));
                popup.setVisible(true);
            });
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(applyButton);
            
            // Add components to the details panel
            detailsPanel.add(new JScrollPane(jobDetailsArea), BorderLayout.CENTER);
            detailsPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            // Initially hide the details panel
            detailsPanel.setVisible(false);
            
            // Add panels to split pane
            splitPane.setLeftComponent(resultsScrollPane);
            splitPane.setRightComponent(detailsPanel);
            splitPane.setDividerLocation(350);
            
            return splitPane;
        }

        private JPanel createJobCard(String title, String summary, String details) {
            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setMaximumSize(new Dimension(330, 100));
            cardPanel.setPreferredSize(new Dimension(330, 100));
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            cardPanel.setBackground(Color.WHITE);
            
            // Title
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
            
            // Summary
            JLabel summaryLabel = new JLabel(summary);
            summaryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            summaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            summaryLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            cardPanel.add(titleLabel);
            cardPanel.add(summaryLabel);
            
            // Add mouse listener to handle click
            cardPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Show job details
                    jobDetailsArea.setText(title + "\n\n" + summary + "\n\n" + details);
                    detailsPanel.setVisible(true);
                    
                    // Highlight the selected card
                    for (Component comp : jobResultsPanel.getComponents()) {
                        if (comp instanceof JPanel) {
                            comp.setBackground(Color.WHITE);
                        }
                    }
                    cardPanel.setBackground(new Color(230, 242, 255)); // Light blue highlight
                    
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (cardPanel.getBackground() != new Color(230, 242, 255)) {
                        cardPanel.setBackground(new Color(245, 245, 245)); // Light gray hover
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (cardPanel.getBackground() == new Color(245, 245, 245)) {
                        cardPanel.setBackground(Color.WHITE);
                    }
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
                    SwingUtilities.invokeLater(() -> new UserProfileScreen());
                    break;
                case "Search Jobs":
                    SwingUtilities.invokeLater(() -> new JobSearchScreen());
                    break;
                case "Applications":
                    SwingUtilities.invokeLater(() -> new UserAppScreen());
                    break;
                case "Logout":
                    performLogout();
                    break;
            }
        }
        
        private void performLogout() {
            // Logout logic
            System.out.println("Logging out...");
            dispose();
            // new LoginScreen(); // Assuming you have a login screen
        }
    }
    

    // Enhanced UserAppScreen with table functionality
    private static class UserAppScreen extends BaseScreen {
        private JTable table;
        private DefaultTableModel tableModel;

        public UserAppScreen() {
            super("Applications");
            initializeUI();
        }

        private void initializeUI() {
            JPanel mainPanel = new JPanel(new BorderLayout());
            
            // Title Panel
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            try {
                BufferedImage icon = ImageIO.read(new File("src/Assets/Profile-Pic-Icon.png"));
                Image scaledIcon = icon.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                titlePanel.add(new JLabel("Applications", new ImageIcon(scaledIcon), JLabel.LEFT));
            } catch (Exception e) {
                titlePanel.add(new JLabel("Applications"));
            }

            // Table setup
            String[] columnNames = {"No", "Job Title", "Company Name", "Status", "Date", "Update Date", "Withdraw", "Resume", "View"};
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 6 || column == 7 || column == 8;
                }
            };

            table = new JTable(tableModel);
            table.setRowHeight(25);
            configureTableColumns();

            JScrollPane scrollPane = new JScrollPane(table);
            mainPanel.add(titlePanel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            // Add sample data
            for (int i = 1; i <= 5; i++) {
                tableModel.addRow(new Object[]{
                    i, "Developer " + i, "Company " + i, "Applied", 
                    "2023-09-0" + i, "2023-09-1" + i, "Withdraw", 
                    "Resume Content " + i, "View"
                });
            }

            add(mainPanel, BorderLayout.CENTER);
            setVisible(true);
        }

        private void configureTableColumns() {
            table.getColumn("Withdraw").setCellRenderer(new ButtonRenderer("Withdraw"));
            table.getColumn("Withdraw").setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, table, "Withdraw", this));

            table.getColumn("Resume").setCellRenderer(new ButtonRenderer("Download"));
            table.getColumn("Resume").setCellEditor(new ResumeButtonEditor(new JCheckBox(), tableModel, table, this));

            table.getColumn("View").setCellRenderer(new ButtonRenderer("View"));
            table.getColumn("View").setCellEditor(new ButtonEditor(new JCheckBox(), tableModel, table, "View", this));
        }

        @Override
        protected void handleNavigation(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();
            dispose();
            
            switch (command) {
                case "Profile": new UserProfileScreen(); break;
                case "Search Jobs": new JobSearchScreen(); break;
                case "Applications": new UserAppScreen(); break;
                case "Logout": System.exit(0); break;
            }
        }
    }

    // Table Cell Renderers and Editors
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        private String label;

        public ButtonRenderer(String label) {
            super(label);
            this.label = label;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            setFont(new Font("Arial", Font.BOLD, 10));
            setBackground(new Color(200, 200, 200));
            setEnabled(!(label.equals("Withdraw") && "Withdrawn".equals(table.getValueAt(row, 3))));
            return this;
        }
    }

    private static class ResumeButtonEditor extends DefaultCellEditor {
        private JButton button;
        private JTable table;
        private DefaultTableModel tableModel;
        private int row;
        private JFrame frame;

        public ResumeButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, JTable table, JFrame frame) {
            super(checkBox);
            this.tableModel = tableModel;
            this.table = table;
            this.frame = frame;
            button = new JButton("View");
            button.setFont(new Font("Arial", Font.BOLD, 10));
            button.setBackground(new Color(200, 200, 200));
            button.addActionListener(e -> handleResumeAction());
        }

        private void handleResumeAction() {
            String resumeText = (String) tableModel.getValueAt(row, 7);
            int response = JOptionPane.showConfirmDialog(frame,
                    "Do you want to download this resume?",
                    "Download Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                try {
                    String jobTitle = (String) tableModel.getValueAt(row, 1);
                    String company = (String) tableModel.getValueAt(row, 2);
                    String fileName = "Resume_" + sanitizeFilename(jobTitle) + "_" +
                            sanitizeFilename(company) + ".txt";

                    File downloadsDir = new File(System.getProperty("user.home"), "Downloads");
                    if (!downloadsDir.exists()) downloadsDir.mkdirs();

                    File file = new File(downloadsDir, fileName);
                    try (OutputStreamWriter writer = new OutputStreamWriter(
                            new FileOutputStream(file), StandardCharsets.UTF_8)) {
                        writer.write(resumeText);
                    }

                    JOptionPane.showMessageDialog(frame,
                            "Resume downloaded to:\n" + file.getAbsolutePath(),
                            "Download Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Error saving resume: " + ex.getMessage(),
                            "Download Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            fireEditingStopped();
        }

        private String sanitizeFilename(String input) {
            return input.replaceAll("[^a-zA-Z0-9.-]", "_");
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "View";
        }
    }

    private static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private JTable table;
        private DefaultTableModel tableModel;
        private int row;
        private JFrame frame;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, 
                JTable table, String label, JFrame frame) {
            super(checkBox);
            this.tableModel = tableModel;
            this.table = table;
            this.frame = frame;
            button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 10));
            button.setBackground(new Color(200, 200, 200));
            button.addActionListener(e -> handleButtonAction(label));
        }

        private void handleButtonAction(String label) {
            if (label.equals("Withdraw")) {
                handleWithdrawAction();
            } else if (label.equals("View")) {
                JOptionPane.showMessageDialog(frame,
                        "Displaying full job post details...",
                        "Job Post",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            fireEditingStopped();
        }

        private void handleWithdrawAction() {
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to withdraw this application?",
                    "Confirm Withdrawal",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.setValueAt("Withdrawn", row, 3);
                JOptionPane.showMessageDialog(frame,
                        "Application withdrawn successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserProfileScreen());
    }

    // In all handleNavigation methods (UserProfileScreen, JobSearchScreen, UserAppScreen)
@Override
protected void handleNavigation(ActionEvent e) {
    String command = ((JButton) e.getSource()).getText();
    dispose(); // Close current window

    switch (command) {
        case "Profile":
            new UserProfileScreen(); // Direct instantiation
            break;
        case "Search Jobs":
            new JobSearchScreen();
            break;
        case "Applications":
            new UserAppScreen();
            break;
        case "Logout":
            System.exit(0);
            break;
    }
}
}