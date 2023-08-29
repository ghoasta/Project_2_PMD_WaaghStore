package com.example.project2;

public class ProductsModel {
    String productName;
    String productType;
    String productPrice;
    String productDesc;
    int image;

    public ProductsModel(String productName, String productType, String productPrice, int image,String productDesc) {
        this.productName = productName;
        this.productType = productType;
        this.productPrice = productPrice;
        this.image = image;
        this.productDesc = productDesc;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public int getImage() {
        return image;
    }

    public String getProductDesc() {
        return productDesc;
    }
}
