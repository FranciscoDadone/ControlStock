package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.ProductsQueries;
import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.local.queries.UtilQueries;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.models.Session;
import com.franciscodadone.util.*;
import com.franciscodadone.view.*;
import javax.swing.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainScreenController {

    public MainScreenController(MainScreen view) {
        this.view = view;

        PrinterService printerService = new PrinterService();
        printerService.getPrinters().forEach(printer -> {
            view.printersComboBox.addItem(printer);
        });

        String selectedPrinter = UtilQueries.getPrinterName();
        if (selectedPrinter != null) {
            for (int i = 0; i < view.printersComboBox.getItemCount(); i++) {
                if (view.printersComboBox.getItemAt(i).toString().equals(selectedPrinter)) {
                    view.printersComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        view.printersComboBox.addActionListener(e -> {
            UtilQueries.setPrinterName(view.printersComboBox.getItemAt(view.printersComboBox.getSelectedIndex()).toString());
        });

        view.startTurnButton.addActionListener(e -> {
            Session activeSession = SessionsQueries.getActiveSession();

            if(activeSession != null) {
                int res = JCustomOptionPane.confirmDialog("Hay un turno activo. ¿Desea seguir trabajando en él? O iniciar uno nuevo", "Turno activo");
                if(res == JOptionPane.YES_OPTION) {
                    TurnView turn = new TurnView(activeSession);
                    GUIHandler.changeScreen(turn.panel);
                    turn.focusField();
                } else if(res == JOptionPane.NO_OPTION) {
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
