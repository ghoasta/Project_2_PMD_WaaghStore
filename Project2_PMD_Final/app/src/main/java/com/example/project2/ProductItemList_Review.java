package com.example.project2;

public class ProductItemList_Review {

    public String review_rating;
    public String review_content;

    public ProductItemList_Review(String review_rating, String review_content) {
        this.review_rating = review_rating;
        this.review_content = review_content;
    }

    public String getReview_rating() {
        return review_rating;
    }

    public String getReview_content() {
        return review_content;
    }
}
