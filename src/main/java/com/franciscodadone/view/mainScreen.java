package com.franciscodadone.view;

import javax.swing.*;
import java.awt.*;

public class mainScreen extends JFrame {
    private JPanel panel;
    private JButton iniciarTurnoButton;
    private JButton agregarMercaderíaButton;
    private JButton modificarStockButton;
    private JButton historialButton;

    public mainScreen() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

//        this.setIconImage(new ImageIcon(getClass().getResource("/images/logo.jpg")).getImage());

        this.setVisible(true);
    }


    public Container getContentPanel() {
        return panel;
    }
}
