package com.franciscodadone.models;

import java.util.ArrayList;
import java.util.Date;

public class Session {

    public Session(Seller seller, Date date, double startMoney) {
        this.seller     = seller;
        this.date       = date;
        this.startMoney = startMoney;
    }

    public Session(Seller seller, Date date, double startMoney, double endMoney, ArrayList<Sell> sells) {
        this.seller     = seller;
        this.date       = date;
        this.startMoney = startMoney;
        this.endMoney   = endMoney;
        this.sells      = sells;
    }

    private Seller          seller;
    private Date            date;
    private double          startMoney;
    private double          endMoney;
    private ArrayList<Sell> sells;

}
