package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList();
        java.sql.Connection connection = connect();
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Stock;");
            while(res.next()) {
                products.add(new Product(
                        res.getString("code"),
                        res.getString("title"),
                        res.getDouble("price"),
                        res.getInt("quantity"),
                        res.getString("quantityType")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return products;
    }

}
