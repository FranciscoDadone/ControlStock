package com.franciscodadone.model.local.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.model.remote.queries.RemoteUtilQueries;

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
