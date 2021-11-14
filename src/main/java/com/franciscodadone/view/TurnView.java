package com.franciscodadone.view;

import com.franciscodadone.controller.TurnController;

import javax.swing.*;

public class TurnView {
    public JPanel panel;
    public JList productList;
    private JButton terminarTurnoButton;
    public JTextField codeField;
    private JTextField quantityField;
    private JList list1;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton archivarVentaButton;
    private JButton agregarProductoButton;

    public TurnView() {
        new TurnController(this);
    }

    public void focusField() {
        codeField.setText("");
        codeField.requestFocusInWindow();
    }

}
