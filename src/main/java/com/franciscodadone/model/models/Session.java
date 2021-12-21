package com.franciscodadone.model.models;

import com.franciscodadone.model.local.queries.SellQueries;
import com.franciscodadone.util.FDate;

public class Session {

    public Session(String seller, FDate dateStarted, double startMoney) {
        this.seller      = seller;
        this.dateStarted = dateStarted;
        this.startMoney  = startMoney;
        this.active      = true;
    }

    public Session(int id, String seller, FDate dateStarted, FDate dateEnded, double startMoney, double endMoney) {
        this.id          = id;
        this.seller      = seller;
        this.dateStarted = dateStarted;
        this.dateEnded   = dateEnded;
        this.startMoney  = startMoney;
        this.endMoney    = endMoney;
    }

    public String getSeller() {
        return seller;
    }

    public double getStartMoney() {
        return startMoney;
    }

    public double getEndMoney() {
        return endMoney;
    }

    public int getId() {
        return id;
    }

    public void setEndMoney(double endMoney) {
        this.endMoney = endMoney;
    }

    public void setDateEnded(FDate dateEnded) {
        this.dateEnded = dateEnded;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public FDate getDateStarted() {
        return dateStarted;
    }

    public FDate getDateEnded() {
        return dateEnded;
    }

    public double getEarnings() {
        double earnings = 0;
        for(Sell sell : SellQueries.getAllSellsFromSession(this)) {
            earnings += sell.getPrice();
        }
        return earnings;
    }

    private int     id;
    private double  startMoney;
    private double  endMoney;
    private String  seller;
    private FDate   dateStarted;
    private FDate   dateEnded;
    private boolean active;

}
