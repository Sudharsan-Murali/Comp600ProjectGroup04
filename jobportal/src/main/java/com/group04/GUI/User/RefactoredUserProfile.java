package com.group04.GUI.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class RefactoredUserProfile {
    private JLabel profilePicLabel;
    private JButton editButton, removeButton;
    private JPanel buttonPanel = new JPanel();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RefactoredUserProfile().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("User Profile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout(1, 10));

        JPanel leftPanel = createSidePanel();
        JPanel rightPanel = createRightPanel();

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createSidePanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(33, 37, 41));
        leftPanel.setPreferredSize(new Dimension(200, 600));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        String[] buttonNames = { "Profile", "Applications", "Search Jobs", "Logout" };
        for (String name : buttonNames) {
            JButton button = createSideButton(name);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(button);
        }

        return leftPanel;
    }

    private JButton createSideButton(String name) {
        JButton button = new JButton(name);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(52, 58, 64));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(73, 80, 87));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 58, 64));
            }
        });

        return button;
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
        JLabel screenTitle = createTitleLabel();
        titlePanel.setPreferredSize(new Dimension(800, 50));
        titlePanel.add(screenTitle);
        return titlePanel;
    }

    private JLabel createTitleLabel() {
        JLabel screenTitle;
        try {
            BufferedImage originalIcon = ImageIO.read(new File("jobportal\\src\\Assets\\Profile-Pic-Icon.png"));
            Image scaledIcon = originalIcon.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            screenTitle = new JLabel("User Profile", new ImageIcon(scaledIcon), JLabel.LEFT);
        } catch (Exception e) {
            screenTitle = new JLabel("User Profile");
        }

        Font font = new Font("Arial", Font.BOLD, 24);
        screenTitle.setFont(font);
        screenTitle.setForeground(Color.BLACK);
        return screenTitle;
    }

    private JPanel createProfilePicPanel() {
        // OUTER panel to center the whole thing
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        // LABEL container to control fixed size
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
    
        // BUTTON PANEL (Upload + Remove)
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        editButton = new JButton("Upload");
        removeButton = new JButton("Remove");
        removeButton.setVisible(false); // Hide by default
    
        editButton.addActionListener(e -> uploadProfilePicture());
        removeButton.addActionListener(e -> removeProfilePicture());
    
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
    
        // Add both to the vertical container
        containerPanel.add(imagePanel);
        containerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing
        containerPanel.add(buttonPanel);
    
        return containerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[][] fields = { { "First Name", "Last Name" }, { "Email", "Phone" }, { "Current Company", "Job Role" } };
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
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG Images", "jpg", "jpeg", "png"));
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(selectedFile.getPath());
            Image image = imageIcon.getImage().getScaledInstance(profilePicLabel.getWidth(), profilePicLabel.getHeight(), Image.SCALE_SMOOTH);
            profilePicLabel.setIcon(new ImageIcon(image));
            removeButton.setVisible(true);
        }
    }

    private void removeProfilePicture() {
        profilePicLabel.setIcon(null);
        removeButton.setVisible(false);
    }
}