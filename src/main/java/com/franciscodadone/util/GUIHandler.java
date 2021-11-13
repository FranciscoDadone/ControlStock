package com.franciscodadone.util;

import com.franciscodadone.view.mainScreen;
import javax.swing.*;

public class GUIHandler {

    private static mainScreen main;

    public static void main() {
        main = new mainScreen();
    }

    /**
     * Handles the screen change.
     * @param newScreen
     */
    public static void changeScreen(JPanel newScreen) {
        main.getContentPanel().removeAll();
        main.getContentPanel().add(newScreen);
        main.getContentPanel().revalidate();
        main.getContentPanel().repaint();
//        main.getContentPanel().setBackground(new Color(251, 235, 222));
    }

}
