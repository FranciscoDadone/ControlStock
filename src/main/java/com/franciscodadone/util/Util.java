package com.franciscodadone.util;

public class Util {

    /**
     * Check if a String has only numbers or not.
     * @param strNum
     * @return boolean
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) return false;
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
