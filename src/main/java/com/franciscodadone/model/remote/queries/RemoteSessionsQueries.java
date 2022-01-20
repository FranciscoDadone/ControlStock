package com.franciscodadone.model.remote.queries;

import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.model.models.Session;
import com.franciscodadone.model.remote.MongoStatus;
import com.franciscodadone.util.FDate;
import com.franciscodadone.util.Logger;
import com.franciscodadone.util.exceptions.MongoNotConnected;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

import static com.mongodb.client.model.Updates.set;

public class RemoteSessionsQueries {

    private static long getMongoSessionsCount() {
        long count = MongoConnection.mongoSessions.countDocuments(new Document());
        return count;
    }

    protected static boolean isDatabaseOutdated() throws MongoNotConnected {
        boolean outdated = false;

        if(!MongoStatus.connected) {
            throw new MongoNotConnected();
        }
        Logger.log("Checking (Sessions) Collection on Mongo...");

        long localRegisteredSessions  = SessionsQueries.getAllSessions().size();
        long remoteRegisteredSessions = getMongoSessionsCount();

        if(localRegisteredSessions > remoteRegisteredSessions) { // makes the backup on remote
            SessionsQueries.getAllSessions().forEach((session) -> {
                Document mongoQuery = (Document) MongoConnection.mongoSessions.find(Filters.eq("id", session.getId())).first();
                if(mongoQuery == null) {
                    backupSession(session);
                }
            });
        } else if(localRegisteredSessions == 0 && localRegisteredSessions != remoteRegisteredSessions) { // if the local database is empty, retrieves from remote
            outdated = true;
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
        return outdated;
    }

    protected static void retrieveFromRemote() {
        getAllSessions().forEach((remoteSession) -> {
            Logger.log("Retrieving session from remote. id=" + remoteSession.getId());
            SessionsQueries.saveSession(remoteSession);
        });

        SessionsQueries.getAllSessions().forEach((session) -> {
            updateSessionID(session);
        });
    }

    private static void updateSessionID(Session session) {
        Logger.log("Updating remote id of Session id=" + session.getId());

        Bson filter = Filters.eq("dateStarted", session.getDateStarted().toString());
        Bson updateOperation = set("id", session.getId());
        MongoConnection.mongoSells.updateOne(filter, updateOperation);
    }

    public static void backupSession(Session session) {
        if(MongoStatus.connected) {
            new Thread(() -> {
                Logger.log("Making backup of Session id=" + session.getId());
                MongoConnection.mongoSessions.insertOne(new Document()
                        .append("id", session.getId())
                        .append("seller", session.getSeller())
                        .append("dateStarted", session.getDateStarted().toString())
                        .append("dateEnded", session.getDateEnded().toString())
                        .append("startMoney", session.getStartMoney())
                        .append("endMoney", session.getEndMoney())
                );
            }).start();
        }
    }

    public static void editSession(Session session) {
        if(MongoStatus.connected) {
            new Thread(() -> {
                Logger.log("Editing Session id=" + session.getId());
                Bson filter = Filters.eq("id", session.getId());

                Bson updateStartMoney  = set("startMoney", session.getStartMoney());
                Bson updateEndMoney    = set("endMoney", session.getEndMoney());
                Bson updateSeller      = set("seller", session.getSeller());
                Bson updateDateStarted = set("dateStarted", session.getDateStarted().toString());
                Bson updateDateEnded   = set("dateEnded", session.getDateEnded().toString());

                Bson updates = Updates.combine(updateStartMoney, updateEndMoney, updateDateStarted, updateSeller, updateDateEnded);
                MongoConnection.mongoSessions.updateOne(filter, updates);
            }).start();
        }
    }

    private static ArrayList<Session> getAllSessions() {
        FindIterable remoteSessions = MongoConnection.mongoSessions.find();

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
        return sessions;
    }
}
