package com.franciscodadone.model.local.queries;

import java.io.*;

public class CSVQueries {

    public String search(String code) {
        String line;
        String splitBy = ",";
        try {
            FileReader fr = null;
            try {
                fr = new FileReader("asw.producto.csv");
            } catch (FileNotFoundException e1) {
                if(fr == null) fr = new FileReader("src/main/resources/asw.producto.csv");
            }
            BufferedReader br = new BufferedReader(fr);
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
