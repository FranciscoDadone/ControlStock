package com.franciscodadone.util;

import java.util.Date;

public class FDate {

    public FDate(String date) {
        this.date = date;
    }
    public FDate() {
        date = Util.formatDate(new Date());
    }

    @Override
    public String toString() {
        return date;

    }

    public String getDate() {
        return date;
    }

    private String date;

}
