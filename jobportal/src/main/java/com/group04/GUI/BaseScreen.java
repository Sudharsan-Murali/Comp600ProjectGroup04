package com.group04.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import com.group04.GUI.Components.SidePanel;

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