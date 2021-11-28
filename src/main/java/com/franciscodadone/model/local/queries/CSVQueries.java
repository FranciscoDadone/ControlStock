package com.franciscodadone.model.local.queries;

import java.io.*;

public class CSVQueries {

    public String search(String code) {
        String line;
        String splitBy = ",";
        try {
            BufferedReader br;
            try {
                InputStream in = getClass().getResourceAsStream("/asw.producto.csv");
                br = new BufferedReader(new InputStreamReader(in));
            } catch (Exception e1) {
                br = new BufferedReader(new FileReader("src/main/resources/asw.producto.csv"));
            }

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
