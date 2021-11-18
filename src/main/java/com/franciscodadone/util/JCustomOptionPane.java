package com.franciscodadone.util;

import com.franciscodadone.models.Product;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class JCustomOptionPane {

    /**
     * Custom warning dialog with bigger font.
     * @param txt
     * @param title
     * @param warningType
     */
    public static void messageDialog(String txt, String title, final int warningType) {
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL",Font.PLAIN,20)));
        JOptionPane.showMessageDialog(
                null,
                new JLabelFont(txt, new Font("Arial", Font.BOLD, 24)),
                title,
                warningType
        );
    }

    public static int inputDialog(String placeholderText, String title) {
        JTextField field = new JTextField(placeholderText, 30);
        field.setFont(new Font("Arial", Font.BOLD, 24));
        JOptionPane pane = new JOptionPane(field,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null);
        pane.createDialog(title).setVisible(true);
        return 1;
    }

    /**
     * Custom message dialog with bigger font and custom buttons.
     * @param txt
     * @param title
     * @return
     */
    public static int confirmDialog(String txt, String title) {
        Object[] options = { "Si", "No", "Cancelar" };
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL",Font.PLAIN,20)));
        int result = JOptionPane.showOptionDialog(
                null,
                new JLabelFont(txt, new Font("Arial", Font.PLAIN, 24)),
                title,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                null
        );
        return result;
    }

    /**
     * Confirm dialog for the new product.
     * @param product
     * @return
     */
    public static int confirmDialog(Product product) {
        String txt =
                "<html><b>¿Son correctos estos datos del nuevo producto?</b><br><br>" +
                        "<b>Código</b>: "      + product.getCode()      + "<br>" +
                        "<b>Descripción</b>: " + product.getProdName()  + "<br>" +
                        "<b>Precio</b>: "      + product.getPrice()     + "<br>" +
                        "<b>Cantidad</b>: " + product.getQuantity() + "</html>";

        Object[] options = { "Si", "No", "Cancelar" };
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL",Font.PLAIN,20)));

        int result = JOptionPane.showOptionDialog(
                null,
                new JLabelFont(txt, new Font("Arial", Font.PLAIN, 24)),
                "Confirmar datos",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                null
        );
        return result;
    }

}
