package com.franciscodadone.controller;

import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.JCustomOptionPane;
import com.franciscodadone.view.*;

public class MainScreenController {

    public MainScreenController(MainScreen view) {
        this.view = view;

        view.startTurnButton.addActionListener(e -> {
            Object[] nameAndMoney = JCustomOptionPane.inputDialogMultiLine();
            if(nameAndMoney != null) {
                String sellerName = (String)nameAndMoney[0];
                double money = Double.parseDouble(nameAndMoney[1].toString());

                TurnView turn = new TurnView(sellerName, money);
                GUIHandler.changeScreen(turn.panel);
                turn.focusField();
            }
        });

        view.addStockButton.addActionListener(e -> {
            GUIHandler.changeScreen(new AddModifyStock().panel);
        });
    }

    private MainScreen view;
}
