package com.franciscodadone.controller;

import com.franciscodadone.model.local.queries.CSVQueries;
import com.franciscodadone.model.local.queries.StockQueries;
import com.franciscodadone.models.Product;
import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.JCustomOptionPane;
import com.franciscodadone.util.Util;
import com.franciscodadone.view.AddModifyStock;
import com.franciscodadone.view.MainScreen;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;

public class AddModifyStockController {

    public AddModifyStockController(AddModifyStock view) {
        this.view = view;

        handleKeyboard();
        addProductButton();
        backButton();
        stockList();
        stockSelection();
        editButtons();

    }

    private void updateList() {
        int a = view.stockList.getSelectedIndex();
        defaultListModel.clear();
        stockList();
        view.stockList.setSelectedIndex(a);
    }

    private void editButtons() {
        view.modifyNameButton.addActionListener(e -> {
            String res = JOptionPane.showInputDialog("Modificar nombre", view.stockList.getSelectedValue());
            if(res != null) {
                Product product = (Product)view.stockList.getSelectedValue();
                product.setProdName(res);
                StockQueries.modifyProductByCode(product.getCode(), product);
                JCustomOptionPane.messageDialog("Nombre modificado con éxito!", "Info", JOptionPane.PLAIN_MESSAGE);
                updateList();
            }
        });

        view.modifyPriceButton.addActionListener(e -> {
            String res = JOptionPane.showInputDialog("Modificar precio", ((Product)view.stockList.getSelectedValue()).getPrice());
            if(res != null) {
                if(Util.isNumeric(res)) {
                    Product product = (Product)view.stockList.getSelectedValue();
                    product.setPrice(Double.parseDouble(res));
                    StockQueries.modifyProductByCode(product.getCode(), product);
                    JCustomOptionPane.messageDialog("Precio modificado con éxito!", "Info", JOptionPane.PLAIN_MESSAGE);
                    updateList();
                } else {
                    JCustomOptionPane.messageDialog("El precio tiene que ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        view.modifyQuantityButton.addActionListener(e -> {
            String res = JOptionPane.showInputDialog("Modificar cantidad", ((Product)view.stockList.getSelectedValue()).getQuantity());
            if(res != null) {
                if(Util.isNumeric(res)) {
                    Product product = (Product)view.stockList.getSelectedValue();
                    product.setQuantity(Integer.parseInt(res));
                    StockQueries.modifyProductByCode(product.getCode(), product);
                    JCustomOptionPane.messageDialog("Cantidad modificada con éxito!", "Info", JOptionPane.PLAIN_MESSAGE);
                    updateList();
                } else {
                    JCustomOptionPane.messageDialog("La cantidad tiene que ser numérica.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void stockSelection() {
        view.stockList.addListSelectionListener(e -> {
            try {
                view.productSearch.setText("Producto: " + ((Product)view.stockList.getSelectedValue()).getProdName());
                view.searchPrice.setText("Precio: $" + ((Product)view.stockList.getSelectedValue()).getPrice());
                view.searchQuantity.setText("Cantidad: " + ((Product)view.stockList.getSelectedValue()).getQuantity());
                view.searchQR.setText("QR: " + ((Product)view.stockList.getSelectedValue()).getCode());
                view.modifyQuantityButton.setEnabled(true);
                view.modifyPriceButton.setEnabled(true);
                view.modifyNameButton.setEnabled(true);
            } catch (Exception e1) {
                view.productSearch.setText("Producto: -");
                view.searchPrice.setText("Precio: $0000");
                view.searchQuantity.setText("Cantidad: 0000");
                view.searchQR.setText("QR: 00000000000");
                view.modifyQuantityButton.setEnabled(false);
                view.modifyPriceButton.setEnabled(false);
                view.modifyNameButton.setEnabled(false);
            }

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
            if(product.getProdName().toLowerCase().contains(searchTerm.toLowerCase()) || product.getCode().equals(searchTerm)) {
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
                    if(StockQueries.getProductByCode(product.getCode()) == null) {
                        StockQueries.saveProduct(product);
                        JCustomOptionPane.messageDialog("Producto guardado correctamente!", "", JOptionPane.PLAIN_MESSAGE);
                    }
                    GUIHandler.changeScreen(new AddModifyStock().panel);
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
                        if(StockQueries.getProductByCode(view.codeField.getText()) == null) {
                            view.descriptionField.setText(CSVQueries.search(view.codeField.getText()));
                            view.searchStock.setText("");
                        } else {
                            view.searchStock.setText(view.codeField.getText());
                            searchFilter(view.codeField.getText());
                            view.stockList.setSelectedIndex(0);
                            view.codeField.setText("");
                        }
                    }
                    if(view.searchStock.hasFocus()) searchFilter(view.searchStock.getText());
                    return false;
                });
    }



    private AddModifyStock view;

}
