package com.franciscodadone.models;

import java.util.ArrayList;
import java.util.Date;

public class Session {

    public Session(String seller, Date dateStarted, double startMoney) {
        this.seller      = seller;
        this.dateStarted = dateStarted;
        this.startMoney  = startMoney;
        this.active      = true;
    }

    public Session(int id, String seller, Date dateStarted, Date dateEnded, double startMoney, double endMoney) {
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

    public void setDateEnded(Date dateEnded) {
        this.dateEnded = dateEnded;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public Date getDateEnded() {
        return dateEnded;
    }

    private int             id;
    private double          startMoney;
    private double          endMoney;
    private String          seller;
    private Date            dateStarted;
    private Date            dateEnded;
    private boolean         active;

}
