package com.franciscodadone.controller;

import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.view.AddStock;
import com.franciscodadone.view.MainScreen;
import com.franciscodadone.view.TurnView;

public class MainScreenController {

    public MainScreenController(MainScreen view) {
        this.view = view;

        view.startTurnButton.addActionListener(e -> {
            TurnView turn = new TurnView();
            GUIHandler.changeScreen(turn.panel);
            turn.focusField();
        });

        view.addStockButton.addActionListener(e -> {
            GUIHandler.changeScreen(new AddStock().panel);
        });
    }

    private MainScreen view;
}
