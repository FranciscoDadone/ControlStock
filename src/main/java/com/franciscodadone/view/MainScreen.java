package com.franciscodadone.view;

import com.franciscodadone.controller.MainScreenController;
import com.franciscodadone.util.GUIHandler;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    private JPanel panel;
    public JButton startTurnButton;
    public JButton addStockButton;
    public JButton historyButton;

    public MainScreen(boolean buildFrame) {
        new MainScreenController(this);

        if(buildFrame) {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setContentPane(panel);
            this.pack();
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);

//        this.setIconImage(new ImageIcon(getClass().getResource("/images/logo.jpg")).getImage());

            this.setVisible(true);
        }
    }

    public JPanel getContentPanel() {
        return panel;
    }
}
