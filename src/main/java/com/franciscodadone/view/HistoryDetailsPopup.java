package com.franciscodadone.view;

import com.franciscodadone.model.local.queries.SellQueries;
import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.models.Session;
import com.franciscodadone.util.JLabelFont;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class HistoryDetailsPopup extends JFrame {

    private JPanel panel1;
    private JScrollPane scrollPane;
    private JLabel sellerLabel;
    private JLabel startMoneyLabel;
    private JLabel endMoneyLabel;
    private JLabel earningsLabel;
    private JPanel panelScroll;
    private JLabel earningsWithWithdrawsLabel;
    private JLabel withdrawsLabel;
    private JLabel earningsBoxLabel;
    private JLabel earningsPosnetLabel;
    private JLabel earningsWithdrawsLabel;
    private Session session;

    public HistoryDetailsPopup(Session session) {
        this.session = session;

        this.setVisible(true);
        this.setBounds(20,20,900,700);
        this.setContentPane(panel1);
        this.setTitle(session.getDateStarted() + " - " + session.getDateEnded());

        setupHeader();
        showSells();
    }

    private void setupHeader() {

        DecimalFormat format = new DecimalFormat("#.##");

        double withdraws = SessionsQueries.getWithdrawFromSession(session);
        double deposits = SessionsQueries.getDepositsFromSession(session);
        double earningsBox = SessionsQueries.getMoneyFromSessionBox(session);
        double earningsPosnet = SessionsQueries.getMoneyFromSessionPosnet(session);
        double earningsTotal = earningsBox + earningsPosnet;


        sellerLabel.setText("Vendedor: " + session.getSeller());
        earningsBoxLabel.setText("Ganancias caja: $" + earningsBox);
        earningsPosnetLabel.setText("Ganancias posnet: $" + earningsPosnet);
        startMoneyLabel.setText("Inicio de la caja: $" + session.getStartMoney());
        endMoneyLabel.setText("Inicio + Ganancias: $" + format.format(session.getStartMoney() + earningsBox));
        earningsLabel.setText("Ganancias del turno: $" + earningsTotal);
        earningsWithdrawsLabel.setText("Ganancias del turno - Retiros: $" + format.format(earningsTotal - withdraws));
        withdrawsLabel.setText("Retiros: $" + withdraws);
        withdrawsLabel.setText("Ingresos: $" + deposits);
        earningsWithWithdrawsLabel.setText("Inicio + Ganancias - Retiros: $" + format.format(session.getStartMoney() + earningsBox - withdraws));
    }

    private void showSells() {
        panelScroll.setLayout(new VerticalLayout());
        AtomicInteger i = new AtomicInteger();

        SellQueries.getAllSellsFromSession(session).forEach(sell -> {
            i.getAndIncrement();
            JPanel sellPanel = new JPanel();
            sellPanel.setLayout(new VerticalLayout());
            sellPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.black),
                    "Venta #" + i + " Total: $" + sell.getPrice()
            ));

            sell.getProducts().forEach(product -> {
                Font font = new Font("Arial", Font.PLAIN, 22);

                JPanel prod = new JPanel();
                prod.setLayout(new BorderLayout());
                prod.add(new JLabelFont(product.getProdName(), font), BorderLayout.WEST);
                prod.add(new JLabelFont("(" + product.getQuantity() + ")", font), BorderLayout.EAST);

                sellPanel.add(prod);
            });
            panelScroll.add(sellPanel);

        });
    }
}
