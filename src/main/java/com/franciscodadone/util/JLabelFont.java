package com.franciscodadone.util;

import javax.swing.*;
import java.awt.*;

public class JLabelFont extends JLabel {
    /**
     * Label with a font.
     * @param txt
     * @param font
     */
    public JLabelFont(String txt, Font font) {
        super(txt);
        this.setFont(font);
    }
}
