package com.franciscodadone.view;

import com.franciscodadone.controller.MainScreenController;
import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.models.Session;
import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.JCustomOptionPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

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
//        this.setIconImage(new ImageIcon(getClass().getResource("/images/logo.jpg")).getImage());

            this.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    Session activeSession = SessionsQueries.getActiveSession();
                    if(activeSession != null) {
                        int res = JCustomOptionPane.confirmDialog("Advertencia, hay un turno abierto. ¿Desea cerrarlo antes de salir?", "Turno abierto");
                        if(res == JOptionPane.YES_OPTION) {
                            double endMoney = JCustomOptionPane.endSessionDialog();
                            if(endMoney != -1) {
                                SessionsQueries.endCurrentSession(endMoney);
                                JCustomOptionPane.messageDialog("Turno gurdado exitosamente!", "", JOptionPane.PLAIN_MESSAGE);
                                System.exit(0);
                            }
                        } else if(res == JOptionPane.NO_OPTION) System.exit(0);
                    } else System.exit(0);
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
