package com.group04.GUI.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class UserProfile {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserProfile::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("User Profile Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 224, 224));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // === Profile Picture Section ===
        JPanel profilePicPanel = new JPanel();
        profilePicPanel.setPreferredSize(new Dimension(100, 100)); // Make it a small square
        profilePicPanel.setBackground(Color.LIGHT_GRAY);

        // Set the grid width and height to ensure proper sizing and centering
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;  // Span across 2 columns to center it
        gbc.gridheight = 1; // Ensure it takes only necessary space
        gbc.anchor = GridBagConstraints.CENTER; // Center the profile picture
        mainPanel.add(profilePicPanel, gbc);

        // Add Edit and Remove buttons
        JPanel profilePicButtonsPanel = new JPanel();
        profilePicButtonsPanel.setLayout(new BoxLayout(profilePicButtonsPanel, BoxLayout.Y_AXIS)); // Stack buttons vertically
        JButton editButton = new JButton("Edit");
        JButton removeButton = new JButton("Remove");
        removeButton.setVisible(false); // Hide Remove button initially

        editButton.addActionListener(e -> {
            // Open file chooser to select a new profile picture
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                ImageIcon icon = new ImageIcon(selectedFile.getPath());
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Scale the image
                profilePicPanel.removeAll(); // Remove any old profile picture
                profilePicPanel.add(new JLabel(new ImageIcon(img))); // Add the new image
                profilePicPanel.revalidate();
                profilePicPanel.repaint();
                removeButton.setVisible(true); // Show the Remove button once an image is uploaded
            }
        });

        removeButton.addActionListener(e -> {
            profilePicPanel.removeAll(); // Remove the image
            profilePicPanel.add(new JPanel()); // Replace with a blank panel
            profilePicPanel.revalidate();
            profilePicPanel.repaint();
            removeButton.setVisible(false); // Hide the Remove button after removal
        });

        profilePicButtonsPanel.add(editButton);
        profilePicButtonsPanel.add(removeButton);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;  // Make buttons span across two columns to keep them next to the profile picture
        mainPanel.add(profilePicButtonsPanel, gbc);

        // === User Information Form ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 10, 10, 10);
        formGbc.fill = GridBagConstraints.HORIZONTAL;

        String[][] fields = {
            {"First Name", "Last Name"},
            {"Email", "Phone"},
            {"Current Company", "Job Role"}
        };

        JTextField[][] textFields = new JTextField[fields.length][2];

        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < 2; j++) {
                JLabel label = new JLabel(fields[i][j] + " *:");
                label.setHorizontalAlignment(SwingConstants.RIGHT);  // Right-align the labels
                textFields[i][j] = new JTextField(15);

                formGbc.gridx = j * 2; // Place label at even indices (0,2)
                formGbc.gridy = i;
                formPanel.add(label, formGbc);

                formGbc.gridx = j * 2 + 1; // Place text field at odd indices (1,3)
                formPanel.add(textFields[i][j], formGbc);
            }
        }

        // === Preferences / Skills Field (Extended Width) ===
        JLabel skillsLabel = new JLabel("Preferences / Skills *:");
        skillsLabel.setHorizontalAlignment(SwingConstants.RIGHT);  // Right-align the label
        JTextField skillsField = new JTextField(34);

        formGbc.gridx = 0;
        formGbc.gridy = fields.length;
        formGbc.gridwidth = 1;
        formPanel.add(skillsLabel, formGbc);

        formGbc.gridx = 1;
        formGbc.gridwidth = 3; // Span across both columns
        formPanel.add(skillsField, formGbc);

        // === Additional Fields ===
        JLabel linkedInLabel = new JLabel("LinkedIn URL *:");
        linkedInLabel.setHorizontalAlignment(SwingConstants.RIGHT);  // Right-align the label
        JTextField linkedInField = new JTextField(34);
        
        formGbc.gridx = 0;
        formGbc.gridy = fields.length + 1;
        formGbc.gridwidth = 1;
        formPanel.add(linkedInLabel, formGbc);
        
        formGbc.gridx = 1;
        formGbc.gridwidth = 3;
        formPanel.add(linkedInField, formGbc);
        
        JLabel availabilityLabel = new JLabel("Availability *:");
        availabilityLabel.setHorizontalAlignment(SwingConstants.RIGHT);  // Right-align the label
        JTextField availabilityField = new JTextField(34);
        
        formGbc.gridx = 0;
        formGbc.gridy = fields.length + 2;
        formGbc.gridwidth = 1;
        formPanel.add(availabilityLabel, formGbc);
        
        formGbc.gridx = 1;
        formGbc.gridwidth = 3;
        formPanel.add(availabilityField, formGbc);
        
        JLabel resumeLabel = new JLabel("Default Resume *:");
        resumeLabel.setHorizontalAlignment(SwingConstants.RIGHT);  // Right-align the label
        JButton uploadButton = new JButton("Upload");
        JLabel fileNameLabel = new JLabel();
        JButton removeFileButton = new JButton("X");
        removeFileButton.setVisible(false);
        
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF & Word Documents", "pdf", "doc", "docx"));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                fileNameLabel.setText(selectedFile.getName());
                removeFileButton.setVisible(true);
            }
        });
        
        removeFileButton.addActionListener(e -> {
            fileNameLabel.setText("");
            removeFileButton.setVisible(false);
        });
        
        formGbc.gridx = 0;
        formGbc.gridy = fields.length + 3;
        formGbc.gridwidth = 1;
        formPanel.add(resumeLabel, formGbc);
        
        formGbc.gridx = 1;
        formGbc.gridwidth = 1;
        formPanel.add(uploadButton, formGbc);
        
        formGbc.gridx = 2;
        formPanel.add(fileNameLabel, formGbc);
        
        formGbc.gridx = 3;
        formPanel.add(removeFileButton, formGbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(formPanel, gbc);

        // === Save Button ===
        JButton saveButton = new JButton("SAVE");
        saveButton.setPreferredSize(new Dimension(80, 30)); // Adjust to fit the text
        gbc.gridy = 3;
        gbc.gridx = 1; // Keep it centered in the grid, but not spanning multiple columns
        gbc.gridwidth = 1; // Make sure it doesn't span across fields
        gbc.fill = GridBagConstraints.NONE; // Don't stretch the button
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(saveButton, gbc);

        // === Modify Other Buttons to Hug Content ===
        uploadButton.setPreferredSize(new Dimension(uploadButton.getPreferredSize().width, 30)); // Adjust to hug text
        removeFileButton.setPreferredSize(new Dimension(removeFileButton.getPreferredSize().width, 30)); // Adjust to hug text
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
