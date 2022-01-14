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
                            double earnings = SessionsQueries.getMoneyFromActiveSession();

                            DecimalFormat df = new DecimalFormat("#.##");

                            JCustomOptionPane.messageDialog(
                                    "<html>Turno de: " + activeSession.getSeller() + "<br>" +
                                            "Ingresos totales en el turno: $" + df.format(earnings) + "<br>" +
                                            "La caja inició con: $" + activeSession.getStartMoney() + "<br>" +
                                            "Inicio + Ingresos: $" + df.format(activeSession.getStartMoney() + earnings) + "</html>", "", JOptionPane.INFORMATION_MESSAGE);
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
