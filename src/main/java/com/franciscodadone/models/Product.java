package com.franciscodadone.models;

public class Product {

    public Product(String code, String prodName, double price, int quantity, String quantityType) {
        this.code         = code;
        this.prodName     = prodName;
        this.price        = price;
        this.quantity     = quantity;
        this.quantityType = quantityType;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
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

    public String getQuantityType() {
        return quantityType;
    }

    public String getProductFormatted() {
        return code + ":" + price + ":" + quantity;
    }

    @Override
    public String toString() {
        return prodName;
    }

    private String code;
    private String prodName;
    private double price;
    private int    quantity;
    private String quantityType;

}
