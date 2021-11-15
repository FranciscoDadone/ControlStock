package com.franciscodadone.models;

public class Product {

    public Product(String QR, String prodName, double price, int quantity) {
        this.QR       = QR;
        this.prodName = prodName;
        this.price    = price;
        this.quantity = quantity;
    }

    public String getQR() {
        return QR;
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

    private String QR;
    private String prodName;
    private double price;
    private int    quantity;

}
