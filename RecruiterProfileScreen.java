/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumnModel;


public class RecruiterProfileScreen 
{
    private JLabel profilePicLabel;
    private JPanel rightPanel;
    private JButton profileButton, jobpostsButton ;
    private JButton addjobpostsButton, applicationButton ;
    private JFrame frame;
    private int currentPage = 1;
    private JLabel pageLabel;
    private JButton logoutButton;
    private JButton uploadButton, deleteButton;


public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new RecruiterProfileScreen().createAndShowGUI());
    }

    void createAndShowGUI() 
        {
            frame = new JFrame("Recruiter Portal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setLayout(new BorderLayout(20, 20));

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setPreferredSize(new Dimension(180, frame.getHeight()));
            leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            profileButton = new JButton("Profile");
            jobpostsButton = new JButton("Job Posts");
            addjobpostsButton = new JButton("Add Job Posts");
            applicationButton = new JButton("Applications");
            logoutButton = new JButton("Logout");

            profileButton.addActionListener(e -> showProfileScreen());
            jobpostsButton.addActionListener(e -> showJobPostsScreen());
            addjobpostsButton.addActionListener(e -> showAddJobPostsScreen());
            applicationButton.addActionListener(e -> showApplicationScreen());
            logoutButton.addActionListener(e -> showApplicationScreen());

            leftPanel.add(profileButton);
            leftPanel.add(Box.createVerticalStrut(10));
            leftPanel.add(jobpostsButton);
            leftPanel.add(Box.createVerticalStrut(10));
            leftPanel.add(addjobpostsButton);
            leftPanel.add(Box.createVerticalStrut(10));
            leftPanel.add(applicationButton);
            leftPanel.add(Box.createVerticalStrut(475));
            leftPanel.add(logoutButton);  

            frame.add(leftPanel, BorderLayout.WEST);
            rightPanel = new JPanel();
            frame.add(rightPanel, BorderLayout.CENTER);

            showProfileScreen();

            frame.setVisible(true);
        }
        private void showProfileScreen() 
        {
            rightPanel.removeAll();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JPanel profilePanel = new JPanel();
            profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
            profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel profilePicPanel = new JPanel(new BorderLayout());
            profilePicPanel.setMaximumSize(new Dimension(150, 150));
            profilePicPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            profilePicLabel = new JLabel("Add Profile Picture", SwingConstants.CENTER);
            profilePicLabel.setPreferredSize(new Dimension(120, 120));
            profilePicLabel.setMinimumSize(new Dimension(120, 120));
            profilePicLabel.setMaximumSize(new Dimension(120, 120));
            profilePicLabel.setHorizontalAlignment(JLabel.CENTER);
            profilePicLabel.setVerticalAlignment(JLabel.CENTER);
            profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            profilePicLabel.setOpaque(true);
            profilePicLabel.setBackground(Color.LIGHT_GRAY);

            profilePicPanel.add(profilePicLabel, BorderLayout.CENTER);

            // === Buttons ===
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            uploadButton = new JButton("Upload");
            uploadButton.setPreferredSize(new Dimension(100, 30));

            deleteButton = new JButton("Delete");
            deleteButton.setPreferredSize(new Dimension(100, 30));
            deleteButton.setVisible(false); // Hide initially
            
            buttonPanel.add(uploadButton);
            buttonPanel.add(deleteButton);

            profilePanel.add(profilePicPanel);
            profilePanel.add(buttonPanel);

            uploadButton.addActionListener(e -> uploadProfilePicture());
            deleteButton.addActionListener(e -> removeProfilePicture());

            // === Form Panel ===
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            String[] labels = {"First Name", "Last Name", "Email", "Company Name", "Company Address", "Company Phone", "Company LinkedIn", "Company Website"};

            for (int i = 0; i < labels.length; i++) 
            {
                JLabel label = new JLabel(labels[i]);
                JTextField textField = new JTextField(20);

                gbc.gridx = 0;
                gbc.gridy = i;
                formPanel.add(label, gbc); // Label on left

                gbc.gridx = 1;
                formPanel.add(textField, gbc); // Text field on right
            }

            // === Save Button ===
            JPanel savePanel = new JPanel();
            JButton saveButton = new JButton("SAVE");
            saveButton.setPreferredSize(new Dimension(100, 30));
            savePanel.add(saveButton);

            // === Parent Panel to Maintain Layout ===
            JPanel parentPanel = new JPanel();
            parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));
            parentPanel.add(profilePanel);
            parentPanel.add(Box.createVerticalStrut(20));
            parentPanel.add(formPanel);
            parentPanel.add(Box.createVerticalStrut(10));
            parentPanel.add(savePanel);

            rightPanel.add(parentPanel);
            frame.add(rightPanel, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
        }
        // Upload Profile Picture
        private void uploadProfilePicture() 
        {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) 
            {
                File selectedFile = fileChooser.getSelectedFile();
                ImageIcon originalIcon = new ImageIcon(selectedFile.getAbsolutePath());

                // Scale image while maintaining aspect ratio
                Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                profilePicLabel.setIcon(new ImageIcon(scaledImage));
                profilePicLabel.setText(""); // Remove placeholder text
                deleteButton.setVisible(true); // Show delete button after image upload    
            }
        }
        // Remove Profile Picture
        private void removeProfilePicture() 
        {
            profilePicLabel.setIcon(null);
            profilePicLabel.setText("Add Profile Picture"); // Restore default text
            deleteButton.setVisible(false); // Hide delete button when no image is present
        }
    
    private void showJobPostsScreen() 
        {
            rightPanel.removeAll();
            rightPanel.setLayout(new BorderLayout());

            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            JButton addJobButton = new JButton("ADD");
            headerPanel.add(addJobButton);

            String[] columnNames = {"S.NO", "JOB TITLE", "DATE POSTED", "NUMBER", "STATUS", "EDIT", "DELETE", "DUE DATE"};
            Object[][] data = new Object[10][8]; // Placeholder data
            JTable jobTable = new JTable(data, columnNames);
            JScrollPane tableScrollPane = new JScrollPane(jobTable);

            JPanel footerPanel = new JPanel();
            JButton prevButton = new JButton("<");
            JButton nextButton = new JButton(">");
            pageLabel = new JLabel("Page " + currentPage);

            prevButton.addActionListener(e -> 
            {
                if (currentPage > 1) 
                {
                    currentPage--;
                    updatePageLabel();
                }
            });
            nextButton.addActionListener(e -> 
            {
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

        private void updatePageLabel() 
            {
                pageLabel.setText("Page " + currentPage);
            }

    private void showAddJobPostsScreen() 
    {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());

        // Title: Left aligned
        JLabel titleLabel = new JLabel("ADD JOB POST");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Job ID (aligned to right side)
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        formPanel.add(new JLabel("JOB ID:"), gbc);
        gbc.gridx = 2;
        formPanel.add(new JTextField(15), gbc);

        // Job Title
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        formPanel.add(new JLabel("JOB TITLE:"), gbc);
        gbc.gridx = 2;
        formPanel.add(new JTextField(15), gbc);

        // Job Type
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridx = 1;
        formPanel.add(new JLabel("JOB TYPE:"), gbc);
        gbc.gridx = 2;
        formPanel.add(new JTextField(15), gbc);

        // Salary Range (Min - Max)
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridx = 1;
        formPanel.add(new JLabel("SALARY RANGE:"), gbc);
        gbc.gridx = 2;
        JTextField minSalaryField = new JTextField(7);
        formPanel.add(minSalaryField, gbc);
        gbc.gridx = 3;
        formPanel.add(new JLabel("TO:"), gbc);
        gbc.gridx = 4;
        JTextField maxSalaryField = new JTextField(7);
        formPanel.add(maxSalaryField, gbc);

        // Job Description
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridx = 1;
        formPanel.add(new JLabel("Job Description:"), gbc);
        gbc.gridx = 2; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH;
        JTextArea jobDescField = new JTextArea(4, 30);
        formPanel.add(new JScrollPane(jobDescField), gbc);

        // Job Location
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        formPanel.add(new JLabel("JOB LOCATION:"), gbc);
        gbc.gridx = 2;
        formPanel.add(new JTextField(15), gbc);

        // Job Mode
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridx = 1;
        formPanel.add(new JLabel("JOB MODE:"), gbc);
        gbc.gridx = 2;
        formPanel.add(new JTextField(15), gbc);

        // Save Button
        gbc.gridx = 1; gbc.gridy = 8; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("SAVE");
        saveButton.setPreferredSize(new Dimension(100, 30));
        formPanel.add(saveButton, gbc);

        rightPanel.add(titleLabel, BorderLayout.NORTH);
        rightPanel.add(formPanel, BorderLayout.CENTER);

        rightPanel.revalidate();
        rightPanel.repaint();
    }
    
    private void showApplicationScreen() 
    {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("APPLICATIONS:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"APP NO", "JOB ID", "JOB TITLE", "APPLICANT NAME", "CONTACT", "VIEW JOB", "VIEW APPL", "STS"};
        Object[][] data = new Object[10][8]; // Placeholder data
        JTable applicationTable = new JTable(data, columnNames);

        // Set proper column widths
        TableColumnModel columnModel = applicationTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // APP NO
        columnModel.getColumn(1).setPreferredWidth(70);  // JOB ID
        columnModel.getColumn(2).setPreferredWidth(120); // JOB TITLE
        columnModel.getColumn(3).setPreferredWidth(150); // APPLICANT NAME
        columnModel.getColumn(4).setPreferredWidth(100); // CONTACT
        columnModel.getColumn(5).setPreferredWidth(80);  // VIEW JOB
        columnModel.getColumn(6).setPreferredWidth(80);  // VIEW APPLICATION
        columnModel.getColumn(7).setPreferredWidth(50);  // STATUS

        // Disable auto column resizing to avoid overlap
        applicationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane tableScrollPane = new JScrollPane(applicationTable);

        JPanel footerPanel = new JPanel();
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        pageLabel = new JLabel("Page " + currentPage);

        prevButton.addActionListener(e -> 
        {
            if (currentPage > 1) 
            {
                currentPage--;
                updatePageLabel();
            }
        });

        nextButton.addActionListener(e -> 
        {
            currentPage++;
            updatePageLabel();
        });

        footerPanel.add(prevButton);
        footerPanel.add(pageLabel);
        footerPanel.add(nextButton);

        rightPanel.add(titleLabel, BorderLayout.NORTH);
        rightPanel.add(tableScrollPane, BorderLayout.CENTER);
        rightPanel.add(footerPanel, BorderLayout.SOUTH);

        rightPanel.revalidate();
        rightPanel.repaint();
    }
}
