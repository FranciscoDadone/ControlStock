package com.franciscodadone.util;

import com.franciscodadone.view.MainScreen;
import javax.swing.*;

public class GUIHandler {

    private static MainScreen main;

    public static void main() {
        main = new MainScreen();
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
