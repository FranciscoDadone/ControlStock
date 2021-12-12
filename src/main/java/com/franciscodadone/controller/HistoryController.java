package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.HistoryPanel;
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

        JPanel buttonsPanel = new JPanel();
        JPanel descriptionPanel = new JPanel();
        JPanel datePanel = new JPanel();

        buttonsPanel.setLayout(new VerticalLayout());
        descriptionPanel.setLayout(new VerticalLayout());
        datePanel.setLayout(new VerticalLayout());

        view.mainPanel.add(datePanel, BorderLayout.WEST);
        view.mainPanel.add(descriptionPanel, BorderLayout.CENTER);
        view.mainPanel.add(buttonsPanel, BorderLayout.EAST);

        descriptionPanel.setBackground(new Color(124,127,129));
        datePanel.setBackground(new Color(124,127,129));
        buttonsPanel.setBackground(new Color(124,127,129));

        SessionsQueries.getAllSessions().forEach(session -> {
            new HistoryPanel(session, datePanel, descriptionPanel, buttonsPanel);
        });
    }




    private static History view;

}
