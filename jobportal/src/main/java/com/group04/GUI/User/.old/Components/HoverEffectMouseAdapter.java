package com.group04.GUI.Components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

public class HoverEffectMouseAdapter extends MouseAdapter {
    @Override
    public void mouseEntered(MouseEvent e) {
        ((JComponent) e.getSource()).setBackground(UIConstants.BUTTON_HOVER);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ((JComponent) e.getSource()).setBackground(UIConstants.BUTTON_BG);
    }
}