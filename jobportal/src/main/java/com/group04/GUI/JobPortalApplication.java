package com.group04.GUI;

import javax.swing.*;

import com.group04.GUI.Admin.AdminDashboard;
import com.group04.GUI.Recruiter.RecruiterProfileScreen;
import com.group04.GUI.User.UserMerged;

import java.awt.*;
import java.awt.event.*;

public class JobPortalApplication {
    JFrame frame;
    CardLayout layout;
    JPanel containerPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JobPortalApplication::new);
    }

    public JobPortalApplication() {
        frame = new JFrame("Job Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        layout = new CardLayout();
        containerPanel = new JPanel(layout);

        containerPanel.add(createWelcomePanel(), "Welcome");
        containerPanel.add(createMainTabs(), "MainTabs");

        frame.add(containerPanel);
        frame.setVisible(true);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("LET'S START BUILDING A BETTER FUTURE.", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.DARK_GRAY);
        panel.add(title, BorderLayout.CENTER);

        JButton startButton = new JButton("LET'S BEGIN");
        startButton.setPreferredSize(new Dimension(150, 40));
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> layout.show(containerPanel, "MainTabs"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JTabbedPane createMainTabs() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));
        tabbedPane.setPreferredSize(new Dimension(frame.getWidth(), 60));
        tabbedPane.addTab("User", createLoginPanel("User"));
        tabbedPane.addTab("Recruiter", createLoginPanel("Recruiter"));
        tabbedPane.addTab("Admin", createLoginPanel("Admin"));
        tabbedPane.addTab("Contact Us", new JPanel());

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 3) {
                showContactPopup();
            }
        });

        return tabbedPane;
    }

    private JPanel createLoginPanel(String role) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(role + " Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField email = new JTextField(20);
        panel.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField password = new JPasswordField(20);
        panel.add(password, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        char defaultEcho = password.getEchoChar();
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.addActionListener(e -> password.setEchoChar(showPassword.isSelected() ? (char) 0 : defaultEcho));
        panel.add(showPassword, gbc);

        gbc.gridy++;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String inputEmail = email.getText();
            String inputPassword = new String(password.getPassword());

            boolean loginSuccess = false;

            if (role.equals("User")) {
                loginSuccess = inputEmail.equals("user@example.com") && inputPassword.equals("user123");
            } else if (role.equals("Recruiter")) {
                loginSuccess = inputEmail.equals("recruiter@example.com") && inputPassword.equals("recruit123");
            } else if (role.equals("Admin")) {
                loginSuccess = inputEmail.equals("admin@example.com") && inputPassword.equals("admin123");
            }

            if (loginSuccess) {
                JOptionPane.showMessageDialog(frame, role + " login successful!");

                switch (role) {
                    case "User":
                        SwingUtilities.invokeLater(() -> new UserMerged.UserProfileScreen());
                        break;
                    case "Recruiter":
                        new RecruiterProfileScreen().createAndShowGUI();
                        break;
                    case "Admin":
                        new AdminDashboard().createAndShowGUI();
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials for " + role + ".", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginButton, gbc);

        if (!role.equals("Admin")) {
            gbc.gridy++;
            JButton forgotButton = new JButton("Forgot password?");
            forgotButton.addActionListener(e -> showForgotPassword(role));
            panel.add(forgotButton, gbc);

            gbc.gridy++;
            JButton registerButton = new JButton("New here? Register Here");
            registerButton.addActionListener(e -> showRegistration(role));
            panel.add(registerButton, gbc);
        }

        return panel;
    }

    private void showContactPopup() {
        JFrame contactFrame = new JFrame("Contact Us");
        contactFrame.setSize(400, 200);
        contactFrame.setLocationRelativeTo(null);
        contactFrame.setLayout(new BorderLayout());
        contactFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea contactInfo = new JTextArea("Contact us:\nEmail: abc@outlook.ca\nToll-free: +1 800-800-8000");
        contactInfo.setFont(new Font("Arial", Font.PLAIN, 16));
        contactInfo.setEditable(false);
        contactInfo.setBackground(new Color(245, 245, 245));
        contactInfo.setMargin(new Insets(20, 20, 20, 20));

        contactFrame.add(contactInfo, BorderLayout.CENTER);
        contactFrame.setVisible(true);
    }

    private void showForgotPassword(String role) {
        JFrame forgotFrame = new JFrame(role + " - Forgot Password");
        forgotFrame.setSize(400, 250);
        forgotFrame.setLayout(new GridBagLayout());
        forgotFrame.setLocationRelativeTo(null);
        forgotFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        forgotFrame.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        forgotFrame.add(new JTextField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        forgotFrame.add(new JLabel("Security Question:"), gbc);
        gbc.gridx = 1;
        forgotFrame.add(new JComboBox<>(new String[] {
                "What is your mother's maiden name?",
                "What was your first pet’s name?",
                "What is your favorite book?"
        }), gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        forgotFrame.add(new JLabel("Answer:"), gbc);
        gbc.gridx = 1;
        forgotFrame.add(new JTextField(20), gbc);

        gbc.gridx = 1;
        gbc.gridy = ++row;
        forgotFrame.add(new JButton("Submit"), gbc);

        forgotFrame.setVisible(true);
    }

    private void showRegistration(String role) {
        JFrame regFrame = new JFrame(role + " Registration");
        regFrame.setSize(500, 500);
        regFrame.setLayout(new GridBagLayout());
        regFrame.setLocationRelativeTo(null);
        regFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        regFrame.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        regFrame.add(new JTextField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        regFrame.add(new JTextField(20), gbc);

        if (role.equals("Recruiter")) {
            gbc.gridx = 0;
            gbc.gridy = ++row;
            regFrame.add(new JLabel("Company Email:"), gbc);
            gbc.gridx = 1;
            regFrame.add(new JTextField(20), gbc);
            gbc.gridx = 0;
            gbc.gridy = ++row;
            regFrame.add(new JLabel("Company Mobile:"), gbc);
            gbc.gridx = 1;
            regFrame.add(new JTextField(20), gbc);
        } else {
            gbc.gridx = 0;
            gbc.gridy = ++row;
            regFrame.add(new JLabel("Email Address:"), gbc);
            gbc.gridx = 1;
            regFrame.add(new JTextField(20), gbc);
            gbc.gridx = 0;
            gbc.gridy = ++row;
            regFrame.add(new JLabel("Mobile Number:"), gbc);
            gbc.gridx = 1;
            regFrame.add(new JTextField(20), gbc);
            gbc.gridx = 0;
            gbc.gridy = ++row;
            regFrame.add(new JLabel("Date of Birth:"), gbc);
            gbc.gridx = 1;
            regFrame.add(new JTextField(20), gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Security Question:"), gbc);
        gbc.gridx = 1;
        regFrame.add(new JComboBox<>(new String[] {
                "What is your mother's maiden name?",
                "What was your first pet’s name?",
                "What is your favorite book?"
        }), gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Answer:"), gbc);
        gbc.gridx = 1;
        regFrame.add(new JTextField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        regFrame.add(new JPasswordField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        regFrame.add(new JPasswordField(20), gbc);

        gbc.gridx = 1;
        gbc.gridy = ++row;
        regFrame.add(new JButton("Sign Up"), gbc);

        regFrame.setVisible(true);
    }
}