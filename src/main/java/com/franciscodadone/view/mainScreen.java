package com.franciscodadone.view;

import com.franciscodadone.util.GUIHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class mainScreen extends JFrame {
    private JPanel panel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;

    public mainScreen() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

//        this.setIconImage(new ImageIcon(getClass().getResource("/images/logo.jpg")).getImage());

        this.setVisible(true);
        button1.addActionListener(e -> GUIHandler.changeScreen(new turnoView().panel));
    }


    public Container getContentPanel() {
        return panel;
    }
}
