package com.franciscodadone.util;

import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.models.Session;

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
                "Si"
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

    public static Object[] startSessionDialog() {
        Font font = new Font("Arial", Font.PLAIN, 30);

        JTextField sellerNameField = new JTextField(8);
        JTextField money = new JTextField(8);
        sellerNameField.setFont(font);
        money.setFont(font);

        JLabel moneyLabel = new JLabel("Dinero en la caja:");
        JLabel sellerLabel = new JLabel("Vendedor:");
        moneyLabel.setFont(font);
        sellerLabel.setFont(font);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(sellerLabel);
        mainPanel.add(sellerNameField);
        mainPanel.add(moneyLabel);
        mainPanel.add(money);

        int result = JOptionPane.showConfirmDialog(null, mainPanel,
                "Iniciar turno", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {

            if(sellerNameField.getText().equals("")) {
                JCustomOptionPane.messageDialog("El nombre del vendedor no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            if(!Util.isNumeric(money.getText()) || money.getText().equals("")) {
                JCustomOptionPane.messageDialog("El dinero en caja tiene que ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            Object[] values = new Object[2];
            values[0] = sellerNameField.getText();
            values[1] = money.getText();
            return values;
        }
        return null;
    }
}
