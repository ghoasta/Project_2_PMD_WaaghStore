package com.example.project2;

public class ProductListItem_Transaction {

    public String transaction_id;
    public String transaction_time;
    public String transaction_value;

    public ProductListItem_Transaction(String transaction_id, String transaction_time, String transaction_value) {
        this.transaction_id = transaction_id;
        this.transaction_time = transaction_time;
        this.transaction_value = transaction_value;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public String getTransaction_value() {
        return transaction_value;
    }
}
