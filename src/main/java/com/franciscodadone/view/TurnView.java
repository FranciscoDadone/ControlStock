package com.franciscodadone.view;

import com.franciscodadone.controller.TurnController;

import javax.swing.*;

public class TurnView {
    public JPanel panel;
    public JList productList;
    public JButton endTurnButton;
    public JTextField codeField;
    public JTextField quantityField;
    public JList cartList;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    public JButton addSellButton;
    public JButton addProductButton;
    public JButton deleteProductButton;
    public JButton modifyQuantityButton;
    public JLabel price;
    public JLabel description;

    public TurnView() {
        new TurnController(this);
    }

    public void focusField() {
        codeField.setText("");
        codeField.requestFocusInWindow();
    }

}
