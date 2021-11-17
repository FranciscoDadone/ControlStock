package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.CSVQueries;
import com.franciscodadone.model.local.queries.StockQueries;
import com.franciscodadone.models.Product;
import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.JCustomOptionPane;
import com.franciscodadone.util.Util;
import com.franciscodadone.view.AddStock;
import com.franciscodadone.view.MainScreen;

import javax.swing.*;
import java.awt.*;

public class AddStockController {

    public AddStockController(AddStock view) {
        this.view = view;
        handleKeyboard();

        addProductButton();
        backButton();
        stockList();
        stockSelection();

    }


    private void stockSelection() {
        view.stockList.addListSelectionListener(e -> {
            view.searchPrice.setText("Precio: $" + ((Product)view.stockList.getSelectedValue()).getPrice());
            view.searchQuantity.setText("Cantidad: " + ((Product)view.stockList.getSelectedValue()).getQuantity());
        });
    }

    DefaultListModel defaultListModel = new DefaultListModel();
    private void stockList() {
        StockQueries.getAllProducts().forEach((product) -> {
            defaultListModel.addElement(product);
        });
        view.stockList.setModel(defaultListModel);
        view.stockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Filters the names from the database to the searchTerm and if
     * there are coincidences it saves it in the JList to show them.
     * @param searchTerm
     */
    private void searchFilter(String searchTerm) {
        DefaultListModel filteredItems = new DefaultListModel();
        StockQueries.getAllProducts().forEach((product) -> {
            String name = product.getProdName().toLowerCase();
            if(name.contains(searchTerm.toLowerCase())) {
                filteredItems.addElement(product);
            }
        });
        defaultListModel = filteredItems;
        view.stockList.setModel(defaultListModel);
    }

    private void addProductButton() {
        view.addProductButton.addActionListener(e -> {
            if(view.codeField.getText().length() <= 6) {
                JCustomOptionPane.messageDialog("<html>Código de barras incorrecto.<br>(Si el producto no posee código QR, dejar el campo en blanco para que el sistema genere uno por usted.)</html>", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(view.priceField.getText().length() == 0 || !Util.isNumeric(view.priceField.getText()) ) {
                JCustomOptionPane.messageDialog("El precio no puede quedar vacío o tener letras.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(view.descriptionField.getText().length() == 0) {
                JCustomOptionPane.messageDialog("La descripción no puede quedar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(view.quantityField.getText().length() == 0 || !Util.isNumeric(view.quantityField.getText())) {
                JCustomOptionPane.messageDialog("La cantidad no puede quedar vacía o tener letras.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int resCode = -1;
                if(CSVQueries.search(view.codeField.getText()) == null) {
                    resCode = JCustomOptionPane.confirmDialog("<html>El código QR puede que se haya escaneado mal. ¿Continuar?<br>(Si el producto no posee código QR, dejar el campo en blanco para que el sistema genere uno por usted.)</html>", "Advertencia");
                }

                Product product = new Product(
                        view.codeField.getText(),
                        view.descriptionField.getText(),
                        Double.parseDouble(view.priceField.getText()),
                        Integer.parseInt(view.quantityField.getText()),
                        (view.unitRadioButton.isSelected()) ? "U" : "G");
                int res = JCustomOptionPane.confirmDialog(product);

                if(res == JOptionPane.YES_OPTION && (resCode == -1 || resCode == JOptionPane.YES_OPTION)) {
                    StockQueries.saveProduct(product);
                    defaultListModel.clear();
                    stockList();
                }
            }
        });
    }

    private void backButton() {
        view.backButton.addActionListener(e -> {
            GUIHandler.changeScreen(new MainScreen(false).getContentPanel());
        });
    }

    private void handleKeyboard() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if(e.getKeyCode() == 118) view.focusField();
                    if(view.codeField.hasFocus() && view.codeField.getText().length() > 5) {
                        view.descriptionField.setText(CSVQueries.search(view.codeField.getText()));
                    }
                    if(view.searchStock.hasFocus()) searchFilter(view.searchStock.getText());
                    return false;
                });
    }



    private AddStock view;

}
