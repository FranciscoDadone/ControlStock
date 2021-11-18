package com.franciscodadone.view;

import com.franciscodadone.controller.AddModifyStockController;
import org.jdesktop.swingx.prompt.PromptSupport;


import javax.swing.*;
import java.awt.*;

public class AddModifyStock {

    public JTextField codeField;
    public JTextField quantityField;
    public JButton addProductButton;
    public JPanel panel;
    public JTextField descriptionField;
    public JTextField priceField;
    public JButton backButton;
    public JList stockList;
    public JTextField searchStock;
    public JRadioButton unitRadioButton;
    public JRadioButton gramsRadioButton;
    public JLabel searchQuantity;
    public JLabel searchPrice;
    public JLabel searchQR;
    public JLabel productSearch;
    public JButton modifyQuantityButton;
    public JButton modifyPriceButton;
    public JButton modifyNameButton;

    public AddModifyStock() {
        new AddModifyStockController(this);

        PromptSupport.setPrompt("Buscar por nombre o código...", searchStock);
        PromptSupport.setPrompt("Código QR o dejar en blanco para generar un código.", codeField);
        searchStock.setFont(new Font("Arial", Font.PLAIN, 24));
        codeField.setFont(new Font("Arial", Font.PLAIN, 24));

    }

    public void focusField() {
        codeField.setText("");
        codeField.requestFocusInWindow();
    }

}
