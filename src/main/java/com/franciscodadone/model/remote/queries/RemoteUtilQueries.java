package com.franciscodadone.model.remote.queries;

import com.franciscodadone.model.local.queries.UtilQueries;
import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.model.remote.MongoStatus;
import com.franciscodadone.util.Logger;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Updates.set;

public class RemoteUtilQueries {

    private static long getMongoUtilCount() {
        MongoConnection mongoConnection = new MongoConnection();
        long count = mongoConnection.mongoUtil.countDocuments(new Document());
        mongoConnection.close();
        return count;
    }

    protected static boolean isDatabaseOutdated() {
        MongoConnection mongoConnection = new MongoConnection();
        Logger.log("Checking (Util) Collection on Mongo...");

        long localRegisteredUtils  = (UtilQueries.getLastCustomCode().equals("C0")) ? 0 : 1;
        long remoteRegisteredUtils = getMongoUtilCount();

        if(localRegisteredUtils > remoteRegisteredUtils) { // makes the backup on remote
            backupLastCode(UtilQueries.getLastCustomCode());
        } else if(localRegisteredUtils == 0 && localRegisteredUtils != remoteRegisteredUtils) { // if the local database is empty, retrieves from remote
            return true;
        }

        if(!UtilQueries.getLastCustomCode().equals(getLastCustomCode())) {
            setLastCustomCode(UtilQueries.getLastCustomCode());
        }

        mongoConnection.close();
        return false;
    }

    protected static void retrieveFromRemote() {
        Logger.log("Retrieving utils from remote.");
        UtilQueries.modifyLastCode(getLastCustomCode(), false);
    }

    private static void backupLastCode(String code) {
        new Thread(() -> {
            MongoConnection mongoConnection = new MongoConnection();
            Logger.log("Making backup of Util (last code) code=" + code);
            mongoConnection.mongoUtil.insertOne(new Document()
                    .append("customQR", code)
                    .append("id", 1)
            );
            mongoConnection.close();
        }).start();
    }

    public static void setLastCustomCode(String newCode) {
        if(MongoStatus.connected) {
            Logger.log("Editing Util (Custom QR) code=" + newCode);
            new Thread(() -> {
                MongoConnection mongoConnection = new MongoConnection();
                Bson filter = Filters.eq("id", 1);

                Bson updateStartMoney = set("customQR", newCode);

                Bson updates = Updates.combine(updateStartMoney);
                mongoConnection.mongoUtil.updateOne(filter, updates);

                mongoConnection.close();
            }).start();
        }
    }

    private static String getLastCustomCode() {
        MongoConnection mongoConnection = new MongoConnection();
        Document customQR = (Document) mongoConnection.mongoUtil.find(Filters.eq("id", 1)).first();
        return (customQR == null) ? null : customQR.getString("customQR");
    }

}
