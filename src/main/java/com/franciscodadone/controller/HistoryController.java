package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.JLabelFont;
import com.franciscodadone.view.History;
import com.franciscodadone.view.HistoryDetailsPopup;
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

        descriptionPanel.setBackground(new Color(164,167,169));
        datePanel.setBackground(new Color(164,167,169));
        buttonsPanel.setBackground(new Color(164,167,169));

        SessionsQueries.getAllSessions().forEach(session -> {
            Font font = new Font("Arial", Font.PLAIN, 24);
            JButton btn = new JButton("Detalles");
            JLabelFont date = new JLabelFont("     (" + session.getDateStarted() + " - " + session.getDateEnded() + ")      ", font),
                    sellerName = new JLabelFont(session.getSeller(), font);

            JPanel subPanelCenter = new JPanel();
            subPanelCenter.add(sellerName);
            descriptionPanel.add(subPanelCenter);
            subPanelCenter.setBackground(new Color(164,167,169));
            subPanelCenter.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            btn.setFont(new Font("Arial", Font.BOLD, 16));
            JPanel subPanelRight = new JPanel();
            subPanelRight.add(btn);
            buttonsPanel.add(subPanelRight);
            subPanelRight.setBackground(new Color(164,167,169));
            subPanelRight.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JPanel subPanelLeft = new JPanel();
            subPanelLeft.add(date);
            datePanel.add(subPanelLeft);
            subPanelLeft.setBackground(new Color(164,167,169));
            subPanelLeft.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            btn.addActionListener(e -> {
                new HistoryDetailsPopup(session);
            });
        });
    }

    private static History view;

}
