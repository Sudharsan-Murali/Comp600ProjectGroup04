package com.group04.GUI.Components;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SidePanel extends JPanel {
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