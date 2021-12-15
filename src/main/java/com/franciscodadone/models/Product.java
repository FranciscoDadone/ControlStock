package com.franciscodadone.models;

public class Product {

    public Product(String code, String prodName, double price, int quantity, String quantityType, boolean deleted, int minQuantity) {
        this.code               = code;
        this.prodName           = prodName;
        this.unmodifiedProdName = prodName;
        this.price              = price;
        this.quantity           = quantity;
        this.quantityType       = quantityType;
        this.deleted            = deleted;
        this.minQuantity        = minQuantity;
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

    public String getUnmodifiedProdName() {
        return unmodifiedProdName;
    }

    @Override
    public String toString() {
        return prodName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    private String  code;
    private String  prodName;
    private String  unmodifiedProdName;
    private double  price;
    private int     quantity;
    private String  quantityType;
    private boolean deleted;
    private int     minQuantity;

}
