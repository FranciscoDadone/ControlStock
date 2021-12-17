package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.SellQueries;
import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.local.queries.ProductsQueries;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.models.Sell;
import com.franciscodadone.model.models.Session;
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
        this.inAnotherScreen = false;

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
                    p1.setProdName(p.getProdName() + "     (x" + p1.getQuantity() + ")               $" + p.getPrice() * p1.getQuantity());
                    cartListModel.set(index, p1);
                } else {
                    p.setQuantity(quantity);
                    p.setProdName(p.getProdName() + "     (x" + p.getQuantity() + ")               $" + p.getPrice() * p.getQuantity());
                    cartListModel.addElement(p);
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
                inAnotherScreen = false;
            }
        });

        view.backButton.addActionListener(e -> {
            inAnotherScreen = true;
            GUIHandler.changeScreen(new MainScreen(false).getContentPanel());
        });

        view.endTurnButton.addActionListener(e -> {
            inAnotherScreen = true;
            double sessionEndMoney = JCustomOptionPane.endSessionDialog();
            if(sessionEndMoney != -1) {
                SessionsQueries.endCurrentSession(sessionEndMoney);
                GUIHandler.changeScreen(new MainScreen(false).getContentPanel());
            } else inAnotherScreen = false;
        });

        view.addSellButton.addActionListener(e -> {
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

        });

    }

    private void modifyQuantity(Product product, int newQuantity) {
        int index = isInList(cartListModel, product);
        product.setQuantity(newQuantity);
        product.setProdName(product.getUnmodifiedProdName() + "     (x" + newQuantity + ")               $" + product.getPrice() * newQuantity);
        cartListModel.set(index, product);
        updateTotal();
    }

    private void handleKeyboard() {
        // Registers F7 in keyboard to focus the cursor to the barcode field.
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if(e.getKeyCode() == 118) view.focusField();
                    if(view.codeField.hasFocus()) searchFilter(view.codeField.getText());
                    if(view.exchangeField.hasFocus()) updateExchange();

                    // Handle quantity
                    if(e.getKeyCode() >= 97 && e.getKeyCode() <= 105 && !view.codeField.hasFocus() && !view.exchangeField.hasFocus() && !view.quantityField.hasFocus() && !inAnotherScreen) {
                        modifyQuantity(((Product)view.cartList.getSelectedValue()), (e.getKeyCode() - 96));
                    }
                    return false;
                });
    }

    DefaultListModel stockListModel = new DefaultListModel();
    DefaultListModel cartListModel  = new DefaultListModel();
    private void stockList() {
        ProductsQueries.getAllProducts().forEach((product) -> {
            if(!product.isDeleted()) stockListModel.addElement(product);
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
        ProductsQueries.getAllProducts().forEach((product) -> {
            if((product.getProdName().toLowerCase().contains(searchTerm.toLowerCase())) && !product.isDeleted()) {
                filteredItems.addElement(product);
            }
            if(product.getCode().equals(searchTerm) && !product.isDeleted()) {
                int index = isInList(cartListModel, product);
                if(index != -1) {
                    Product p = (Product) cartListModel.get(index);
                    p.setQuantity(p.getQuantity());
                    p.setProdName(product.getProdName() + "     (x" + p.getQuantity() + ")               $" + p.getPrice() * p.getQuantity());
                    cartListModel.set(index, p);
                } else {
                    product.setQuantity(1);
                    product.setProdName(product.getProdName() + "     (x" + product.getQuantity() + ")               $" + product.getPrice() * product.getQuantity());
                    cartListModel.addElement(product);
                }

                /**
                 * Explanation of this code:
                 * When I tried view.codeField.setText(""); it made it blank but for some reason the
                 * list went all blank. So to overcome this, I made a "robot" to type backspace a bunch
                 * of times to clear the field and with that the list doesn't fail.
                 * This needs to be replaced with a more optimal solution.
                 */
                try {
                    Robot robot = new Robot();
                    for(int i = 0; i < product.getCode().length() + product.getProdName().length(); i++) {
                        robot.keyPress(KeyEvent.VK_BACK_SPACE);
                        robot.keyRelease(KeyEvent.VK_BACK_SPACE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Select the new product in the cart list
                    new Thread(() -> {
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        view.cartList.setSelectedIndex(isInList(cartListModel, product));
                        view.codeField.transferFocus();
                    }).start();
                }
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
    private boolean inAnotherScreen;

}
