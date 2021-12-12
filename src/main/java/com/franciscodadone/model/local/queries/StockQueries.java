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
                    "INSERT INTO Stock (code, title, quantity, price, quantityType, deleted) VALUES (" +
                            "'" + product.getCode()         + "'," +
                            "'" + product.getProdName()     + "'," +
                                  product.getQuantity()     + "," +
                                  product.getPrice()        + "," +
                            "'" + product.getQuantityType() + "', false);"
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
                if(!res.getBoolean("deleted")) {
                    products.add(new Product(
                            res.getString("code"),
                            res.getString("title"),
                            res.getDouble("price"),
                            res.getInt("quantity"),
                            res.getString("quantityType"),
                            res.getBoolean("deleted")
                    ));
                }
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

    public static Product getProductByCode(String code) {
        Product p = null;
        java.sql.Connection connection = connect();
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Stock WHERE (code='" + code + "');");
            while(res.next()) {
                p = new Product(
                        res.getString("code"),
                        res.getString("title"),
                        res.getDouble("price"),
                        res.getInt("quantity"),
                        res.getString("quantityType"),
                        res.getBoolean("deleted")
                );
            }
        } catch (SQLException e) {}
          finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return p;
        }
    }

    public static void modifyProductByCode(String code, Product product) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "UPDATE Stock SET title='" + product.getProdName() + "' WHERE code='" + code + "';"
            );
            connection.createStatement().execute(
                    "UPDATE Stock SET price=" + product.getPrice() + " WHERE code='" + code + "';"
            );
            connection.createStatement().execute(
                    "UPDATE Stock SET quantity=" + product.getQuantity() + " WHERE code='" + code + "';"
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

    public static void deleteProduct(Product product) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "UPDATE Stock SET deleted=true WHERE code='" + product.getCode() + "';"
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

    public static ArrayList<Product> getProducts(String products) {
        ArrayList<Product> products1 = new ArrayList<>();

        String[] prod = products.split(";");
        for(int i = 0; i < prod.length; i++) {
            String[] prodWithQuantity = prod[i].split(":");
            Product product = getProductByCode(prodWithQuantity[0]);
            product.setQuantity(Integer.parseInt(prodWithQuantity[1]));

            products1.add(product);
        }
        return products1;
    }
}
