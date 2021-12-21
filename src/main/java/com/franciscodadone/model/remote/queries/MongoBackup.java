package com.franciscodadone.model.remote.queries;

import com.franciscodadone.model.remote.MongoConnection;
import com.franciscodadone.model.remote.MongoStatus;
import com.franciscodadone.util.JCustomOptionPane;
import com.franciscodadone.util.Logger;

import javax.swing.*;

public class MongoBackup {

    public static void Backup() {
        Logger.log("Initializing remote backup...");
        try {
            new MongoConnection().close();
        } catch (Exception e) {
            Logger.log("Please fill up the MongoDB credentials under ControlStock/database/mongoCrendentials.yml to backup the data! Or check your internet connection!");
            MongoStatus.connected = false;
        }
        if(MongoStatus.connected) {
            RemoteGlobalQueries.checkCollections();

            boolean isSellsOutdated    = RemoteSellQueries.isDatabaseOutdated();
            Logger.log("Done checking Sells. Outdated:" + isSellsOutdated);
            boolean isStockOutdated    = RemoteStockQueries.isDatabaseOutdated();
            Logger.log("Done checking Stock. Outdated:" + isStockOutdated);
            boolean isSessionsOutdated = RemoteSessionsQueries.isDatabaseOutdated();
            Logger.log("Done checking Sessions. Outdated:" + isSessionsOutdated);
            boolean isUtilOutdated     = RemoteUtilQueries.isDatabaseOutdated();
            Logger.log("Done checking Util. Outdated:" + isUtilOutdated);

            if(isSellsOutdated || isStockOutdated || isSessionsOutdated || isUtilOutdated) {
                int res = JCustomOptionPane.confirmDialog("<html>Se ha detectado que la base de datos local está desactualizada.<br>¿Desea actualizarla?</html>", "Actualizar base de datos");
                if(res == JOptionPane.YES_OPTION) {
                    if(isStockOutdated)    RemoteStockQueries.retrieveFromRemote();
                    if(isSellsOutdated)    RemoteSellQueries.retrieveFromRemote();
                    if(isSessionsOutdated) RemoteSessionsQueries.retrieveFromRemote();
                    if(isUtilOutdated)     RemoteUtilQueries.retrieveFromRemote();

                    int res1 = JCustomOptionPane.confirmDialog("<html>Se ha recuperado la información de manera exitosa.<br>¿Desea reiniciar la aplicación para aplicar los cambios?</html>", "Reiniciar");
                    if(res1 == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            }
        }
    }
}
