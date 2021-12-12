package com.franciscodadone.controller;

import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.HistoryPanel;
import com.franciscodadone.util.JLabelFont;
import com.franciscodadone.view.History;
import com.franciscodadone.view.MainScreen;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;

public class HistoryController {

    public HistoryController(History view) {
        this.view = view;

        handleButtons();
        setupPanels();

    }

    public static void handleButtons() {
        view.backButton.addActionListener(e -> {
            GUIHandler.changeScreen(new MainScreen(false).getContentPanel());
        });
    }

    public static void setupPanels() {
        view.mainPanel.setLayout(new VerticalLayout());



        view.mainPanel.add(new HistoryPanel());

    }




    private static History view;

}
