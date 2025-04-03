package com.group04.GUI.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MergedUserProfile {
    private JLabel profilePicLabel;
    private JButton editButton, removeButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MergedUserProfile().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("User Profile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout(1, 10));

        // === Left Panel for Navigation ===
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(180, frame.getHeight())); 
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));  // Reduced border thickness

        JButton profileButton = new JButton("Profile");
        JButton applicationButton = new JButton("Application Screen");
        JButton jobsearchButton = new JButton("Job Search");

        leftPanel.add(profileButton);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(applicationButton);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(jobsearchButton);

        // === Right Panel for Profile Details ===
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));  // Reduced border thickness

        // Title label with icon (Left-aligned)
        JLabel titleLabel = new JLabel("User Profile");

        // Set a small icon next to the title
        ImageIcon titleIcon = new ImageIcon("jobportal\\src\\Assets\\Profile-Pic-Icon.png");  // Specify the path to your icon image

        // Scale the icon to match the height of the title text
        int titleHeight = titleLabel.getPreferredSize().height;
        ImageIcon scaledIcon = new ImageIcon(titleIcon.getImage().getScaledInstance(-1, titleHeight, Image.SCALE_SMOOTH));

        JLabel iconLabel = new JLabel(scaledIcon);

        // Use FlowLayout for the right panel and align left
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        titlePanel.add(iconLabel);  // Add the icon label
        titlePanel.add(titleLabel);  // Add the title label next to the icon

        // === Profile Picture Panel ===
        JPanel profilePicPanel = new JPanel(new BorderLayout());
        profilePicPanel.setMaximumSize(new Dimension(150, 150));
        profilePicPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profilePicLabel = new JLabel("Profile Picture", SwingConstants.CENTER);
        profilePicLabel.setPreferredSize(new Dimension(120, 120));
        profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));  // Reduced border thickness
        profilePicLabel.setOpaque(true);
        profilePicLabel.setBackground(Color.LIGHT_GRAY);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        editButton = new JButton("Edit");
        removeButton = new JButton("Remove");
        removeButton.setVisible(false); 

        editButton.addActionListener(e -> uploadProfilePicture());
        removeButton.addActionListener(e -> removeProfilePicture());

        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        profilePicPanel.add(profilePicLabel, BorderLayout.CENTER);
        profilePicPanel.add(buttonPanel, BorderLayout.SOUTH);

        // === User Information Form ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[][] fields = {
            {"First Name", "Last Name"},
            {"Email", "Phone"},
            {"Current Company", "Job Role"}
        };

        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < 2; j++) {
                JLabel label = new JLabel(fields[i][j] + " *:");
                JTextField textField = new JTextField(15);

                gbc.gridx = j * 2; 
                gbc.gridy = i;
                formPanel.add(label, gbc);

                gbc.gridx = j * 2 + 1;
                formPanel.add(textField, gbc);
            }
        }

        JLabel skillsLabel = new JLabel("Preferences / Skills *:");
        JTextField skillsField = new JTextField(34);
        gbc.gridx = 0;
        gbc.gridy = fields.length;
        gbc.gridwidth = 1;
        formPanel.add(skillsLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(skillsField, gbc);

        JLabel linkedInLabel = new JLabel("LinkedIn URL *:");
        JTextField linkedInField = new JTextField(34);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        formPanel.add(linkedInLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(linkedInField, gbc);

        JLabel availabilityLabel = new JLabel("Availability *:");
        JTextField availabilityField = new JTextField(34);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        formPanel.add(availabilityLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(availabilityField, gbc);

        JLabel resumeLabel = new JLabel("Default Resume *:");
        JButton uploadButton = new JButton("Upload");
        JLabel fileNameLabel = new JLabel();
        JButton removeFileButton = new JButton("X");
        removeFileButton.setVisible(false);

        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF & Word Documents", "pdf", "doc", "docx"));
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
        gbc.gridwidth = 1;
        formPanel.add(resumeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(uploadButton, gbc);
        gbc.gridx = 2;
        formPanel.add(fileNameLabel, gbc);
        gbc.gridx = 3;
        formPanel.add(removeFileButton, gbc);

        // === Save Button ===
        JPanel savePanel = new JPanel();
        JButton saveButton = new JButton("SAVE");
        saveButton.setPreferredSize(new Dimension(100, 30));
        savePanel.add(saveButton);

        // === Assembling UI ===
        rightPanel.add(titlePanel);  // Add titlePanel containing left-aligned title and icon
        rightPanel.add(profilePicPanel);
        rightPanel.add(formPanel);
        rightPanel.add(savePanel);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon profileImage = new ImageIcon(new ImageIcon(selectedFile.getAbsolutePath())
                    .getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
            profilePicLabel.setIcon(profileImage);
            profilePicLabel.setText(""); 
            removeButton.setVisible(true);
        }
    }

    private void removeProfilePicture() {
        profilePicLabel.setIcon(null);
        profilePicLabel.setText("Profile Picture");
        removeButton.setVisible(false);
    }
}
