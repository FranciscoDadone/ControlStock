package com.franciscodadone.model.remote.queries;

import com.franciscodadone.model.local.SQLiteConnection;
import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.util.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RemoteGlobalQueries {

    /**
     * Checks if there are the same tables in the sqlite database and the mongo.
     * If the mongo leaks a table (collection) it creates it.
     */
    protected static void checkCollections() {

        Logger.log("Checking collections...");

        sqliteDatabase = SQLiteConnection.connect();
        ResultSet res;
        ArrayList<String> tables = new ArrayList<>();
        try {
            res = sqliteDatabase.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
            while(res.next()) {
                tables.add(res.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            sqliteDatabase.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        MongoConnection mongoConnection = new MongoConnection();
        ArrayList<String> mongoCollections = new ArrayList<>();
        mongoConnection.mongoDatabase.listCollectionNames().forEach((collection) -> {
            mongoCollections.add(collection);
        });

        for(String sqliteTableName : tables) {
            if(!sqliteTableName.equals("sqlite_sequence")) {
                if(!mongoCollections.contains(sqliteTableName)) {
                    mongoConnection.mongoDatabase.createCollection(sqliteTableName);
                    Logger.log("Mongo collection created: " + sqliteTableName);
                }
            }
        }
        mongoConnection.close();
    }

    private static java.sql.Connection sqliteDatabase;
}
