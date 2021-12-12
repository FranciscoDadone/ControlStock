package com.franciscodadone.models;

import java.util.ArrayList;
import java.util.Date;

public class Sell {

    public Sell(ArrayList<Product> products, double price, int sessionID, Date date) {
        this.products = products;
        this.price    = price;
        this.sessionID  = sessionID;
        this.date     = date;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public int getSessionID() {
        return sessionID;
    }

    public Date getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String s = "";
        for(Product p : products) {
            s += p.getCode() + ";";
        }
        return s;
    }

    private ArrayList<Product> products;
    private int                sessionID;
    private Date               date;
    private double             price;

}
