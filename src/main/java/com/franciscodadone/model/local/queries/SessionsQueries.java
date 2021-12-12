package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.models.Product;
import com.franciscodadone.models.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Sessions WHERE active=1;");
            DateFormat format = new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            while(res.next()) {
                session = new Session(
                        Integer.parseInt(res.getString("id")),
                        res.getString("seller"),
                        format.parse(res.getString("dateStarted")),
                        (res.getString("dateEnded") == null) ? null : format.parse(res.getString("dateEnded")),
                        res.getDouble("startMoney"),
                        res.getDouble("endMoney")
                );
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
            return session;
        }
    }

    public static void endCurrentSession(double sessionEndMoney) {
        Session currentSession = getActiveSession();
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "UPDATE Sessions SET active=0 WHERE id=" + currentSession.getId() + ";"
            );
            connection.createStatement().execute(
                    "UPDATE Sessions SET dateEnded='" + new Date() + "' WHERE id=" + currentSession.getId() + ";"
            );
            connection.createStatement().execute(
                    "UPDATE Sessions SET endMoney=" + sessionEndMoney + " WHERE id=" + currentSession.getId() + ";"
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
