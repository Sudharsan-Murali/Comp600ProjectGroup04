/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.awt.*;

public class WelcomeScreen {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Job Portal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            // Background Panel
            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon backgroundImage = new ImageIcon("/mnt/data/Background.jpeg"); // Ensure the image file is in the correct path
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            };
            backgroundPanel.setLayout(new BorderLayout());
            
            // Header Panel
            JPanel headerPanel = new JPanel();
            headerPanel.setBackground(new Color(50, 150, 100, 180)); // Semi-transparent effect
            JLabel headerLabel = new JLabel("FROM HIRING TO RECRUITING, WE OFFER THE BEST EXPERIENCE FOR ALL OUR USERS.");
            headerLabel.setForeground(Color.WHITE);
            headerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            headerPanel.add(headerLabel);

            // Center Panel
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            centerPanel.setOpaque(false);
            
            JLabel welcomeLabel = new JLabel("LET’S START BUILDING A BETTER FUTURE.");
            welcomeLabel.setFont(new Font("Serif", Font.BOLD, 22));
            welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            welcomeLabel.setForeground(Color.WHITE);
            
            JButton beginButton = new JButton("LET’S BEGIN");
            beginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            beginButton.setBackground(new Color(100, 150, 250));
            beginButton.setForeground(Color.WHITE);
            beginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            beginButton.setFocusPainted(false);
            beginButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            
            centerPanel.add(Box.createVerticalStrut(120)); // Spacing
            centerPanel.add(welcomeLabel);
            centerPanel.add(Box.createVerticalStrut(30));
            centerPanel.add(beginButton);
            
            // Footer Panel
            JPanel footerPanel = new JPanel();
            footerPanel.setBackground(new Color(240, 240, 240, 180));
            JLabel footerLabel = new JLabel("© 2025 Job Portal. All rights reserved.");
            footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            footerPanel.add(footerLabel);

            // Adding components
            backgroundPanel.add(headerPanel, BorderLayout.NORTH);
            backgroundPanel.add(centerPanel, BorderLayout.CENTER);
            backgroundPanel.add(footerPanel, BorderLayout.SOUTH);
            
            frame.setContentPane(backgroundPanel);
            frame.setVisible(true);
        });
    }
}
