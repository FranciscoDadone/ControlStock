package com.franciscodadone.models;

import java.util.ArrayList;
import java.util.Date;

public class Sell {

    public Sell(ArrayList<Product> products, Seller seller, double price, Session session, Date date) {
        this.products = products;
        this.seller   = seller;
        this.price    = price;
        this.session  = session;
        this.date     = date;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public Seller getSeller() {
        return seller;
    }

    public Session getSession() {
        return session;
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
            s += p.toString() + ";";
        }
        return s;
    }

    private ArrayList<Product> products;
    private Seller             seller;
    private Session            session;
    private Date               date;
    private double             price;

}
