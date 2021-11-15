package com.franciscodadone.models;

public class Seller {

    public Seller(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    private String name;
    private int id;

}
