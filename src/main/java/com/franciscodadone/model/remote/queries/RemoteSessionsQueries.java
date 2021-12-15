package com.franciscodadone.model.remote.queries;

import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.models.Session;
import com.franciscodadone.util.FDate;
import com.franciscodadone.util.Logger;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

import static com.mongodb.client.model.Updates.set;

public class RemoteSessionsQueries {

    private static long getMongoSessionsCount() {
        MongoConnection mongoConnection = new MongoConnection();
        long count = mongoConnection.mongoSessions.countDocuments(new Document());
        mongoConnection.close();
        return count;
    }

    protected static boolean isDatabaseOutdated() {
        MongoConnection mongoConnection = new MongoConnection();
        Logger.log("Checking (Sessions) Collection on Mongo...");

        long localRegisteredSessions  = SessionsQueries.getAllSessions().size();
        long remoteRegisteredSessions = getMongoSessionsCount();

        if(localRegisteredSessions > remoteRegisteredSessions) { // makes the backup on remote
            SessionsQueries.getAllSessions().forEach((session) -> {
                Document mongoQuery = (Document) mongoConnection.mongoSessions.find(Filters.eq("id", session.getId())).first();
                if(mongoQuery == null) {
                    backupSession(session);
                }
            });
        } else if(localRegisteredSessions == 0 && localRegisteredSessions != remoteRegisteredSessions) { // if the local database is empty, retrieves from remote
            return true;
        }

        ArrayList<Session> remoteSessions = getAllSessions();
        for(Session localSessions : SessionsQueries.getAllSessions()) {
            if(remoteSessions.contains(localSessions)) break;
            for(Session remoteProduct : remoteSessions) {
                if(localSessions.getId() == remoteProduct.getId())   {
                    if(
                            !localSessions.getSeller().equals(remoteProduct.getSeller()) ||
                                    !localSessions.getDateStarted().toString().equals(remoteProduct.getDateStarted().toString()) ||
                                    !localSessions.getDateEnded().toString().equals(remoteProduct.getDateEnded().toString()) ||
                                    localSessions.getStartMoney() != remoteProduct.getStartMoney() ||
                                    localSessions.getEndMoney() != remoteProduct.getEndMoney()
                    ) {
                        editSession(localSessions);
                    }
                    break;
                }
            }
        }

        mongoConnection.close();
        return false;
    }

    public static void retrieveFromRemote() {
        getAllSessions().forEach((remoteSession) -> {
            SessionsQueries.saveSession(remoteSession);
        });

        SessionsQueries.getAllSessions().forEach((session) -> {
            updateSessionID(session);
        });
    }

    private static void updateSessionID(Session session) {
        Logger.log("Updating remote id of Session id=" + session.getId());
        new Thread(() -> {
            MongoConnection mongoConnection = new MongoConnection();

            Bson filter = Filters.eq("dateStarted", session.getDateStarted().toString());
            Bson updateOperation = set("id", session.getId());
            mongoConnection.mongoSells.updateOne(filter, updateOperation);

            mongoConnection.close();
        }).start();
    }

    public static void backupSession(Session session) {
        new Thread(() -> {
            MongoConnection mongoConnection = new MongoConnection();
            Logger.log("Making backup of Session id=" + session.getId());
            mongoConnection.mongoSessions.insertOne(new Document()
                    .append("id", session.getId())
                    .append("seller", session.getSeller())
                    .append("dateStarted", session.getDateStarted().toString())
                    .append("dateEnded", session.getDateEnded().toString())
                    .append("startMoney", session.getStartMoney())
                    .append("endMoney", session.getEndMoney())
            );
            mongoConnection.close();
        }).start();
    }

    public static void editSession(Session session) {
        Logger.log("Editing Session id=" + session.getId());
        new Thread(() -> {
            MongoConnection mongoConnection = new MongoConnection();
            Bson filter = Filters.eq("id", session.getId());

            Bson updateStartMoney  = set("startMoney", session.getStartMoney());
            Bson updateEndMoney    = set("endMoney", session.getEndMoney());
            Bson updateSeller      = set("seller", session.getSeller());
            Bson updateDateStarted = set("dateStarted", session.getDateStarted().toString());
            Bson updateDateEnded   = set("dateEnded", session.getDateEnded().toString());

            Bson updates = Updates.combine(updateStartMoney, updateEndMoney, updateDateStarted, updateSeller, updateDateEnded);
            mongoConnection.mongoSessions.updateOne(filter, updates);

            mongoConnection.close();
        }).start();
    }

    private static ArrayList<Session> getAllSessions() {
        MongoConnection mongoConnection = new MongoConnection();
        FindIterable remoteSessions = mongoConnection.mongoSessions.find();

        ArrayList<Session> sessions = new ArrayList<>();
        remoteSessions.forEach((session) -> {
            sessions.add(new Session(
                    ((Document)session).getInteger("id"),
                    ((Document)session).getString("seller"),
                    new FDate(((Document)session).getString("dateStarted")),
                    new FDate(((Document)session).getString("dateEnded")),
                    ((Document)session).getDouble("startMoney"),
                    ((Document)session).getDouble("endMoney")
            ));
        });
        mongoConnection.close();
        return sessions;
    }

}
