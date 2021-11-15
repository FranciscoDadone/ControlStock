package com.franciscodadone.models;

import java.util.ArrayList;
import java.util.Date;

public class Session {

    public Session(Seller seller, Date date, double startMoney) {
        this.seller     = seller;
        this.date       = date;
        this.startMoney = startMoney;
    }

    public Session(int id, Seller seller, Date date, double startMoney, double endMoney, ArrayList<Sell> sells) {
        this.id         = id;
        this.seller     = seller;
        this.date       = date;
        this.startMoney = startMoney;
        this.endMoney   = endMoney;
        this.sells      = sells;
    }

    public Seller getSeller() {
        return seller;
    }

    public Date getDate() {
        return date;
    }

    public double getStartMoney() {
        return startMoney;
    }

    public double getEndMoney() {
        return endMoney;
    }

    public ArrayList<Sell> getSells() {
        return sells;
    }

    public int getId() {
        return id;
    }

    private Seller          seller;
    private Date            date;
    private double          startMoney;
    private double          endMoney;
    private ArrayList<Sell> sells;
    private int             id;

}
