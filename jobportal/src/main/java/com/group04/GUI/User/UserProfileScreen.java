package com.group04.GUI.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.group04.GUI.User.Components.BaseScreen;
import com.group04.GUI.User.Components.UIUtils;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class UserProfileScreen extends BaseScreen {
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
        titlePanel.add(UIUtils.createTitleLabel("User Profile",
                "jobportal\\src\\Assets\\Profile-Pic-Icon.png"));
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
            // Use invokeLater to ensure thread safety
            SwingUtilities.invokeLater(() -> new UserProfileScreen());
            break;
        case "Search Jobs":
            SwingUtilities.invokeLater(() -> new JobSearchScreen());
            break;
        case "Applications":
            SwingUtilities.invokeLater(() -> new UserAppscreen());
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserProfileScreen::new);
    }

}