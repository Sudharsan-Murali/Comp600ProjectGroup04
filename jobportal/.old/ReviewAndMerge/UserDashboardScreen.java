package com.group04.merge;

import com.group04.GUI.User.UserDashboardScreen;
import com.group04.GUI.JobPortalApplication;
import com.group04.GUI.Recruiter.RecruiterProfileScreen;

import javax.swing.*;
import java.awt.*;

public class UserDashboardScreen {
    JFrame frame;
    CardLayout layout;
    JPanel containerPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JobPortalApplication::new);
    }

    public JobPortalUI() {
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
                frame.dispose();

                if (role.equals("User")) {
                    new UserDashboardScreen(); // User dashboard
                } else if (role.equals("Recruiter")) {
                    new RecruiterProfileScreen().createAndShowGUI(); // Recruiter dashboard
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials for " + role + ".", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(loginButton, gbc);

        return panel;
    }

    private void showContactPopup() {
        JOptionPane.showMessageDialog(frame,
                "Contact us:\nEmail: abc@outlook.ca\nToll-free: +1 800-800-8000",
                "Contact Us",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
