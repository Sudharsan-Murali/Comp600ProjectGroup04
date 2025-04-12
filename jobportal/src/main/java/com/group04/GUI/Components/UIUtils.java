package com.group04.GUI.Components;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class UIUtils {
    public static JLabel createTitleLabel(String title, String iconPath) {
        try {
            BufferedImage icon = ImageIO.read(new File(iconPath));
            Image scaledIcon = icon.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(title, new ImageIcon(scaledIcon), JLabel.LEFT);
            label.setFont(UIConstants.TITLE_FONT);
            return label;
        } catch (Exception e) {
            JLabel label = new JLabel(title);
            label.setFont(UIConstants.TITLE_FONT);
            return label;
        }
    }
}