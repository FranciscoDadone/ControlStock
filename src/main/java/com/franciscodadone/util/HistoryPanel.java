package com.franciscodadone.util;

import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends JPanel {

    public HistoryPanel() {
        JButton btn = new JButton("Detalles");
        Font font = new Font("Arial", Font.PLAIN, 24);
        JLabelFont date = new JLabelFont("     12/12/2021 (13:30hs - 18:30hs)      ", font),
                sellerName = new JLabelFont("     Vendedor: Lorenzo     ", font),
                startMoney = new JLabelFont("     Inicio caja: $10000     ", font),
                endMoney = new JLabelFont("     Fin caja: $30000     ", font);

        JPanel panelVertical = new JPanel();
        panelVertical.setLayout(new BoxLayout(panelVertical, BoxLayout.X_AXIS));

        JPanel panelHorizontal = new JPanel();
        panelHorizontal.setLayout(new BorderLayout());
        panelVertical.add(panelHorizontal);

        JPanel subPanelHorizontal = new JPanel();
        subPanelHorizontal.setLayout(new FlowLayout());

        subPanelHorizontal.add(sellerName);
        subPanelHorizontal.add(startMoney);
        subPanelHorizontal.add(endMoney);
        panelHorizontal.add(subPanelHorizontal, BorderLayout.CENTER);

        JPanel datePanel = new JPanel();
        JPanel btnPanel = new JPanel();

        panelVertical.setBackground(new Color(185,188,190));
        panelHorizontal.setBackground(new Color(185,188,190));
        subPanelHorizontal.setBackground(new Color(185,188,190));
        datePanel.setBackground(new Color(185,188,190));
        btnPanel.setBackground(new Color(185,188,190));

        datePanel.add(date);
        btnPanel.add(btn);
        panelHorizontal.add(datePanel, BorderLayout.WEST);
        panelHorizontal.add(btnPanel, BorderLayout.EAST);

        datePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        subPanelHorizontal.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        btn.setFont(font);
        panelHorizontal.setMaximumSize(panelHorizontal.getPreferredSize());
        panelVertical.setMaximumSize(panelVertical.getPreferredSize());
        this.add(panelVertical);
        this.setBackground(new Color(124,127,129));
    }

}
