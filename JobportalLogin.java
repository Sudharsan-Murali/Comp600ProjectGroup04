/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JobportalLogin 
{
    static JFrame frame = new JFrame("User Login");
    static JTextField userText = new JTextField(20);
    static JButton contactButton = new JButton("Contact Us");
    static JButton recruiterButton = new JButton("Recruiters");
    static JButton userButton = new JButton("User");
    static JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
    static JButton forgotPasswordButton = new JButton("Forgot password?");
    static JButton loginButton = new JButton("Login");
    static JPasswordField passwordText = new JPasswordField(20);
    static JLabel loginLabel = new JLabel("User Login");

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(JobportalLogin::createAndShowGUI);
    }

        private static void createAndShowGUI() 
        {
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);

            // === Top Panel ===
            JPanel topPanel = new JPanel(new GridLayout(1, 3));
            userButton.setFont(new Font("Arial", Font.BOLD, 16));
            recruiterButton.setFont(new Font("Arial", Font.BOLD, 16));
            contactButton.setFont(new Font("Arial", Font.BOLD, 16));
            topPanel.add(userButton);
            topPanel.add(recruiterButton);
            topPanel.add(contactButton);
            frame.add(topPanel, BorderLayout.NORTH);

            // === Centered Login Panel ===
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            // === Login Label ===
            gbc.gridx = 0;
            gbc.gridy = 0;
            loginLabel.setFont(new Font("Arial", Font.BOLD, 20));
            panel.add(loginLabel, gbc);

            // === Email Field ===
            gbc.gridy = 1;
            userText.setText("Enter your email");
            userText.setForeground(Color.GRAY);
            userText.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            userText.setPreferredSize(new Dimension(250, 30));
            panel.add(userText, gbc);

            // === Password Field ===
            gbc.gridy = 2;
            JLabel passwordLabel = new JLabel("Password");
            panel.add(passwordLabel, gbc);

            gbc.gridy = 3;
            passwordText.setText("Password");
            passwordText.setForeground(Color.GRAY);
            passwordText.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            passwordText.setPreferredSize(new Dimension(250, 30));
            passwordText.setEchoChar((char) 0);
            panel.add(passwordText, gbc);

            // === Show Password Checkbox ===
            gbc.gridy = 4;
            showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
            panel.add(showPasswordCheckBox, gbc);

            showPasswordCheckBox.addActionListener(e -> 
            {
                if (showPasswordCheckBox.isSelected()) 
                {
                    passwordText.setEchoChar((char) 0); // Show password
                } 
                else 
                {
                    passwordText.setEchoChar('*'); // Hide password
                }
            });

            // === Login Button ===
            gbc.gridy = 5;
            loginButton.setPreferredSize(new Dimension(250, 40));
            panel.add(loginButton, gbc);

            // === Forgot Password Button ===
            gbc.gridy = 6;
            forgotPasswordButton.setPreferredSize(new Dimension(250, 40));
            panel.add(forgotPasswordButton, gbc);

            // Add Action Listener for "Forgot Password" button
            forgotPasswordButton.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                openForgotPasswordDialog(frame);
                }
                });
                // === Register Button ===
                gbc.gridy = 7;
                JButton registerButton = new JButton("New here? Register Here");
                registerButton.setFont(new Font("Arial", Font.BOLD, 14));
                registerButton.setForeground(Color.BLUE);
                panel.add(registerButton, gbc);

                registerButton.addActionListener(new ActionListener() 
                {
                public void actionPerformed(ActionEvent e) 
                {
                openRegistrationForm(frame, loginLabel.getText().equals("User Login"));
                }
            });

            frame.add(panel, BorderLayout.CENTER);

            // === Placeholder Handling for Email Field ===
            userText.addFocusListener(new FocusAdapter() 
            {
                @Override
                public void focusGained(FocusEvent e) 
                {
                    if (userText.getText().equals("Enter your email")) 
                    {
                        userText.setText("");
                        userText.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) 
                {
                    if (userText.getText().trim().isEmpty()) 
                    {
                        userText.setText("Enter your email");
                        userText.setForeground(Color.GRAY);
                    }
                }
            });

            // === Placeholder Handling for Password Field ===
            passwordText.addFocusListener(new FocusAdapter() 
            {
                @Override
                public void focusGained(FocusEvent e) 
                {
                    if (String.valueOf(passwordText.getPassword()).equals("Password")) 
                    {
                        passwordText.setText("");
                        passwordText.setForeground(Color.BLACK);
                        passwordText.setEchoChar('*'); // Hide password
                    }
                }

                @Override
                public void focusLost(FocusEvent e) 
                {
                    if (String.valueOf(passwordText.getPassword()).trim().isEmpty()) 
                    {
                        passwordText.setText("Password");
                        passwordText.setForeground(Color.GRAY);
                        passwordText.setEchoChar((char) 0);
                    }
                }
            });

            // === Recruiter/User Login Toggle ===
            recruiterButton.addActionListener(e -> loginLabel.setText("Recruiter Login"));
            userButton.addActionListener(e -> loginLabel.setText("User Login"));

            // === Contact Us Pop-up ===
            contactButton.addActionListener(e -> showContactDialog());

            frame.setVisible(true);
        }
        private static void showContactDialog() 
        {
            JDialog contactDialog = new JDialog(frame, "Contact Us", true);
            contactDialog.setSize(300, 250);
            contactDialog.setLocationRelativeTo(frame);

            JPanel contactPanel = new JPanel(new GridBagLayout());
            contactPanel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel contactLabel = new JLabel("Contact us:");
            contactLabel.setFont(new Font("Arial", Font.BOLD, 16));
            contactPanel.add(contactLabel, gbc);

            gbc.gridy = 1;
            JLabel emailLabel = new JLabel("<html>Email: <font color='blue'><u>abc@outlook.ca</u></font></html>");
            emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
            contactPanel.add(emailLabel, gbc);

            gbc.gridy = 2;
            JLabel phoneLabel = new JLabel("<html>Toll-free: <u>+1 800-800-8000</u></html>");
            phoneLabel.setFont(new Font("Arial", Font.BOLD, 14));
            contactPanel.add(phoneLabel, gbc);

            contactDialog.add(contactPanel);
            contactDialog.setVisible(true);
        }
        private static void openRegistrationForm(JFrame frame, boolean isUser) 
        {
            JDialog registerDialog = new JDialog(frame, isUser ? "User Registration" : "Recruiter Registration", true);
            registerDialog.setSize(500, 500);
            registerDialog.setLocationRelativeTo(frame); // Center the dialog
            registerDialog.setLayout(new GridBagLayout());

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            String[] securityQuestions = 
            {
                "What is your mother's maiden name?",
                "What is the name of your first pet?",
                "What was the name of your elementary school?",
                "What was your childhood nickname?"
            };

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("First Name:"), gbc);
            gbc.gridx = 1;
            JTextField firstName = new JTextField();
            firstName.setPreferredSize(new Dimension(300, 30)); // 300px width, 30px height
            panel.add(firstName, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Last Name:"), gbc);
            gbc.gridx = 1;
            JTextField lastName = new JTextField();
            lastName.setPreferredSize(new Dimension(300, 30)); // 300px width, 30px height
            panel.add(lastName, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel(isUser ? "Email Address:" : "Company Email:"), gbc);
            gbc.gridx = 1;
            JTextField email = new JTextField();
            email.setPreferredSize(new Dimension(300, 30)); // 300px width, 30px height 
            panel.add(email, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel(isUser ? "Mobile Number:" : "Company Mobile:"), gbc);
            gbc.gridx = 1;
            JTextField mobile = new JTextField();
            mobile.setPreferredSize(new Dimension(300, 30)); // 300px width, 30px height 

            panel.add(mobile, gbc);

            if (isUser) 
            {
                gbc.gridx = 0;
                gbc.gridy = 4;
                panel.add(new JLabel("Date of Birth:"), gbc);
                gbc.gridx = 1;
                JTextField dob = new JTextField();
                dob.setPreferredSize(new Dimension(300, 30)); // 300px width, 30px height 
                panel.add(dob, gbc);
            }

            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(new JLabel("Security Question:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> secQuestion = new JComboBox<>(securityQuestions);
            panel.add(secQuestion, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            panel.add(new JLabel("Answer:"), gbc);
            gbc.gridx = 1;
            JTextField answer = new JTextField();
            answer.setPreferredSize(new Dimension(300, 30)); // 300px width, 30px height 
            panel.add(answer, gbc);

            gbc.gridx = 0;
            gbc.gridy = 7;
            panel.add(new JLabel("Password:"), gbc);
            gbc.gridx = 1;
            JPasswordField password = new JPasswordField();
            password.setPreferredSize(new Dimension(300, 30)); // 300px width, 30px height 
            panel.add(password, gbc);

            gbc.gridx = 0;
            gbc.gridy = 8;
            panel.add(new JLabel("Confirm Password:"), gbc);
            gbc.gridx = 1;
            JPasswordField confirmPassword = new JPasswordField();
            confirmPassword.setPreferredSize(new Dimension(300, 30)); // 300px width, 30px height 
            panel.add(confirmPassword, gbc);

            gbc.gridx = 1;
            gbc.gridy = 9;
            JButton signUpButton = new JButton("Sign Up");
            panel.add(signUpButton, gbc);

            registerDialog.add(panel);
            registerDialog.setVisible(true);

            // Sign-Up Button Logic
            signUpButton.addActionListener(e -> 
            {
                String pass = new String(password.getPassword());
                String confirmPass = new String(confirmPassword.getPassword());
                String securityQuestion = secQuestion.getSelectedItem().toString();

                if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || email.getText().isEmpty() || 
                    mobile.getText().isEmpty() || answer.getText().isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(registerDialog, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!pass.equals(confirmPass)) 
                {
                    JOptionPane.showMessageDialog(registerDialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                } else 
                {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "Registration Successful!\nSecurity Question: " + securityQuestion, 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    registerDialog.dispose();
                }
            });
        }
        private static void openForgotPasswordDialog(JFrame frame) 
        {
            JDialog forgotPasswordDialog = new JDialog(frame, "Forgot Password", true);
            forgotPasswordDialog.setSize(450, 450);
            forgotPasswordDialog.setLocationRelativeTo(frame);
            forgotPasswordDialog.setLayout(new GridBagLayout());

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            String[] securityQuestions = 
            {
                "What is your mother's maiden name?",
                "What is the name of your first pet?",
                "What was the name of your elementary school?",
                "What was your childhood nickname?"
            };

            // Username/Email Input
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Username/Email:"), gbc);
            gbc.gridx = 1;
            JTextField emailField = new JTextField(20);
            panel.add(emailField, gbc);

            // Security Question Dropdown
            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Security Question:"), gbc);
            gbc.gridx = 1;
            JComboBox<String> secQuestionBox = new JComboBox<>(securityQuestions);
            panel.add(secQuestionBox, gbc);

            // Security Answer Input
            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("Answer:"), gbc);
            gbc.gridx = 1;
            JTextField answerField = new JTextField(20);
            panel.add(answerField, gbc);

            // Submit Button
            gbc.gridx = 1; gbc.gridy = 3;
            JButton submitButton = new JButton("Submit");
            panel.add(submitButton, gbc);

            forgotPasswordDialog.add(panel);
            forgotPasswordDialog.setVisible(true);

            // Submit Button Action Listener
            submitButton.addActionListener(e -> 
            {
                String email = emailField.getText();
                String securityQuestion = secQuestionBox.getSelectedItem().toString();
                String answer = answerField.getText();

                // Dummy validation - Replace this with database check
                if (email.isEmpty() || answer.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(forgotPasswordDialog, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                else if (answer.equalsIgnoreCase("correctAnswer")) 
                {
                    // Replace with actual verification logic
                    forgotPasswordDialog.dispose();
                    openResetPasswordDialog(frame, email);
                } 
                else 
                {
                    JOptionPane.showMessageDialog(forgotPasswordDialog, "Incorrect answer!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        // Reset Password Dialog
        private static void openResetPasswordDialog(JFrame frame, String email) 
        {
            JDialog resetPasswordDialog = new JDialog(frame, "Reset Password", true);
            resetPasswordDialog.setSize(400, 250);
            resetPasswordDialog.setLocationRelativeTo(frame);
            resetPasswordDialog.setLayout(new GridBagLayout());

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            // New Password
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("New Password:"), gbc);
            gbc.gridx = 1;
            JPasswordField newPasswordField = new JPasswordField(20);
            panel.add(newPasswordField, gbc);

            // Confirm Password
            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Confirm Password:"), gbc);
            gbc.gridx = 1;
            JPasswordField confirmPasswordField = new JPasswordField(20);
            panel.add(confirmPasswordField, gbc);

            // Reset Button
            gbc.gridx = 1; gbc.gridy = 2;
            JButton resetButton = new JButton("Reset Password");
            panel.add(resetButton, gbc);

            resetPasswordDialog.add(panel);
            resetPasswordDialog.setVisible(true);

            // Reset Button Action Listener
            resetButton.addActionListener(e -> 
            {
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(resetPasswordDialog, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                else if (!newPassword.equals(confirmPassword)) 
                {
                    JOptionPane.showMessageDialog(resetPasswordDialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                else 
                {
                    JOptionPane.showMessageDialog(resetPasswordDialog, "Password reset successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    resetPasswordDialog.dispose();
                }
            });
        }
}