package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.models.Sell;
import java.sql.SQLException;

public class SellQueries extends SQLiteConnection {

    /**
     * Adds a new sell to the database.
     * @param sell
     */
    public static void saveSell(Sell sell) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "INSERT INTO Sells (products, date, totalPrice, seller, sessionID) VALUES (" +
                            "'" + sell.toString()            + "'," +
                            "'" + sell.getDate().toString()  + "'," +
                                  sell.getPrice()            + "," +
                            "'" + sell.getSeller().getName() + "'," +
                                  sell.getSession().getId()  + ");"
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
