package com.franciscodadone.view;

import com.franciscodadone.controller.MainScreenController;
import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.models.Session;
import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.util.JCustomOptionPane;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

public class MainScreen extends JFrame {
    private JPanel panel;
    public JButton startTurnButton;
    public JButton addStockButton;
    public JButton historyButton;
    public JComboBox printersComboBox;

    public MainScreen(boolean buildFrame) {
        if(buildFrame) {
            this.setContentPane(panel);
            this.pack();
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            this.setTitle("Control de Stock - Mundo Helado");
            this.setIconImage(new ImageIcon(getClass().getResource("/boxIcon.png")).getImage());

            this.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    Session activeSession = SessionsQueries.getActiveSession();
                    if(activeSession != null) {
                        int res = JCustomOptionPane.confirmDialog("Advertencia, hay un turno abierto. ¿Desea cerrarlo antes de salir?", "Turno abierto");
                        if(res == JOptionPane.YES_OPTION) {
                            DecimalFormat df = new DecimalFormat("#.##");

                            double earningsBox = SessionsQueries.getMoneyFromSessionBox(SessionsQueries.getActiveSession());
                            double earningsPosnet = SessionsQueries.getMoneyFromSessionPosnet(SessionsQueries.getActiveSession());
                            double totalEarnings = earningsBox + earningsPosnet;
                            double withdraw = SessionsQueries.getWithdrawFromSession(SessionsQueries.getActiveSession());
                            JCustomOptionPane.messageDialog(
                                    "<html>" +
                                            "<hr><b>Turno de: " + activeSession.getSeller() + "</b><br>" +
                                            "Ingresos totales: $" + df.format(totalEarnings) + "<br>" +
                                            "Ingresos totales - Retiros: $" + df.format(totalEarnings - withdraw) + "<br>" +
                                            "<hr><li><b>CAJA:</b>" +
                                            "   <ul>Ingresos: $" + df.format(earningsBox) + "</ul>" +
                                            "   <ul>Dinero inicial: $" + df.format(activeSession.getStartMoney()) + "</ul>" +
                                            "   <ul>Retiros de dinero: $" + df.format(withdraw) + "</ul>" +
                                            "   <ul>Inicio + Ingresos: $" + df.format(activeSession.getStartMoney() + earningsBox) + "</ul>" +
                                            "   <ul>Inicio + Ingresos - Retiros: $" + df.format(activeSession.getStartMoney() + earningsBox - withdraw) + "<br></ul>" +
                                            "</li><li><b>POSNET:</b> <br>" +
                                            "   <ul>Ingresos: $" + df.format(earningsPosnet) + "</ul>" +
                                            "</li><hr></html>",
                                    "Fin del turno",
                                    JOptionPane.PLAIN_MESSAGE
                            );

                            SessionsQueries.endCurrentSession();
                            MongoConnection.close();
                            System.exit(0);
                        } else if(res == JOptionPane.NO_OPTION) System.exit(0);
                    } else {
                        MongoConnection.close();
                        System.exit(0);
                    }
                }
            });
            this.setVisible(true);
        }
        new MainScreenController(this);
    }

    public JPanel getContentPanel() {
        return panel;
    }
}
