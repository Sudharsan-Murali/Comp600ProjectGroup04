package com.group04.GUI.Components;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;

public class ButtonFactory {
    public static JButton createSideButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(UIConstants.BUTTON_SIZE);
        button.setBackground(UIConstants.BUTTON_BG);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(UIConstants.BUTTON_FONT);
        button.addMouseListener(new HoverEffectMouseAdapter());
        return button;
    }
}