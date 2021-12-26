package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.ProductsQueries;
import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.models.Session;
import com.franciscodadone.util.FDate;
import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.JCustomOptionPane;
import com.franciscodadone.view.*;
import javax.swing.*;

public class MainScreenController {

    public MainScreenController(MainScreen view) {
        this.view = view;

        view.startTurnButton.addActionListener(e -> {
            Session activeSession = SessionsQueries.getActiveSession();

            if(activeSession != null) {
                int res = JCustomOptionPane.confirmDialog("Hay un turno activo. ¿Desea seguir trabajando en él? O iniciar uno nuevo", "Turno activo");
                if(res == JOptionPane.YES_OPTION) {
                    TurnView turn = new TurnView(activeSession);
                    GUIHandler.changeScreen(turn.panel);
                    turn.focusField();
                } else if(res == JOptionPane.NO_OPTION) {
                    double earnings = SessionsQueries.getMoneyFromActiveSession();
                    JCustomOptionPane.messageDialog(
                            "<html>Turno de: " + activeSession.getSeller() + "<br>" +
                                    "Ingresos totales en el turno: $" + earnings + "<br>" +
                                    "La caja inició con: $" + activeSession.getStartMoney() + "<br>" +
                                    "Inicio + Ingresos: $" + (activeSession.getStartMoney() + earnings) + "</html>", "", JOptionPane.INFORMATION_MESSAGE);
                    SessionsQueries.endCurrentSession();
                }
            } else {
                Object[] nameAndMoney = JCustomOptionPane.startSessionDialog();
                if(nameAndMoney != null) {
                    String sellerName = (String)nameAndMoney[0];
                    double money = Double.parseDouble(nameAndMoney[1].toString());

                    TurnView turn = new TurnView(SessionsQueries.startSession(money, sellerName, new FDate()));
                    GUIHandler.changeScreen(turn.panel);
                    turn.focusField();
                }
            }

        });

        view.addStockButton.addActionListener(e -> {
            GUIHandler.changeScreen(new AddModifyStock().panel);
        });

        view.historyButton.addActionListener(e -> {
            GUIHandler.changeScreen(new History().panel);
        });

        lowStockNotification();
    }

    private void lowStockNotification() {
        String lowStockStr = "";
        for(Product product: ProductsQueries.getAllProductsNonDeleted()) {
            if((product.getQuantity() < product.getMinQuantity())) {
                lowStockStr += "   - " + product.getProdName() + "<br>";
            }
        }
        if(!lowStockStr.equals("")) {
            JCustomOptionPane.messageDialog(
                    "<html>" +
                            "<b>¡Advertencia! Stock bajo de los siguientes artículos:</b><br>" +
                            lowStockStr +
                            "</html>"
                    , "Advertencia de stock", JOptionPane.WARNING_MESSAGE);
        }
    }

    private MainScreen view;
}
