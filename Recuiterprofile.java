package com.group04;

import javax.swing.*;
import java.awt.*;

public class Recuiterprofile {
    public static void main(String[] args) {
        JFrame frame = new JFrame("User Profile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(null);
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(60, 20, 20));
        leftPanel.setBounds(0, 0, 100, 400);
        frame.add(leftPanel);
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(220, 220, 220));
        mainPanel.setBounds(100, 0, 400, 400);
        mainPanel.setLayout(null);
        frame.add(mainPanel);
        JPanel profilePic = new JPanel();
        profilePic.setBackground(new Color(180, 80, 80));
        profilePic.setBounds(150, 20, 80, 50);
        mainPanel.add(profilePic);
        JButton editBtn = new JButton();
        editBtn.setBackground(new Color(100, 200, 200));
        editBtn.setBounds(140, 80, 20, 20);
        mainPanel.add(editBtn);

        JButton deleteBtn = new JButton();
        deleteBtn.setBackground(new Color(0, 0, 200));
        deleteBtn.setBounds(180, 80, 20, 20);
        mainPanel.add(deleteBtn);

        JLabel editLabel = new JLabel("edit");
        editLabel.setBounds(140, 100, 30, 20);
        mainPanel.add(editLabel);

        JLabel deleteLabel = new JLabel("delete");
        deleteLabel.setBounds(180, 100, 40, 20);
        mainPanel.add(deleteLabel);

        String[] labels = { "FNAME", "LNAME", "EMAIL", "COMPANY NAME", "COMPANY ADDRESS",
                "COMPANY PHONE", "COMPANY LINKEDIN", "COMPANY WEBSITE" };

        int yOffset = 130;
        for (String label : labels) {
            JTextField textField = new JTextField();
            textField.setBounds(20, yOffset, 150, 20);
            textField.setBackground(Color.BLACK);
            textField.setForeground(Color.WHITE);
            mainPanel.add(textField);

            JLabel lbl = new JLabel(label);
            lbl.setBounds(180, yOffset, 150, 20);
            mainPanel.add(lbl);

            yOffset += 30;
        }

        JButton saveBtn = new JButton("SAVE");
        saveBtn.setBounds(140, 360, 100, 20);
        saveBtn.setBackground(new Color(230, 150, 150));
        mainPanel.add(saveBtn);

        frame.setVisible(true);
    }
}
