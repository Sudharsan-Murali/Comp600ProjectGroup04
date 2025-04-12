package com.group04.GUI;

import javax.swing.*;
import com.group04.DAO.UserDAO;
import com.group04.DAO.User;
import com.group04.GUI.Admin.AdminDashboard;
import com.group04.GUI.Recruiter.RecruiterProfileScreen;
import com.group04.GUI.User.UserProfileScreen;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JobPortalApplication {
    JFrame frame;
    CardLayout layout;
    JPanel containerPanel;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtMobile;
    private JTextField txtDob;

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
            if (tabbedPane.getSelectedIndex() == 3) { // index 3 for "Contact Us"
                showContactPopup(tabbedPane);
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
            

            UserDAO userDAO = new UserDAO();
            boolean loginSuccess = userDAO.loginUser(inputEmail, inputPassword); // Check login credentials
            // boolean loginSuccess = userDAO.loginUser(inputEmail, Password); //
            // Check login credentials

            if (loginSuccess) {
                // Get the roleId of the logged-in user
                int roleId = userDAO.getRoleId(inputEmail);

                // Check if the role matches the selected tab
                if ((role.equals("User") && roleId == 1) ||
                        (role.equals("Recruiter") && roleId == 2) ||
                        (role.equals("Admin") && roleId == 3)) {

                    // Navigate to the appropriate screen based on the roleId
                    switch (roleId) {
                        case 1: // User role
                            SwingUtilities.invokeLater(() -> new UserProfileScreen(inputEmail));
                            break;

                        case 2: // Recruiter role
                            new RecruiterProfileScreen(inputEmail).createAndShowGUI();
                            break;
                        case 3: // Admin role
                            new AdminDashboard().createAndShowGUI();
                            break;
                        default:
                            JOptionPane.showMessageDialog(frame, "Role not recognized.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials for the selected role.", "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid email or password.", "Login Failed",
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

    private void showContactPopup(JTabbedPane tabbedPane) {
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

        // Add window listener to reset the tab selection when popup is closed.
        contactFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Reset selected tab to the first tab (or any other tab you prefer)
                tabbedPane.setSelectedIndex(0);
            }

            public void windowClosing(java.awt.event.WindowEvent e) {
                // Also handle closing action to reset the tab selection
                tabbedPane.setSelectedIndex(0);
            }
        });

        contactFrame.setVisible(true);
    }

    private void showForgotPassword(String role) {
        // Step 1: Ask for email.
        JFrame emailFrame = new JFrame(role + " - Forgot Password");
        emailFrame.setSize(400, 200);
        emailFrame.setLocationRelativeTo(null);
        emailFrame.setLayout(new GridBagLayout());
        emailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        emailFrame.add(new JLabel("Enter your Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        emailFrame.add(emailField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JButton submitEmailButton = new JButton("Submit");
        emailFrame.add(submitEmailButton, gbc);

        submitEmailButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(emailFrame, "Please enter your email.");
                return;
            }
            UserDAO userDAO = new UserDAO();
            String securityQuestion = userDAO.getSecurityQuestion(email, role);
            if (securityQuestion != null && !securityQuestion.isEmpty()) {
                emailFrame.dispose(); // Close the email entry frame.
                showSecurityAnswerScreen(email, securityQuestion, role);
            } else {
                JOptionPane.showMessageDialog(emailFrame,
                        "No security question found for the provided email and role.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        emailFrame.setVisible(true);
    }

    private void showSecurityAnswerScreen(String email, String securityQuestion, String role) {
        // Step 2: Display the security question and ask for answer.
        JFrame answerFrame = new JFrame(role + " - Answer Security Question");
        answerFrame.setSize(450, 250);
        answerFrame.setLocationRelativeTo(null);
        answerFrame.setLayout(new GridBagLayout());
        answerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Display Email (optional)
        gbc.gridx = 0;
        gbc.gridy = row;
        answerFrame.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailDisplayField = new JTextField(email, 20);
        emailDisplayField.setEditable(false);
        answerFrame.add(emailDisplayField, gbc);

        // Display the security question in a non-editable field.
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        answerFrame.add(new JLabel("Security Question:"), gbc);

        gbc.gridx = 1;
        JTextField questionField = new JTextField(securityQuestion, 20);
        questionField.setEditable(false);
        answerFrame.add(questionField, gbc);

        // Input field for answer.
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        answerFrame.add(new JLabel("Your Answer:"), gbc);

        gbc.gridx = 1;
        JTextField answerField = new JTextField(20);
        answerFrame.add(answerField, gbc);

        // Submit Button
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JButton submitAnswerButton = new JButton("Submit");
        answerFrame.add(submitAnswerButton, gbc);

        submitAnswerButton.addActionListener(e -> {
            String userAnswer = answerField.getText().trim();
            if (userAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(answerFrame, "Please provide an answer.");
                return;
            }
            UserDAO userDAO = new UserDAO();
            String storedAnswer = userDAO.getSecurityAnswer(email, role);
            if (storedAnswer != null && storedAnswer.equalsIgnoreCase(userAnswer)) {
                answerFrame.dispose();
                showChangePasswordScreen(email, role);
            } else {
                JOptionPane.showMessageDialog(answerFrame, "Incorrect answer. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        answerFrame.setVisible(true);
    }

    private void showChangePasswordScreen(String email, String role) {
        // Declare the frame as final so that it can be referenced in the lambda.
        final JFrame changePwdFrame = new JFrame(role + " - Change Password");
        changePwdFrame.setSize(400, 250);
        changePwdFrame.setLocationRelativeTo(null);
        changePwdFrame.setLayout(new GridBagLayout());
        changePwdFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        changePwdFrame.add(new JLabel("New Password:"), gbc);
    
        gbc.gridx = 1;
        JPasswordField newPasswordField = new JPasswordField(20);
        changePwdFrame.add(newPasswordField, gbc);
    
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        changePwdFrame.add(new JLabel("Confirm Password:"), gbc);
    
        gbc.gridx = 1;
        JPasswordField confirmPasswordField = new JPasswordField(20);
        changePwdFrame.add(confirmPasswordField, gbc);
    
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JButton updatePwdButton = new JButton("Update Password");
        changePwdFrame.add(updatePwdButton, gbc);
    
        updatePwdButton.addActionListener(e -> {
            String newPwd = new String(newPasswordField.getPassword());
            String confirmPwd = new String(confirmPasswordField.getPassword());
            if (newPwd.isEmpty() || confirmPwd.isEmpty()) {
                JOptionPane.showMessageDialog(changePwdFrame, "Please fill in both fields.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPwd.equals(confirmPwd)) {
                JOptionPane.showMessageDialog(changePwdFrame, "Passwords do not match.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            UserDAO userDAO = new UserDAO();
            if (userDAO.updatePassword(email, newPwd, role)) {
                JOptionPane.showMessageDialog(changePwdFrame, "Password updated successfully!");
                changePwdFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(changePwdFrame, "Failed to update password. Please try again later.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        changePwdFrame.setVisible(true);
    }
    
    private void showRegistration(String role) {
        JFrame regFrame = new JFrame(role + " Registration");
        regFrame.setSize(500, 500);
        regFrame.setLayout(new GridBagLayout());
        regFrame.setLocationRelativeTo(null);
        regFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        regFrame.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        txtFirstName = new JTextField(20);
        regFrame.add(txtFirstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        txtLastName = new JTextField(20);
        regFrame.add(txtLastName, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        regFrame.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Mobile:"), gbc);
        gbc.gridx = 1;
        txtMobile = new JTextField(20);
        regFrame.add(txtMobile, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Date of Birth:"), gbc);
        gbc.gridx = 1;
        txtDob = new JTextField(20);
        regFrame.add(txtDob, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        regFrame.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField txtPassword = new JPasswordField(20);
        regFrame.add(txtPassword, gbc);
        JButton regButton = new JButton("Register");
        regButton.addActionListener(e -> {
            // Get the plain text password directly.
            String password = new String(txtPassword.getPassword());
            
            // Create the user object using the plain text password.
            User user = new User(txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(),
                    txtMobile.getText(), txtDob.getText(), password);
            
            UserDAO userDAO = new UserDAO();
            if (userDAO.registerUser(user)) {
                JOptionPane.showMessageDialog(null, "Registration Successful!");
                // Optionally dispose the registration form, if applicable.
            } else {
                JOptionPane.showMessageDialog(null, "Registration Failed. Try again later.");
            }
        });

        gbc.gridx = 1;
        gbc.gridy = ++row;
        regFrame.add(regButton, gbc);

        regFrame.setVisible(true);
    }
}
