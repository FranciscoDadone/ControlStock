package com.franciscodadone.view;

import com.franciscodadone.util.GUIHandler;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    private JPanel panel;
    private JButton startTurnButton;
    private JButton addStockButton;
    private JButton modifyStockButton;
    private JButton historyButton;

    public MainScreen() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

//        this.setIconImage(new ImageIcon(getClass().getResource("/images/logo.jpg")).getImage());

        this.setVisible(true);
        startTurnButton.addActionListener(e -> {
            GUIHandler.changeScreen(new TurnView().panel);
        });
    }


    public Container getContentPanel() {
        return panel;
    }
}
