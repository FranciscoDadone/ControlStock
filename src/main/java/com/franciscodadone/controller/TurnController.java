package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.SellQueries;
import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.local.queries.ProductsQueries;
import com.franciscodadone.models.Product;
import com.franciscodadone.models.Sell;
import com.franciscodadone.models.Session;
import com.franciscodadone.util.FDate;
import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.JCustomOptionPane;
import com.franciscodadone.util.Util;
import com.franciscodadone.view.MainScreen;
import com.franciscodadone.view.TurnView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TurnController {

    public TurnController(TurnView view, Session session) {
        this.view       = view;
        this.session    = session;

        handleKeyboard();
        stockList();
        cartSelection();
        stockSelection();
        handleButtons();

        view.cartList.setModel(defaultListModel1);
        view.cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        view.sellerNameField.setText("Vendedor: " + this.session.getSeller());
        view.sessionStartLabel.setText("Inicio del turno: " + session.getDateStarted());
        view.dateNowLabel.setText("Fecha actual: " + new FDate());

    }

    private void handleButtons() {
        view.addProductButton.addActionListener(e -> {
            if(!Util.isNumeric(view.quantityField.getText())) {
                JCustomOptionPane.messageDialog("La cantidad tiene que ser numérica.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Product p = (Product)view.productList.getSelectedValue();
                int quantity = Integer.parseInt(view.quantityField.getText());
                int index = isInList(defaultListModel1, p);
                if(index != -1) {
                    Product p1 = (Product) defaultListModel1.get(index);
                    p1.setQuantity(p1.getQuantity() + quantity);
                    p1.setProdName(p.getProdName() + "     (x" + p1.getQuantity() + ")               $" + p.getPrice() * p1.getQuantity());
                    defaultListModel1.set(index, p1);
                } else {
                    p.setQuantity(quantity);
                    p.setProdName(p.getProdName() + "     (x" + p.getQuantity() + ")               $" + p.getPrice() * p.getQuantity());
                    defaultListModel1.addElement(p);
                }
            }
            view.codeField.setText("");
            view.quantityField.setText("1");
            view.description.setText("Descripción: ");
            view.price.setText("Precio: $0000");
            view.addProductButton.setEnabled(false);
            defaultListModel.clear();
            stockList();
            updateTotal();
        });

        view.deleteProductButton.addActionListener(e -> {
            defaultListModel1.removeElement(view.cartList.getSelectedValue());
            view.deleteProductButton.setEnabled(false);
            view.modifyQuantityButton.setEnabled(false);
            updateTotal();
        });

        view.modifyQuantityButton.addActionListener(e -> {
            Product p = (Product) view.cartList.getSelectedValue();
            int index = isInList(defaultListModel1, p);

            String newQuantity = JOptionPane.showInputDialog("Ingrese la nueva cantidad", p.getQuantity());
            if(!Util.isNumeric(newQuantity)) JCustomOptionPane.messageDialog("La cantidad tiene que ser numérica", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                p.setQuantity(Integer.parseInt(newQuantity));
                p.setProdName(p.getUnmodifiedProdName() + "     (x" + newQuantity + ")               $" + p.getPrice() * Integer.parseInt(newQuantity));
                defaultListModel1.set(index, p);
            }
            updateTotal();
        });

        view.backButton.addActionListener(e -> {
            GUIHandler.changeScreen(new MainScreen(false).getContentPanel());
        });

        view.endTurnButton.addActionListener(e -> {
            double sessionEndMoney = JCustomOptionPane.endSessionDialog();
            if(sessionEndMoney != -1) {
                SessionsQueries.endCurrentSession(sessionEndMoney);
                GUIHandler.changeScreen(new MainScreen(false).getContentPanel());
            }
        });

        view.addSellButton.addActionListener(e -> {
            ArrayList<Product> products = new ArrayList<>();
            for(int i = 0; i < defaultListModel1.getSize(); i++) {
                Product product = (Product) defaultListModel1.get(i);
                product.setProdName(product.getUnmodifiedProdName());
                products.add(product);
            }
            Sell sell = new Sell(products, getTotal(), session.getId(), new FDate());
            SellQueries.saveSell(sell);

            view.cartList.removeAll();
            defaultListModel1.removeAllElements();
            view.addSellButton.setEnabled(false);
            view.exchangeLabel.setText("$0");
            view.totalLabel.setText("$0");
            view.exchangeField.setText("");
            GUIHandler.changeScreen(view.panel);

        });

    }

    private void handleKeyboard() {
        // Registers F7 in keyboard to focus the cursor to the barcode field.
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    view.dateNowLabel.setText("Fecha actual: " + new FDate()); // update date
                    if(e.getKeyCode() == 118) view.focusField();
                    if(view.codeField.hasFocus()) {
                        searchFilter(view.codeField.getText());
                    }
                    if(view.exchangeField.hasFocus()) updateExchange();
                    return false;
                });
    }

    DefaultListModel defaultListModel = new DefaultListModel();
    DefaultListModel defaultListModel1 = new DefaultListModel();
    private void stockList() {
        ProductsQueries.getAllProducts().forEach((product) -> {
            defaultListModel.addElement(product);
        });
        view.productList.setModel(defaultListModel);
        view.productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private int isInList(DefaultListModel list, Product product) {
        for(int i = 0; i < list.getSize(); i++) {
            if(((Product)list.get(i)).getCode().equals(product.getCode())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Filters the names from the database to the searchTerm and if
     * there are coincidences it saves it in the JList to show them.
     * @param searchTerm
     */
    private void searchFilter(String searchTerm) {
        DefaultListModel filteredItems = new DefaultListModel();
        ProductsQueries.getAllProducts().forEach((product) -> {
            if(product.getProdName().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredItems.addElement(product);
            }
            if(product.getCode().equals(searchTerm)) {
                int index = isInList(defaultListModel1, product);
                if(index != -1) {
                    Product p = (Product) defaultListModel1.get(index);
                    p.setQuantity(p.getQuantity() + 1);
                    p.setProdName(product.getProdName() + "     (x" + p.getQuantity() + ")               $" + p.getPrice() * p.getQuantity());
                    defaultListModel1.set(index, p);
                } else {
                    product.setQuantity(1);
                    product.setProdName(product.getProdName() + "     (x" + product.getQuantity() + ")               $" + product.getPrice() * product.getQuantity());
                    defaultListModel1.addElement(product);
                }
                view.codeField.setText("");
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                    robot.keyRelease(KeyEvent.VK_ESCAPE);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });
        defaultListModel = filteredItems;
        view.productList.setModel(defaultListModel);
    }

    private void cartSelection() {
        view.cartList.addListSelectionListener(e -> {
            try {
                view.deleteProductButton.setEnabled(true);
                view.modifyQuantityButton.setEnabled(true);
            } catch (Exception e1) {
                view.deleteProductButton.setEnabled(false);
                view.modifyQuantityButton.setEnabled(false);
            }
        });
    }

    private void stockSelection() {
        view.productList.addListSelectionListener(e -> {
            try {
                view.addProductButton.setEnabled(true);
                view.price.setText("Precio: $" + ((Product)view.productList.getSelectedValue()).getPrice());
                view.description.setText("Descripción: " + ((Product)view.productList.getSelectedValue()).getProdName());
            } catch (Exception e1) {
                view.addProductButton.setEnabled(false);
            }
        });
    }

    private double getTotal() {
        double total = 0;
        for(int i = 0; i < defaultListModel1.getSize(); i++) {
            Product product = ((Product)defaultListModel1.get(i));
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }

    private void updateTotal() {
        double total = getTotal();
        view.totalLabel.setText("$" + total);
        if(total != 0) view.addSellButton.setEnabled(true);
        else view.addSellButton.setEnabled(false);
    }

    public void updateExchange() {
        if(Util.isNumeric(view.exchangeField.getText())) {
            double exchange = Double.parseDouble(view.exchangeField.getText());
            double total = getTotal();
            if(exchange > total) view.exchangeLabel.setText("$" + (exchange - total));
            else view.exchangeLabel.setText("$0");
        }
    }

    private TurnView view;
    private Session session;

}
