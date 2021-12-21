package com.franciscodadone.model.remote;

import com.franciscodadone.util.Logger;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

    public MongoConnection() {
        MongoDatabase database = null;
        if(!MongoCredentials.getCredentials().get("username").equals("") || !MongoCredentials.getCredentials().get("password").equals("")) {
            ConnectionString connectionString = new ConnectionString("mongodb+srv://" + MongoCredentials.getCredentials().get("username") + ":" + MongoCredentials.getCredentials().get("password") + "@" + MongoCredentials.getCredentials().get("url"));
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("ControlStock");
        } else {
            Logger.log("Please fill up the MongoDB credentials under ControlStock/database/mongoCrendentials.yml to backup the data!");
        }

        MongoStatus.connected = true;
        Logger.log("Connected to Mongo!");

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

    }

    public MongoDatabase getDatabase() {
        return mongoDatabase;
    }


    public void close() {
        if(mongoClient != null) mongoClient.close();
    }

    private MongoClient        mongoClient;
    public MongoDatabase       mongoDatabase;
    public MongoCollection     mongoSells;
    public MongoCollection     mongoSessions;
    public MongoCollection     mongoStock;
    public MongoCollection     mongoUtil;
}
