package com.franciscodadone;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.franciscodadone.model.remote.queries.MongoBackup;
import com.franciscodadone.util.GUIHandler;
import com.franciscodadone.util.Sound;
import org.slf4j.LoggerFactory;

public class main {

    public static void main(String[] args) {

        new Sound().playBeep();

        GUIHandler.main();

        com.franciscodadone.util.Logger.log("Booting up...");

        // Disabling all the logging in mongo connection
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);

        // Initializing remote backup
        MongoBackup.Backup();

    }

}
