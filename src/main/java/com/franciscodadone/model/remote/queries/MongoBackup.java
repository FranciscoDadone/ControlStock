package com.franciscodadone.model.remote.queries;

import com.franciscodadone.model.local.queries.ProductsQueries;
import com.franciscodadone.model.local.queries.SellQueries;
import com.franciscodadone.model.local.queries.SessionsQueries;
import com.franciscodadone.model.models.Product;
import com.franciscodadone.model.models.Sell;
import com.franciscodadone.model.models.Session;
import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.model.remote.MongoStatus;
import com.franciscodadone.util.JCustomOptionPane;
import com.franciscodadone.util.Logger;
import com.franciscodadone.util.exceptions.MongoNotConnected;

import javax.swing.*;
import java.util.ArrayList;

public class MongoBackup {

    public static void Backup() {
        Logger.log("Connecting to Mongo...");

        try {
            MongoConnection.connect();
        } catch (Exception e) {
            Logger.log("Please fill up the MongoDB credentials under ControlStock/database/mongoCrendentials.yml to backup the data! Or check your internet connection!");
            MongoStatus.connected = false;
        }
        if(MongoStatus.connected) {
            RemoteGlobalQueries.checkCollections();

            boolean isSellsOutdated = false,
                    isStockOutdated = false,
                    isSessionsOutdated = false,
                    isUtilOutdated = false;

            ArrayList<Sell> allSells = SellQueries.getAllSells();
            ArrayList<Product> allProducts = ProductsQueries.getAllProducts();
            ArrayList<Session> allSessions = SessionsQueries.getAllSessions();
            Logger.log("Finished fetching local DB.");

            try {
                isSellsOutdated    = RemoteSellQueries.isDatabaseOutdated(allSells);
                Logger.log("Done checking Sells. Outdated:" + isSellsOutdated);
                isStockOutdated    = RemoteStockQueries.isDatabaseOutdated(allProducts);
                Logger.log("Done checking Stock. Outdated:" + isStockOutdated);
                isSessionsOutdated = RemoteSessionsQueries.isDatabaseOutdated(allSessions);
                Logger.log("Done checking Sessions. Outdated:" + isSessionsOutdated);
                isUtilOutdated     = RemoteUtilQueries.isDatabaseOutdated();
                Logger.log("Done checking Util. Outdated:" + isUtilOutdated);
            } catch (MongoNotConnected e) {
                e.printStackTrace();
            }

            if(isSellsOutdated || isStockOutdated || isSessionsOutdated || isUtilOutdated) {
                int res = JCustomOptionPane.confirmDialog("<html>Se ha detectado que la base de datos local está desactualizada.<br>¿Desea actualizarla?</html>", "Actualizar base de datos");
                if(res == JOptionPane.YES_OPTION) {
                    if(isStockOutdated)    RemoteStockQueries.retrieveFromRemote();
                    if(isSellsOutdated)    RemoteSellQueries.retrieveFromRemote();
                    if(isSessionsOutdated) RemoteSessionsQueries.retrieveFromRemote();
                    if(isUtilOutdated)     RemoteUtilQueries.retrieveFromRemote();

                    int res1 = JCustomOptionPane.confirmDialog("<html>Se ha recuperado la información de manera exitosa.<br>¿Desea reiniciar la aplicación para aplicar los cambios?</html>", "Reiniciar");
                    if(res1 == JOptionPane.YES_OPTION) {
                        MongoConnection.close();
                        System.exit(0);
                    }
                }
            }
            Logger.log("Done with Mongo backup!");
        }
    }
}
