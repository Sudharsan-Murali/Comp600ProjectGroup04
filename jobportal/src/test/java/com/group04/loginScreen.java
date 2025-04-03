import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class loginScreen {
    static JFrame frame = new JFrame("User Login");

    static JTextField userText = new JTextField(20);
    static JButton contactButton = new JButton("Contact Us");
    static JButton recruiterButton = new JButton("Recruiters");
    static JButton userButton = new JButton("User");
    static JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
    static JButton forgotPasswordButton = new JButton("Forgot password?");
    static JButton loginButton = new JButton("Login");
    static JPasswordField passwordText = new JPasswordField(20);

    public static void main(String[] args) 
        {   
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null); // Center the frame on the screen
    
            // Top Buttons Panel
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new GridLayout(1, 3)); // 3 buttons now: User, Recruiters, Contact Us
            
    
            userButton.setFont(new Font("Arial", Font.BOLD, 16));
            recruiterButton.setFont(new Font("Arial", Font.BOLD, 16));
            contactButton.setFont(new Font("Arial", Font.BOLD, 16));
    
            topPanel.add(userButton);
            topPanel.add(recruiterButton);
            topPanel.add(contactButton);
            frame.add(topPanel, BorderLayout.NORTH);
    
            // Main Panel (Centered Login Form)
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
    
            // Email Label (Left-aligned)
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST; // Left-align the label
            JLabel loginLabel = new JLabel("User Login");
            loginLabel.setFont(new Font("Arial", Font.BOLD, 20));
            panel.add(loginLabel, gbc);
    
            // Email Field
            gbc.gridy = 1;
            userText.setText("Enter your email");
            userText.setForeground(Color.GRAY);
            userText.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            userText.setFont(new Font("Arial", Font.PLAIN, 14));
            userText.setPreferredSize(new Dimension(250, 30));
            panel.add(userText, gbc);
    
            // Password Label
            gbc.gridy = 2;
            JLabel passwordLabel = new JLabel("Password");
            panel.add(passwordLabel, gbc);
    
            // Password Field
            gbc.gridy = 3;
            passwordText.setText("Password");
            passwordText.setForeground(Color.GRAY);
            passwordText.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
            passwordText.setFont(new Font("Arial", Font.PLAIN, 14));
            passwordText.setPreferredSize(new Dimension(250, 30));
            passwordText.setEchoChar((char) 0); // Show placeholder text
            panel.add(passwordText, gbc);
    
            // Placeholder Handling for Email Field
            userText.addFocusListener(new FocusListener() 
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
    
            // Placeholder Handling for Password Field
            passwordText.addFocusListener(new FocusListener() 
            {
                @Override
                public void focusGained(FocusEvent e) 
                {
                    if (String.valueOf(passwordText.getPassword()).equals("Password")) 
                    {
                        passwordText.setText("");
                        passwordText.setForeground(Color.BLACK);
                        passwordText.setEchoChar('*'); // Hide password when user starts typing
                    }
                }
    
                @Override
                public void focusLost(FocusEvent e) 
                {
                    if (String.valueOf(passwordText.getPassword()).trim().isEmpty())    
                    {
                        passwordText.setText("Password");
                        passwordText.setForeground(Color.GRAY);
                        passwordText.setEchoChar((char) 0); // Show text as placeholder
                    }
                }
            });
    
            // Show Password Checkbox
            gbc.gridy = 4;
            showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
            showPasswordCheckBox.setBackground(Color.WHITE);
            showPasswordCheckBox.setForeground(Color.GRAY);
            panel.add(showPasswordCheckBox, gbc);
    
            // Login Button
            gbc.gridy = 5;
            loginButton.setPreferredSize(new Dimension(250, 40));
            panel.add(loginButton, gbc);
    
            // Forgot Password Button
            gbc.gridy = 6;
            forgotPasswordButton.setPreferredSize(new Dimension(250, 40));
            panel.add(forgotPasswordButton, gbc);
    
            // New Registration Label
            gbc.gridy = 7;
            JButton registerButton = new JButton("New here? Register Here");
            registerButton.setFont(new Font("Arial", Font.BOLD, 14));
            registerButton.setForeground(Color.BLUE);
            panel.add(registerButton, gbc);
    
            frame.add(panel, BorderLayout.CENTER);
    
            registerButton.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    openRegistrationForm(frame, loginLabel.getText().equals("User Login"));
                }
            });
    
            recruiterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) 
        {
                    loginLabel.setText("Recruiter Login");
                }
            });
    
            userButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loginLabel.setText("User Login");
                }
            });
    
            frame.setVisible(true);

    private static void openRegistrationForm(JFrame frame, boolean isUser) 
        {
        JDialog registerDialog = new JDialog(frame, isUser ? "User Registration" : "Recruiter Registration", true);
        registerDialog.setSize(400, 500);
        registerDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] securityQuestions = 
        {
            "What is your mother's maiden name?",
            "What is the name of your first pet?",
            "What was the name of your elementary school?",
            "What was your childhood nickname?"
        };

        gbc.gridx = 0;
        gbc.gridy = 0;
        registerDialog.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        JTextField firstName = new JTextField(20);
        registerDialog.add(firstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerDialog.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        JTextField lastName = new JTextField(20);
        registerDialog.add(lastName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registerDialog.add(new JLabel(isUser ? "Email Address:" : "Company Email:"), gbc);
        gbc.gridx = 1;
        JTextField email = new JTextField(20);
        registerDialog.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        registerDialog.add(new JLabel(isUser ? "Mobile Number:" : "Company Mobile:"), gbc);
        gbc.gridx = 1;
        JTextField mobile = new JTextField(20);
        registerDialog.add(mobile, gbc);

        if (isUser) 
        {
            gbc.gridx = 0;
            gbc.gridy = 4;
            registerDialog.add(new JLabel("Date of Birth:"), gbc);
            gbc.gridx = 1;
            JTextField dob = new JTextField(20);
            registerDialog.add(dob, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 5;
        registerDialog.add(new JLabel("Security Question:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> secQuestion = new JComboBox<>(securityQuestions);
        registerDialog.add(secQuestion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        registerDialog.add(new JLabel("Answer:"), gbc);
        gbc.gridx = 1;
        JTextField answer = new JTextField(20);
        registerDialog.add(answer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        registerDialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField password = new JPasswordField(20);
        registerDialog.add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        registerDialog.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField confirmPassword = new JPasswordField(20);
        registerDialog.add(confirmPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        JButton signUpButton = new JButton("Sign Up");
        registerDialog.add(signUpButton, gbc);

        signUpButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                registerDialog.dispose();
            }
        });
            registerDialog.setVisible(true);        
        }
        
        // Contact us pop up 
        contactButton.addActionListener(new ActionListener() 
        {

    @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Create the Contact Us dialog
                JDialog contactDialog = new JDialog(frame, "Contact Us", true);
                contactDialog.setSize(300, 250); // Match dimensions from image
                contactDialog.setLocationRelativeTo(frame); // Center it

                // Create panel for the content
                JPanel contactPanel = new JPanel();
                contactPanel.setLayout(new GridBagLayout());
                contactPanel.setBackground(Color.WHITE);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.anchor = GridBagConstraints.WEST;

                // Contact Label
                gbc.gridx = 0;
                gbc.gridy = 0;
                JLabel contactLabel = new JLabel("Contact us:");
                contactLabel.setFont(new Font("Arial", Font.BOLD, 16));
                contactPanel.add(contactLabel, gbc);

                // Email Section
                gbc.gridy = 1;
                JLabel emailLabel = new JLabel("<html><font color='black'>Email: <font color='blue'><u>abc@outlook.ca</u></font></font></html>");
                emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
                emailLabel.setForeground(Color.BLUE); // Highlight the email text
                contactPanel.add(emailLabel, gbc);

                // Toll-Free Number Section
                gbc.gridy = 2;
                JLabel phoneLabel = new JLabel("<html>Toll free number: <u>+1 800-800-8000</u></html>");
                phoneLabel.setFont(new Font("Arial", Font.BOLD, 14));
                phoneLabel.setForeground(Color.BLACK); // Highlight the phone number text
                contactPanel.add(phoneLabel, gbc);


                contactDialog.add(contactPanel);
                contactDialog.setVisible(true);
            }
        }
}
        // Forgot Password Popup
        forgotPasswordButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                // Create the reset password dialog
                JDialog resetDialog = new JDialog(frame, "Reset Password", true);

                // Set the size to half the dimensions of the main frame
                int width = frame.getWidth() / 2;
                int height = frame.getHeight() / 2;
                resetDialog.setSize(width, height);

                // Center the dialog on the screen
                resetDialog.setLocationRelativeTo(frame);

                // Set dialog layout
                resetDialog.setLayout(new GridBagLayout());
                GridBagConstraints gbc2 = new GridBagConstraints();
                gbc2.insets = new Insets(10, 10, 10, 10);

                // Username Field
                gbc2.gridx = 0;
                gbc2.gridy = 0;
                gbc2.anchor = GridBagConstraints.WEST;
                JLabel usernameLabel = new JLabel("Username");
                resetDialog.add(usernameLabel, gbc2);

                gbc2.gridx = 1;
                gbc2.gridy = 0;
                JTextField usernameField = new JTextField(20);
                usernameField.setPreferredSize(new Dimension(250, 30));
                resetDialog.add(usernameField, gbc2);

                // Security Question Dropdown
                gbc2.gridx = 0;
                gbc2.gridy = 1;
                gbc2.anchor = GridBagConstraints.WEST;
                JLabel questionLabel = new JLabel("Security Question");
                resetDialog.add(questionLabel, gbc2);

                String[] questions = 
                {
                    "What is your mother's maiden name?",
                    "What is the name of your first pet?",
                    "What was the name of your elementary school?",
                    "What was your childhood nickname?"
                };
                JComboBox<String> securityQuestionComboBox = new JComboBox<>(questions);
                securityQuestionComboBox.setPreferredSize(new Dimension(250, 30));
                gbc2.gridx = 1;
                resetDialog.add(securityQuestionComboBox, gbc2);

                // Answer Field
                gbc2.gridx = 0;
                gbc2.gridy = 2;
                gbc2.anchor = GridBagConstraints.WEST;
                JLabel answerLabel = new JLabel("Answer");
                resetDialog.add(answerLabel, gbc2);

                gbc2.gridx = 1;
                JTextField answerField = new JTextField(20);
                answerField.setPreferredSize(new Dimension(250, 30));
                resetDialog.add(answerField, gbc2);

                // Submit Button to check answer
                gbc2.gridx = 1;
                gbc2.gridy = 3;
                JButton submitAnswerButton = new JButton("Submit Answer");
                resetDialog.add(submitAnswerButton, gbc2);

                // New Password and Confirm Password Fields (Initially hidden)
                gbc2.gridx = 0;
                gbc2.gridy = 4;
                gbc2.anchor = GridBagConstraints.WEST;
                JLabel newPasswordLabel = new JLabel("New Password");
                resetDialog.add(newPasswordLabel, gbc2);

                gbc2.gridx = 1;
                JPasswordField newPasswordField = new JPasswordField(20);
                newPasswordField.setPreferredSize(new Dimension(250, 30));
                resetDialog.add(newPasswordField, gbc2);

                gbc2.gridx = 0;
                gbc2.gridy = 5;
                gbc2.anchor = GridBagConstraints.WEST;
                JLabel confirmPasswordLabel = new JLabel("Confirm Password");
                resetDialog.add(confirmPasswordLabel, gbc2);

                gbc2.gridx = 1;
                JPasswordField confirmPasswordField = new JPasswordField(20);
                confirmPasswordField.setPreferredSize(new Dimension(250, 30));
                resetDialog.add(confirmPasswordField, gbc2);

                // Button to update password after answer is correct
                gbc2.gridx = 1;
                gbc2.gridy = 6;
                JButton updatePasswordButton = new JButton("Update Password");
                resetDialog.add(updatePasswordButton, gbc2);

                // Initially hide new password and confirm password fields
                newPasswordLabel.setVisible(false);
                newPasswordField.setVisible(false);
                confirmPasswordLabel.setVisible(false);
                confirmPasswordField.setVisible(false);
                updatePasswordButton.setVisible(false);

                // Action to verify answer and show password fields
                submitAnswerButton.addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent e) 
                        {
                        String selectedQuestion = (String) securityQuestionComboBox.getSelectedItem();
                        String answer = answerField.getText().trim();

                        // You can modify this for actual validation (e.g., check against a database)
                        if (selectedQuestion.equals("What is your mother's maiden name?") && answer.equals("Smith")) 
                        {
                            // Show password fields if the answer is correct
                            newPasswordLabel.setVisible(true);
                            newPasswordField.setVisible(true);
                            confirmPasswordLabel.setVisible(true);
                            confirmPasswordField.setVisible(true);
                            updatePasswordButton.setVisible(true);
                        }
                        else 
                        {
                            JOptionPane.showMessageDialog(resetDialog, "Incorrect answer. Please try again.");
                        }
                    }
                });

                // Action to update the password
                updatePasswordButton.addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent e) 
                        {
                        String newPassword = new String(newPasswordField.getPassword());
                        String confirmPassword = new String(confirmPasswordField.getPassword());

                        if (newPassword.equals(confirmPassword)) 
                        {
                            JOptionPane.showMessageDialog(resetDialog, "Password updated successfully.");
                            resetDialog.dispose();
                        } 
                        else 
                        {
                            JOptionPane.showMessageDialog(resetDialog, "Passwords do not match. Please try again.");
                        }
                    }
                });

                resetDialog.setVisible(true);
            }
        });

        // Simple Login Action (Placeholder)
        loginButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                // Here you can add real authentication logic
                if (username.equals("admin") && password.equals("admin123")) 
                {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                } 
                else 
                {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password.");
                }
            }
        });

        // Action to toggle password visibility
        showPasswordCheckBox.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                if (showPasswordCheckBox.isSelected()) 
                    {
                        passwordText.setEchoChar((char) 0); // Show the password
                    } 
                else 
                    {
                            passwordText.setEchoChar('*'); // Hide the password
                    }
            }
        });

        // Change "User Login" label to "Recruiter Login" when "Recruiters" button is clicked

        recruiterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginLabel.setText("Recruiter Login"); // Corrected line
            }
        });

        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginLabel.setText("User Login"); // Corrected line
            }
        });

// Ensure braces match for all action listeners and methods


        frame.setVisible(true);
}