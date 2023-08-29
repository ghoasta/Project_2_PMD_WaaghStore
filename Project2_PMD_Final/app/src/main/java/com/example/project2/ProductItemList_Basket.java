package com.example.project2;

public class ProductItemList_Basket {

    public String basket_name;
    public String basket_id;
    public String basket_quantity;
    public String basket_value;

    public ProductItemList_Basket(String basket_name, String basket_id, String basket_quantity, String basket_value) {
        this.basket_name = basket_name;
        this.basket_id = basket_id;
        this.basket_quantity = basket_quantity;
        this.basket_value = basket_value;
    }

    public String getBasket_name() {
        return basket_name;
    }

    public String getBasket_id() {
        return basket_id;
    }

    public String getBasket_quantity() {
        return basket_quantity;
    }

    public String getBasket_value() {
        return basket_value;
    }
}
