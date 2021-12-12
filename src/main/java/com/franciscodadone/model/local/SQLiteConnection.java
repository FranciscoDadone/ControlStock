package com.franciscodadone.model.local;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class SQLiteConnection {
    /**
     * Connects to the database.
     *
     * If the database doesn't exist, it creates it.
     *
     * Creation of tables 'Sells', 'History', 'Stock' if it doesn't exist.
     *
     * @return Connection
     */
    public static java.sql.Connection connect() {
        java.sql.Connection con = null;
        String dbPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/ControlStock/database";
        try {
            File theDir = new File(dbPath);
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            con = DriverManager.getConnection("jdbc:sqlite:" + dbPath + "/sqlite.db");
            ResultSet res = con.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
            ArrayList<String> tables = new ArrayList<>();
            while(res.next()) {
                tables.add(res.getString(1));
            }
            /**
             * Creation of table Sessions if it doesn't exist.
             */
            if(!tables.contains("Sessions")) {
                Statement statement = con.createStatement();
                statement.execute("CREATE TABLE Sessions (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "startMoney DOUBLE," +
                        "endMoney DOUBLE," +
                        "seller VARCHAR(100)," +
                        "dateStarted VARCHAR(255)," +
                        "dateEnded VARCHAR(255)" +
                        ");"
                );
            }

            /**
             * Creation of table Sells if it doesn't exist.
             */
            if(!tables.contains("Sells")) {
                Statement statement = con.createStatement();
                statement.execute("CREATE TABLE Sells (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "products TEXT,"     +
                        "date VARCHAR(255)," +
                        "totalPrice DOUBLE," +
                        "seller VARCHAR(40)," +
                        "sessionID INTEGER"  +
                        ");"
                );
            }

            /**
             * Creation of table Sellers if it doesn't exist.
             */
            if(!tables.contains("Sellers")) {
                Statement statement = con.createStatement();
                statement.execute("CREATE TABLE Sellers (" +
                        "id INTEGER, " +
                        "name VARCHAR(40)" +
                        ");"
                );
            }

            /**
             * Creation of table Sellers if it doesn't exist.
             */
            if(!tables.contains("Stock")) {
                Statement statement = con.createStatement();
                statement.execute("CREATE TABLE Stock (" +
                        "code VARCHAR(50), " +
                        "title VARCHAR(40), " +
                        "quantity INTEGER, " +
                        "price DOUBLE, " +
                        "quantityType VARCHAR(1)," +
                        "deleted INTEGER);"
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }
}
