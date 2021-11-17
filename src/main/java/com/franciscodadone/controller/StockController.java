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

public class StockController {

    public StockController(AddStock view) {
        this.view = view;
        handleKeyboard();


        view.addProductButton.addActionListener(e -> {

            if(CSVQueries.search(view.codeField.getText()) == null) {
                JCustomOptionPane.messageDialog("Código incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(view.priceField.getText().length() == 0 || !Util.isNumeric(view.priceField.getText()) ) {
                JCustomOptionPane.messageDialog("El precio no puede quedar vacío o tener letras.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(view.descriptionField.getText().length() == 0) {
                JCustomOptionPane.messageDialog("La descripción no puede quedar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(view.quantityField.getText().length() == 0 || !Util.isNumeric(view.quantityField.getText())) {
                JCustomOptionPane.messageDialog("La cantidad no puede quedar vacía o tener letras.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Product product = new Product(
                        view.codeField.getText(),
                        view.descriptionField.getText(),
                        Double.parseDouble(view.priceField.getText()),
                        Integer.parseInt(view.quantityField.getText()),
                        (view.unitRadioButton.isSelected()) ? "U" : "G");
                int res = JCustomOptionPane.confirmDialog(product);

                if(res == JOptionPane.YES_OPTION) {

                    StockQueries.saveProduct(product);

                }
            }

        });


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
                    return false;
                });
    }



    private AddStock view;

}
