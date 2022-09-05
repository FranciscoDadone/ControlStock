package com.franciscodadone.model.remote;

import com.franciscodadone.util.Logger;
import com.franciscodadone.util.Configuration;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import sun.rmi.runtime.Log;

import java.net.URL;
import java.net.URLConnection;

public class MongoConnection {

    public static void connect() {
        if (!Configuration.getSaveRemote()) {
            MongoStatus.connected = false;
            Logger.log("Remote backups off.");
            return;
        }

        MongoDatabase database = null;
        if(!Configuration.getUsername().equals("") || !Configuration.getPassword().equals("")) {
            ConnectionString connectionString = new ConnectionString("mongodb+srv://" + Configuration.getUsername() + ":" + Configuration.getPassword() + "@" + Configuration.getUrl());
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("ControlStock");

            Logger.log("Mongo connected!");
            MongoStatus.connected = true;
        } else {
            Logger.log("Please fill up the MongoDB credentials under ControlStock/database/mongoCrendentials.yml to backup the data!");
            MongoStatus.connected = false;
        }

        try {
            database.listCollectionNames().iterator();
        } catch (Exception e) {
            Logger.log("Invalid mongo credentials!");
            MongoStatus.connected = false;
        }

        if(MongoStatus.connected) {
            mongoDatabase = database;
            mongoSessions = mongoDatabase.getCollection("Sessions");
            mongoSells    = mongoDatabase.getCollection("Sells");
            mongoUtil     = mongoDatabase.getCollection("Util");
            mongoStock    = mongoDatabase.getCollection("Stock");
        }

        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    URL u = new URL("https://www.google.com");
                    URLConnection conn = u.openConnection();
                    conn.connect();
                    MongoStatus.connected = true;
                } catch (Exception e) {
                    MongoStatus.connected = false;
                }
            }
        }).start();

    }

    public static MongoDatabase getDatabase() {
        return mongoDatabase;
    }


    public static void close() {
        if(mongoClient != null) mongoClient.close();
        MongoStatus.connected = false;
        Logger.log("Connection closed.");
    }

    private static MongoClient         mongoClient;
    public static MongoDatabase        mongoDatabase;
    public static MongoCollection      mongoSells;
    public static MongoCollection      mongoSessions;
    public static MongoCollection      mongoStock;
    public static MongoCollection      mongoUtil;
}
