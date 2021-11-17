package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.models.Product;

import java.sql.SQLException;

public class StockQueries extends SQLiteConnection {

    /**
     * Adds a new sell to the database.
     * @param product
     */
    public static void saveProduct(Product product) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "INSERT INTO Stock (code, title, quantity, price, quantityType) VALUES (" +
                            "'" + product.getCode()         + "'," +
                            "'" + product.getProdName()     + "'," +
                                  product.getQuantity()     + "," +
                                  product.getPrice()        + "," +
                            "'" + product.getQuantityType() + "');"
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
}
