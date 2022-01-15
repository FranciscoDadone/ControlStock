package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.remote.queries.RemoteStockQueries;
import com.franciscodadone.util.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductsQueries extends SQLiteConnection {

    /**
     * Adds a new sell to the database.
     * @param product
     */
    public static void saveProduct(Product product, boolean saveRemote) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "INSERT INTO Stock (code, title, quantity, price, quantityType, deleted, minQuantity) VALUES (" +
                            "'" + product.getCode()         + "'," +
                            "'" + product.getProdName()     + "'," +
                                  product.getQuantity()     + "," +
                                  product.getPrice()        + "," +
                            "'" + product.getQuantityType() + "', " +
                                  product.isDeleted()       + ", " +
                                  product.getMinQuantity()  + ");"
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
        if(saveRemote) RemoteStockQueries.backupProduct(product);
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
                        res.getString("quantityType"),
                        res.getBoolean("deleted"),
                        res.getInt("minQuantity")
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

    public static ArrayList<Product> getAllProductsNonDeleted() {
        ArrayList<Product> products = new ArrayList<>();
        getAllProducts().forEach(product -> {
            if(!product.isDeleted()) products.add(product);
        });
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
                        res.getBoolean("deleted"),
                        res.getInt("minQuantity")
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
            connection.createStatement().execute(
                    "UPDATE Stock SET minQuantity=" + product.getMinQuantity() + " WHERE code='" + code + "';"
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
        RemoteStockQueries.editProduct(product);
    }

    public static void deleteProduct(Product product) {
        product.setDeleted(true);
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
        RemoteStockQueries.editProduct(product);
    }

    public static ArrayList<Product> getProducts(String products) {
        ArrayList<Product> products1 = new ArrayList<>();
        if(products.length() == 0) return products1;

        String[] prod = products.split(";");
        for(int i = 0; i < prod.length; i++) {
            String[] prodWithQuantity = prod[i].split(":");
            Product product;
            if(prodWithQuantity[0].contains("retiro")) {
                product = new Product(
                        prodWithQuantity[0],
                        "Retiro de dinero (" + prodWithQuantity[0].substring(7) + ") [$" + prodWithQuantity[1] + "]",
                        Double.parseDouble(prodWithQuantity[1]),
                        0,
                        "U",
                        false,
                        0
                );
            } else {
                product = getProductByCode(prodWithQuantity[0]);
                product.setQuantity(Integer.parseInt(prodWithQuantity[1]));
            }
            products1.add(product);
        }
        return products1;
    }
}
