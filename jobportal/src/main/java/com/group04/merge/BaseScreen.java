package com.group04.merge;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumnModel;

// --- UIConstants ---
class UIConstants {
    // Colors
    public static final Color DARK_BG = new Color(33, 37, 41);
    public static final Color BUTTON_BG = new Color(52, 58, 64);
    public static final Color BUTTON_HOVER = new Color(73, 80, 87);
    
    // Dimensions
    public static final Dimension SIDE_PANEL_SIZE = new Dimension(200, 600);
    public static final Dimension BUTTON_SIZE = new Dimension(180, 40);
    
    // Fonts
    public static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
}

// --- HoverEffectMouseAdapter ---
class HoverEffectMouseAdapter extends MouseAdapter {
    @Override
    public void mouseEntered(MouseEvent e) {
        ((JComponent) e.getSource()).setBackground(UIConstants.BUTTON_HOVER);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ((JComponent) e.getSource()).setBackground(UIConstants.BUTTON_BG);
    }
}

// --- ButtonFactory ---
class ButtonFactory {
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

// --- UIUtils ---
class UIUtils {
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

// --- SidePanel ---
class SidePanel extends JPanel {
    public SidePanel(ActionListener listener) {
        setBackground(UIConstants.DARK_BG);
        setPreferredSize(UIConstants.SIDE_PANEL_SIZE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        String[] buttons = {"Profile", "Applications", "Search Jobs", "Logout"};
        for (String btn : buttons) {
            JButton button = ButtonFactory.createSideButton(btn);
            button.addActionListener(listener);
            add(Box.createRigidArea(new Dimension(0, 20)));
            add(button);
        }
    }

    public SidePanel(Object listener) {
        //TODO Auto-generated constructor stub
    }
}

// --- BaseScreen ---
public abstract class BaseScreen extends JFrame {
    public BaseScreen(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLayout(new BorderLayout());
        add(new SidePanel(this::handleNavigation), BorderLayout.WEST);
    }
    
    protected abstract void handleNavigation(ActionEvent e);
}