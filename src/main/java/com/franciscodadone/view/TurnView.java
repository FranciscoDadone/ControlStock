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
    public JTextField exchangeField;
    public JTextField dateField;
    public JButton addSellButton;
    public JButton addProductButton;
    public JButton deleteProductButton;
    public JButton modifyQuantityButton;
    public JLabel price;
    public JLabel description;
    public JLabel totalLabel;
    public JLabel exchangeLabel;
    public JButton backButton;
    public JLabel sellerNameField;

    public TurnView(String sellerName, double money) {
        new TurnController(this, sellerName, money);
    }

    public void focusField() {
        codeField.setText("");
        codeField.requestFocusInWindow();
    }

}
