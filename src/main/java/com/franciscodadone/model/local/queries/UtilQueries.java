package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.remote.queries.RemoteUtilQueries;
import com.franciscodadone.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilQueries extends SQLiteConnection {

    public static String getLastCustomCode() {
        String code = "C0";
        java.sql.Connection connection = connect();
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Util WHERE (id=1);");
            while(res.next()) {
                code = res.getString("customQR");
            }

            int highestLocalCustomCode = 0;
            for(Product product : ProductsQueries.getAllProducts()) {
                if(product.getCode().contains("C")) {
                    int pcode = Integer.parseInt(product.getCode().substring(1));
                    if(highestLocalCustomCode < pcode) highestLocalCustomCode = pcode;
                }
            }
            if(highestLocalCustomCode > Integer.parseInt(code.substring(1))) {
                UtilQueries.modifyLastCode("C" + highestLocalCustomCode, true);
                code = "C" + highestLocalCustomCode;
            }
        } catch (SQLException e) {}
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return code;
        }
    }

    public static void setPrinterName(String name) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "UPDATE Util SET printerName='" + name + "' WHERE id=1;"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPrinterName() {
        String name = "";
        java.sql.Connection connection = connect();
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Util WHERE (id=1);");
            while(res.next()) {
                name = res.getString("printerName");
            }
        } catch (SQLException e) {}
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return name;
        }
    }

    public static void modifyLastCode(String newCode, boolean saveRemote) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "UPDATE Util SET customQR='" + newCode + "' WHERE id=1;"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(saveRemote) RemoteUtilQueries.setLastCustomCode(newCode);
    }

}
