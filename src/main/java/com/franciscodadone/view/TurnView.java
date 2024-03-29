package com.franciscodadone.view;

import com.franciscodadone.controller.TurnController;
import com.franciscodadone.model.models.Session;

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
    public JLabel sessionStartLabel;
    public JLabel dateNowLabel;
    public JButton withdrawMoney;
    public JButton addSellPosnetButton;
    public JCheckBox printTicket;
    public JButton depositMoney;

    public TurnView(Session session) {
        new TurnController(this, session);
    }

    public void focusField() {
        codeField.setText("");
        codeField.requestFocusInWindow();
    }
}
