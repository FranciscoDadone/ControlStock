package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.models.Product;
import com.franciscodadone.models.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionsQueries extends SQLiteConnection {

    public static Session startSession(double startMoney, String seller, Date dateStarted) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "INSERT INTO Sessions (startMoney, seller, dateStarted, active) VALUES (" +
                                  startMoney             + "," +
                            "'" + seller                 + "'," +
                            "'" + dateStarted.toString() + "'," +
                                  true                   + ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                return getActiveSession();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Session getActiveSession() {
        Session session = null;
        java.sql.Connection connection = connect();
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Sessions WHERE (active=1);");
            while(res.next()) {
                session = new Session(
                        res.getInt("id"),
                        res.getString("seller"),
                        new SimpleDateFormat("dd/MM/yyyy").parse(res.getString("dateStarted")),
                        new SimpleDateFormat("dd/MM/yyyy").parse(res.getString("dateEnded")),
                        res.getDouble("startMoney"),
                        res.getDouble("endMoney")
                );
            }
        } catch (SQLException e) {}
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return session;
        }
    }

}
