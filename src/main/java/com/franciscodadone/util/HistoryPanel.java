package com.franciscodadone.util;

import com.franciscodadone.models.Session;

import javax.swing.*;
import java.awt.*;

public class HistoryPanel {

    public HistoryPanel(Session session, JPanel datesPanel, JPanel descriptionPanel, JPanel buttonsPanel) {

        Font font = new Font("Arial", Font.PLAIN, 24);
        JButton btn = new JButton("Detalles");
        JLabelFont date = new JLabelFont("     (" + session.getDateStarted() + " - " + session.getDateEnded() + ")      ", font),
                sellerName = new JLabelFont(session.getSeller(), font);

        JPanel subPanelCenter = new JPanel();
        subPanelCenter.add(sellerName);
        descriptionPanel.add(subPanelCenter);
        subPanelCenter.setBackground(new Color(164,167,169));
        subPanelCenter.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        btn.setFont(new Font("Arial", Font.BOLD, 15));
        JPanel subPanelRight = new JPanel();
        subPanelRight.add(btn);
        buttonsPanel.add(subPanelRight);
        subPanelRight.setBackground(new Color(164,167,169));
        subPanelRight.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel subPanelLeft = new JPanel();
        subPanelLeft.add(date);
        datesPanel.add(subPanelLeft);
        subPanelLeft.setBackground(new Color(164,167,169));
        subPanelLeft.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        btn.addActionListener(e -> {
            System.out.println(sellerName.getText());
        });

    }

}
