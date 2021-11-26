package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.StockQueries;
import com.franciscodadone.models.Product;
import com.franciscodadone.util.JCustomOptionPane;
import com.franciscodadone.util.Util;
import com.franciscodadone.view.TurnView;
import javax.swing.*;
import java.awt.*;

public class TurnController {

    public TurnController(TurnView view) {
        this.view = view;

        handleKeyboard();
        stockList();
        cartSelection();
        stockSelection();
        handleButtons();

        view.cartList.setModel(defaultListModel1);
        view.cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
                    p1.setProdName(p.getProdName() + "     (x" + p1.getQuantity() + ")");
                    defaultListModel1.set(index, p1);
                } else {
                    p.setQuantity(quantity);
                    p.setProdName(p.getProdName() + "     (x" + p.getQuantity() + ")");
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
        });

        view.deleteProductButton.addActionListener(e -> {
            defaultListModel1.removeElement(view.cartList.getSelectedValue());
            view.deleteProductButton.setEnabled(false);
            view.modifyQuantityButton.setEnabled(false);
        });

        view.modifyQuantityButton.addActionListener(e -> {
            Product p = (Product) view.cartList.getSelectedValue();
            int index = isInList(defaultListModel1, p);

            String newQuantity = JOptionPane.showInputDialog("Ingrese la nueva cantidad", p.getQuantity());
            if(!Util.isNumeric(newQuantity)) JCustomOptionPane.messageDialog("La cantidad tiene que ser numérica", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                p.setQuantity(Integer.parseInt(newQuantity));
                p.setProdName(p.getUnmodifiedProdName() + "     (x" + newQuantity + ")");
                defaultListModel1.set(index, p);
            }
        });

    }

    private void handleKeyboard() {
        // Registers F7 in keyboard to focus the cursor to the barcode field.
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if(e.getKeyCode() == 118) view.focusField();
                    if(view.codeField.hasFocus()) {
                        searchFilter(view.codeField.getText());
                    }
                    return false;
                });
    }

    DefaultListModel defaultListModel = new DefaultListModel();
    DefaultListModel defaultListModel1 = new DefaultListModel();
    private void stockList() {
        StockQueries.getAllProducts().forEach((product) -> {
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
        StockQueries.getAllProducts().forEach((product) -> {
            if(product.getProdName().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredItems.addElement(product);
            }
            if(product.getCode().equals(searchTerm)) {
                int index = isInList(defaultListModel1, product);
                if(index != -1) {
                    Product p = (Product) defaultListModel1.get(index);
                    p.setQuantity(p.getQuantity() + 1);
                    p.setProdName(product.getProdName() + "     (x" + p.getQuantity() + ")");
                    defaultListModel1.set(index, p);
                } else {
                    product.setQuantity(1);
                    product.setProdName(product.getProdName() + "     (x" + product.getQuantity() + ")");
                    defaultListModel1.addElement(product);
                }

                view.codeField.setText("");
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

    private TurnView view;

}
