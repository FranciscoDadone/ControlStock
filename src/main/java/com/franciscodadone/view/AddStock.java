package com.franciscodadone.view;

import com.franciscodadone.controller.StockController;
import org.jdesktop.swingx.prompt.PromptSupport;


import javax.swing.*;
import java.awt.*;

public class AddStock {

    private JTextField codeField;
    private JTextField quantityField;
    private JButton agregarProductoButton;
    public JPanel panel;
    private JTextField descriptionField;
    private JTextField priceField;
    private JButton atrásButton;
    private JList stockList;
    private JTextField searchStock;

    public AddStock() {
        new StockController(this);


        PromptSupport.setPrompt("Buscar en el stock...", searchStock);
        searchStock.setFont(new Font("Arial", Font.PLAIN, 24));

    }

    public void focusField() {
        codeField.setText("");
        codeField.requestFocusInWindow();
    }

}
