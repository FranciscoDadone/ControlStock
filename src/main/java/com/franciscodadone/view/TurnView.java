package com.franciscodadone.view;

import com.franciscodadone.controller.TurnController;

import javax.swing.*;

public class TurnView {
    public JPanel panel;
    public JList productList;
    private JButton terminarTurnoButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JList list1;

    public TurnView() {
        new TurnController(this);

    }

}
