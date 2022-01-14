package com.franciscodadone.model.remote.queries;

import com.franciscodadone.model.local.queries.UtilQueries;
import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.model.remote.MongoStatus;
import com.franciscodadone.util.Logger;
import com.franciscodadone.util.exceptions.MongoNotConnected;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Updates.set;

public class RemoteUtilQueries {

    private static long getMongoUtilCount() {
        long count = MongoConnection.mongoUtil.countDocuments(new Document());
        return count;
    }

    protected static boolean isDatabaseOutdated() throws MongoNotConnected {
        boolean outdated = false;

        if(!MongoStatus.connected) {
            throw new MongoNotConnected();
        }
        Logger.log("Checking (Util) Collection on Mongo...");

        long localRegisteredUtils  = (UtilQueries.getLastCustomCode().equals("C0")) ? 0 : 1;
        long remoteRegisteredUtils = getMongoUtilCount();

        if(localRegisteredUtils > remoteRegisteredUtils) { // makes the backup on remote
            backupLastCode(UtilQueries.getLastCustomCode());
        } else if(localRegisteredUtils == 0 && localRegisteredUtils != remoteRegisteredUtils) { // if the local database is empty, retrieves from remote
            outdated = true;
        }

        if(!UtilQueries.getLastCustomCode().equals(getLastCustomCode())) {
            setLastCustomCode(UtilQueries.getLastCustomCode());
        }

        return outdated;
    }

    protected static void retrieveFromRemote() {
        Logger.log("Retrieving utils from remote.");
        UtilQueries.modifyLastCode(getLastCustomCode(), false);
    }

    private static void backupLastCode(String code) {
        Logger.log("Making backup of Util (last code) code=" + code);
        MongoConnection.mongoUtil.insertOne(new Document()
                .append("customQR", code)
                .append("id", 1)
        );
    }

    public static void setLastCustomCode(String newCode) {
        Logger.log("Editing Util (Custom QR) code=" + newCode);
        Bson filter = Filters.eq("id", 1);

        Bson updateStartMoney = set("customQR", newCode);

        Bson updates = Updates.combine(updateStartMoney);
        MongoConnection.mongoUtil.updateOne(filter, updates);
    }

    private static String getLastCustomCode() {
        Document customQR = (Document) MongoConnection.mongoUtil.find(Filters.eq("id", 1)).first();
        return (customQR == null) ? null : customQR.getString("customQR");
    }
}
