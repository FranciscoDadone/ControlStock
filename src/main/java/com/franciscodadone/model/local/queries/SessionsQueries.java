package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.models.Sell;
import com.franciscodadone.model.models.Session;
import com.franciscodadone.model.remote.queries.RemoteSessionsQueries;
import com.franciscodadone.util.FDate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SessionsQueries extends SQLiteConnection {

    public static Session startSession(double startMoney, String seller, FDate dateStarted) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "INSERT INTO Sessions (startMoney, seller, dateStarted, active) VALUES (" +
                                  startMoney             + "," +
                            "'" + seller                 + "'," +
                            "'" + dateStarted + "'," +
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

            while(res.next()) {
                session = new Session(
                        Integer.parseInt(res.getString("id")),
                        res.getString("seller"),
                        new FDate(res.getString("dateStarted")),
                        new FDate(res.getString("dateEnded")),
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
        currentSession.setDateEnded(new FDate());
        currentSession.setEndMoney(sessionEndMoney);
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "UPDATE Sessions SET active=0 WHERE id=" + currentSession.getId() + ";"
            );
            connection.createStatement().execute(
                    "UPDATE Sessions SET dateEnded='" + currentSession.getDateEnded().toString() + "' WHERE id=" + currentSession.getId() + ";"
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
        RemoteSessionsQueries.backupSession(currentSession);
    }

    public static ArrayList<Session> getAllSessions() {
        ArrayList<Session> sessions = new ArrayList<>();
        java.sql.Connection connection = connect();
        try {
            ResultSet res = connection.createStatement().executeQuery("SELECT * FROM Sessions WHERE active=0;");

            while(res.next()) {
                sessions.add(new Session(
                        Integer.parseInt(res.getString("id")),
                        res.getString("seller"),
                        new FDate(res.getString("dateStarted")),
                        new FDate(res.getString("dateEnded")),
                        res.getDouble("startMoney"),
                        res.getDouble("endMoney")
                ));
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
            return sessions;
        }
    }

    public static void saveSession(Session session) {
        java.sql.Connection connection = connect();
        try {
            connection.createStatement().execute(
                    "INSERT INTO Sessions (startMoney, endMoney, seller, dateStarted, dateEnded, active) VALUES (" +
                            session.getStartMoney()        + "," +
                            session.getEndMoney()          + "," +
                            "'" + session.getSeller()      + "'," +
                            "'" + session.getDateStarted() + "'," +
                            "'" + session.getDateEnded()   + "'," +
                                  false                    + ");"
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
