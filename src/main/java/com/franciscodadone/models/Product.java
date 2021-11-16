package com.franciscodadone.models;

public class Product {

    public Product(String code, String prodName, double price, int quantity) {
        this.code       = code;
        this.prodName = prodName;
        this.price    = price;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public String getProdName() {
        return prodName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return code + ":" + price + ":" + quantity;
    }

    private String code;
    private String prodName;
    private double price;
    private int    quantity;

}