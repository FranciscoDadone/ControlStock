package com.franciscodadone.util.exceptions;

public class MongoNotConnected extends Exception{

    public MongoNotConnected() {
        super("Mongo should be connected.");
    }

}
