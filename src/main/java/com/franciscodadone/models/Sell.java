package com.franciscodadone.models;

import com.franciscodadone.util.FDate;
import java.util.ArrayList;

public class Sell {

    public Sell(ArrayList<Product> products, double price, int sessionID, FDate date) {
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

    public FDate getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < products.size(); i++) {
            s += products.get(i).getCode() + ":" + products.get(i).getQuantity() + ((i+1 == products.size()) ? "" : ";");
        }
        return s;
    }

    private ArrayList<Product> products;
    private int                sessionID;
    private FDate              date;
    private double             price;

}
