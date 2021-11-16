package com.franciscodadone.view;

import com.franciscodadone.controller.StockController;
import org.jdesktop.swingx.prompt.PromptSupport;


import javax.swing.*;
import java.awt.*;

public class AddStock {

    public JTextField codeField;
    public JTextField quantityField;
    public JButton agregarProductoButton;
    public JPanel panel;
    public JTextField descriptionField;
    public JTextField priceField;
    public JButton atrásButton;
    public JList stockList;
    public JTextField searchStock;
    private JRadioButton numericRadioButton;
    private JRadioButton gramsRadioButton;

    public AddStock() {
        new StockController(this);

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
