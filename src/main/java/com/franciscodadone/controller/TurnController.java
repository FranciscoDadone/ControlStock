package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.SellQueries;
import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.local.queries.ProductsQueries;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.models.Sell;
import com.franciscodadone.model.models.Session;
import com.franciscodadone.util.*;
import com.franciscodadone.view.MainScreen;
import com.franciscodadone.view.TurnView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TurnController {

    public TurnController(TurnView view, Session session) {
        this.view       = view;
        this.session    = session;
        this.inAnotherScreen = false;

        this.products = ProductsQueries.getAllProductsNonDeleted();

        handleKeyboard();
        stockList();
        cartSelection();
        stockSelection();
        handleButtons();

        view.cartList.setModel(cartListModel);
        view.cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        view.sellerNameField.setText("Vendedor: " + this.session.getSeller());
        view.sessionStartLabel.setText("Inicio del turno: " + session.getDateStarted());

        new Thread(() -> {
            while(true) {
                view.dateNowLabel.setText("Fecha actual: " + new FDate());
                updateTotal();
                updateExchange();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleButtons() {
        view.addProductButton.addActionListener(e -> {
            if(!Util.isNumeric(view.quantityField.getText())) {
                JCustomOptionPane.messageDialog("La cantidad tiene que ser numérica.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Product p = (Product)view.productList.getSelectedValue();
                int quantity = Integer.parseInt(view.quantityField.getText());
                int index = isInList(cartListModel, p);
                if(index != -1) {
                    Product p1 = (Product) cartListModel.get(index);
                    p1.setQuantity(p1.getQuantity() + quantity);
                    cartListModel.set(index, p1);
                    modifyQuantity(p1, p1.getQuantity());
                } else {
                    p.setQuantity(quantity);
                    cartListModel.addElement(p);
                    modifyQuantity(p, p.getQuantity());
                }
            }
            view.codeField.setText("");
            view.quantityField.setText("1");
            view.description.setText("Descripción: ");
            view.price.setText("Precio: $0000");
            view.addProductButton.setEnabled(false);
            stockListModel.clear();
            stockList();
            updateTotal();
        });

        view.deleteProductButton.addActionListener(e -> {
            cartListModel.removeElement(view.cartList.getSelectedValue());
            view.deleteProductButton.setEnabled(false);
            view.modifyQuantityButton.setEnabled(false);
            updateTotal();
        });

        view.modifyQuantityButton.addActionListener(e -> {
            Product p = (Product) view.cartList.getSelectedValue();

            inAnotherScreen = true;

            String newQuantity = JOptionPane.showInputDialog("Ingrese la nueva cantidad", p.getQuantity());
            if(!Util.isNumeric(newQuantity)) JCustomOptionPane.messageDialog("La cantidad tiene que ser numérica", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                modifyQuantity(p, Integer.parseInt(newQuantity));
            }
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                inAnotherScreen = false;
            }).start();
        });

        view.backButton.addActionListener(e -> {
            inAnotherScreen = true;
            GUIHandler.changeScreen(new MainScreen(false).getContentPanel());
        });

        view.endTurnButton.addActionListener(e -> {
            inAnotherScreen = true;

            int res = JCustomOptionPane.confirmDialog("¿Seguro quiere terminar el turno?", "");
            if(res == JOptionPane.YES_OPTION) {
                double earnings = SessionsQueries.getMoneyFromActiveSession();
                Session activeSession = SessionsQueries.getActiveSession();
                JCustomOptionPane.messageDialog(
                        "<html>Turno de: " + activeSession.getSeller() + "<br>" +
                                "Ingresos totales en el turno: $" + earnings + "<br>" +
                                "La caja inició con: $" + activeSession.getStartMoney() + "<br>" +
                                "Inicio + Ingresos: $" + (activeSession.getStartMoney() + earnings) + "</html>", "", JOptionPane.INFORMATION_MESSAGE);
                SessionsQueries.endCurrentSession();
                GUIHandler.changeScreen(new MainScreen(false).getContentPanel());
            } else inAnotherScreen = false;
        });

        view.addSellButton.addActionListener(e -> {
            saveCurrentSell();
        });
    }

    private void saveCurrentSell() {
        ArrayList<Product> products = new ArrayList<>();
        for(int i = 0; i < cartListModel.getSize(); i++) {
            Product product = (Product) cartListModel.get(i);
            product.setProdName(product.getUnmodifiedProdName());
            products.add(product);
        }
        Sell sell = new Sell(products, getTotal(), session.getId(), new FDate());
        SellQueries.saveSell(sell, true);

        // Removing from stock
        products.forEach(product -> {
            Product p = ProductsQueries.getProductByCode(product.getCode());
            p.setQuantity(p.getQuantity() - product.getQuantity());
            ProductsQueries.modifyProductByCode(product.getCode(), p);
        });

        view.cartList.removeAll();
        cartListModel.removeAllElements();
        view.addSellButton.setEnabled(false);
        view.exchangeLabel.setText("$0");
        view.totalLabel.setText("$0");
        view.exchangeField.setText("");
        GUIHandler.changeScreen(view.panel);
    }

    private void modifyQuantity(Product product, int newQuantity) {
        int index = isInList(cartListModel, product);
        product.setQuantity(newQuantity);
        product.setProdName("(x" + newQuantity + ")($" + (product.getQuantity() * product.getPrice()) + ")   " + product.getUnmodifiedProdName());
        if(index != -1) {
            cartListModel.set(index, product);
        }
        updateTotal();
    }

    private void handleKeyboard() {
        // Registers F7 in keyboard to focus the cursor to the barcode field.
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if(e.getKeyCode() == 118) {
                        view.focusField();
                    }
                    if(view.codeField.hasFocus()) searchFilter(view.codeField.getText());

                    // Handle quantity
                    if(e.getKeyCode() >= 97 && e.getKeyCode() <= 105 && !view.codeField.hasFocus() && !view.exchangeField.hasFocus() && !view.quantityField.hasFocus() && !inAnotherScreen) {
                        modifyQuantity(((Product)view.cartList.getSelectedValue()), (e.getKeyCode() - 96));
                    }

                    // Handle enter
                    if(e.getKeyChar() == KeyEvent.VK_ENTER && !cartListModel.isEmpty() && view.productList.getSelectedValue() == null && !inAnotherScreen) {
                        inAnotherScreen = true;
                        if(JCustomOptionPane.confirmDialog("¿Guardar venta?", "Confirmar") == JOptionPane.YES_OPTION) {
                            saveCurrentSell();
                        }
                        inAnotherScreen = false;
                    }
                    return false;
                });
    }

    DefaultListModel stockListModel = new DefaultListModel();
    DefaultListModel cartListModel  = new DefaultListModel();
    private void stockList() {
        ProductsQueries.getAllProductsNonDeleted().forEach((product) -> {
            stockListModel.addElement(product);
        });
        view.productList.setModel(stockListModel);
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
        products.forEach((product) -> {
            if((product.getProdName().toLowerCase().contains(searchTerm.toLowerCase())) || product.getCode().contains(searchTerm) && !product.isDeleted()) {
                filteredItems.addElement(product);
            }
            if(product.getCode().equals(searchTerm) && !product.isDeleted()) {
                new Sound().playBeep();
                int index = isInList(cartListModel, product);
                if(index != -1) {
                    Product p = (Product) cartListModel.get(index);
                    p.setQuantity(p.getQuantity() + 1);
                    modifyQuantity(p, p.getQuantity());
                    cartListModel.set(index, p);
                } else {
                    product.setQuantity(1);
                    modifyQuantity(product, 1);
                    cartListModel.addElement(product);
                }
                this.products = ProductsQueries.getAllProductsNonDeleted();
                view.codeField.setText("");
                filteredItems.removeAllElements();
                for(Product product1 : products) filteredItems.addElement(product1);
                view.cartList.setSelectedIndex(isInList(cartListModel, product));
                view.codeField.transferFocus();
            }
        });


        stockListModel = filteredItems;
        view.productList.setModel(stockListModel);
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
        for(int i = 0; i < cartListModel.getSize(); i++) {
            Product product = ((Product) cartListModel.get(i));
            total += product.getPrice() * product.getQuantity();
        }
        return Double.parseDouble(new DecimalFormat("#.##").format(total));
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
    private boolean inAnotherScreen;
    private ArrayList<Product> products;

}
