package com.franciscodadone.model.remote.queries;

import com.franciscodadone.model.local.queries.ProductsQueries;
import com.franciscodadone.model.local.queries.SellQueries;
import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.model.models.Sell;
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

public class RemoteSellQueries {

    private static long getMongoSellsCount() {
        long count = MongoConnection.mongoSells.countDocuments(new Document());
        return count;
    }

    protected static boolean isDatabaseOutdated() throws MongoNotConnected {
        if(!MongoStatus.connected) {
            throw new MongoNotConnected();
        }
        Logger.log("Checking (Sells) Collection on Mongo...");

        long localRegisteredSells = SellQueries.getAllSells().size();
        long remoteRegisteredSells = getMongoSellsCount();

        if(localRegisteredSells > remoteRegisteredSells) { // makes the backup on remote
            SellQueries.getAllSells().forEach((sell) -> {
                Document mongoQuery = (Document) MongoConnection.mongoSells.find(Filters.eq("id", sell.getId())).first();
                if(mongoQuery == null) {
                    backupSell(sell);
                }
            });
            // localRegisteredSells == 0 && localRegisteredSells != remoteRegisteredSells
        } else if(localRegisteredSells < remoteRegisteredSells) { // if the local database is empty, retrieves from remote
            return true;
        }

        ArrayList<Sell> remoteSells = getAllSells();
        for(Sell localSell : SellQueries.getAllSells()) {
            if(remoteSells.contains(localSell)) break;
            for(Sell remoteSell : remoteSells) {
                if(localSell.getId() == remoteSell.getId()) {
                    if(
                            localSell.getId() != remoteSell.getId() ||
                            !localSell.getDate().toString().equals(remoteSell.getDate().toString()) ||
                            localSell.getSessionID() != remoteSell.getSessionID() ||
                            localSell.getPrice() != remoteSell.getPrice()
                    ) {
                        editSell(localSell);
                    }
                    break;
                }
            }
        }
        return false;
    }

    protected static void retrieveFromRemote() {
        getAllSells().forEach((remoteSell) -> {
            if(SellQueries.getSellID(remoteSell) == -1) {
                Logger.log("Retrieving sell from remote. id=" + remoteSell.getId());
                SellQueries.saveSell(remoteSell, false);
            }
        });

        SellQueries.getAllSells().forEach((localSell) -> {
            updateSellID(localSell);
        });
    }

    private static void updateSellID(Sell sell) {
        Logger.log("Updating remote id of sell id=" + sell.getId());
        Bson filter = Filters.eq("date", sell.getDate().toString());
        Bson updateOperation = set("id", sell.getId());
        MongoConnection.mongoSells.updateOne(filter, updateOperation);
    }

    public static void backupSell(Sell sell) {
        if(MongoStatus.connected) {
            int id = SellQueries.getSellID(sell);
            Logger.log("Making backup of sell id=" + id);
            MongoConnection.mongoSells.insertOne(new Document()
                    .append("id", id)
                    .append("products", sell.toString())
                    .append("totalPrice", sell.getPrice())
                    .append("sessionID", sell.getSessionID())
                    .append("date", sell.getDate().toString())
            );
        }
    }

    public static void editSell(Sell sell) {
        if(MongoStatus.connected) {
            Logger.log("Editing sell id=" + sell.getId());

            Bson filter = Filters.eq("id", sell.getId());
            Bson updateProducts = set("products", sell.toString());
            Bson updateDate = set("date", sell.getDate().toString());
            Bson updatePrice = set("totalPrice", sell.getPrice());
            Bson updateSessionID = set("sessionID", sell.getSessionID());

            Bson updates = Updates.combine(updateProducts, updateDate, updateSessionID, updatePrice);
            MongoConnection.mongoSells.updateOne(filter, updates);
        }
    }

    private static ArrayList<Sell> getAllSells() {
        FindIterable remoteSells = MongoConnection.mongoSells.find();

        ArrayList<Sell> sells = new ArrayList<>();
        remoteSells.forEach((sell) -> {
            sells.add(new Sell(
                    ((Document)sell).getInteger("id"),
                    ProductsQueries.getProducts(((Document)sell).getString("products")),
                    ((Document)sell).getDouble("totalPrice"),
                    ((Document)sell).getInteger("sessionID"),
                    new FDate(((Document)sell).getString("date"))
            ));
        });
        return sells;
    }

}
