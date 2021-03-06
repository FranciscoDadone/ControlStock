package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.models.Sell;
import com.franciscodadone.model.models.Session;
import com.franciscodadone.model.remote.queries.RemoteSellQueries;
import com.franciscodadone.util.FDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SellQueries extends SQLiteConnection {

    /**
     * Adds a new sell to the database.
     * @param sell
     */
    public static void saveSell(Sell sell, boolean saveRemote) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "INSERT INTO Sells (products, date, totalPrice, sessionID, posnet) VALUES (" +
                            "'" + sell.toString()            + "'," +
                            "'" + sell.getDate().toString()  + "'," +
                                  sell.getPrice()            + "," +
                                  sell.getSessionID()        + "," +
                                  sell.isViaPosnet()         + ");"
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
        if(saveRemote) {
            RemoteSellQueries.backupSell(sell);
        }
    }

    public static ArrayList<Sell> getAllSellsFromSession(Session session) {
        ArrayList<Sell> sells = new ArrayList<>();
        ArrayList<Product> allProducts = ProductsQueries.getAllProducts();
        java.sql.Connection connection = connect();
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Sells WHERE sessionID=" + session.getId() + ";");
            while(res.next()) {
                sells.add(new Sell(
                        res.getInt("id"),
                        ProductsQueries.getProductsOptimized(res.getString("products"), allProducts),
                        res.getDouble("totalPrice"),
                        session.getId(),
                        new FDate(res.getString("date")),
                        res.getBoolean("posnet")
                ));
            }
        } catch (SQLException e) {}
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return sells;
        }
    }

    public static ArrayList<Sell> getAllSells() {
        ArrayList<Sell> sells = new ArrayList<>();
        java.sql.Connection connection = connect();
        ArrayList<Product> allProducts = ProductsQueries.getAllProducts();

        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Sells;");
            while(res.next()) {
                String productsStr = res.getString("products");
                ArrayList<Product> products = new ArrayList<>();
                if(productsStr.length() != 0) {
                    String[] prod = productsStr.split(";");
                    for(int i = 0; i < prod.length; i++) {
                        String[] prodWithQuantity = prod[i].split(":");
                        Product product = null;
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
                            for (Product p: allProducts) {
                                if (p.getCode().equals(prodWithQuantity[0])) {
                                    product = p;
                                    product.setQuantity(Integer.parseInt(prodWithQuantity[1]));
                                    break;
                                }
                            }
                        }
                        if (product != null) products.add(product);
                    }
                }

                sells.add(new Sell(
                        res.getInt("id"),
                        products, // ProductsQueries.getProducts(res.getString("products")),
                        res.getDouble("totalPrice"),
                        res.getInt("sessionID"),
                        new FDate(res.getString("date")),
                        res.getBoolean("posnet")
                ));
            }
        } catch (SQLException e) {}
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return sells;
        }
    }

    public static int getSellID(Sell sell) {
        int id = -1;
        java.sql.Connection connection = connect();
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Sells WHERE " +
                    "date='" + sell.getDate().toString() + "' AND " +
                    "totalPrice=" + sell.getPrice() + ";");
            while(res.next()) {
                id = res.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return id;
        }
    }
}
