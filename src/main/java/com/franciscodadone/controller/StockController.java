package com.franciscodadone.controller;

import com.franciscodadone.view.AddStock;

import java.awt.*;

public class StockController {

    public StockController(AddStock view) {
        this.view = view;


        // Registers F7 in keyboard to focus the cursor to the barcode field.
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if(e.getKeyCode() == 118) view.focusField();
                    return false;
                });
    }



    private AddStock view;

}
