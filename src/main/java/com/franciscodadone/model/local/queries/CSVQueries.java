package com.franciscodadone.model.local.queries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVQueries {

    public static String search(String code) {
        String line = "";
        String splitBy = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/franciscodadone/Dev/ControlStock/src/main/resources/asw.producto.csv"));
            while ((line = br.readLine()) != null) {
                String[] lineAux = line.split(splitBy);
                if(lineAux.length > 1) {
                    if(lineAux[1].equals(code)) return lineAux[0];
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
