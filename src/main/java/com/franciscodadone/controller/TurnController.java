package com.franciscodadone.controller;

import com.franciscodadone.view.TurnView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TurnController {

    public TurnController(TurnView view) {

        DefaultListModel defaultListModel = new DefaultListModel();

        view.productList.setModel(defaultListModel);
        view.productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca caaola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca caaola");
        defaultListModel.addElement("coca colaaaa");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca caaola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca caaola");
        defaultListModel.addElement("coca colaaaa");


        // Registers F7 in keyboard to focus the cursor to the barcode field.
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if(e.getKeyCode() == 118) view.focusField();
                    return false;
                });


    }

}
