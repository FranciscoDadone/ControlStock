package com.franciscodadone.view;

import com.franciscodadone.controller.HistoryController;

import javax.swing.*;

public class History {
    public JPanel panel;
    public JButton backButton;
    public JPanel mainPanel;

    public History() {
        new HistoryController(this);
    }

}
