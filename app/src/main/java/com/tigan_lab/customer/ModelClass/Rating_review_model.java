package com.tigan_lab.customer.ModelClass;

public class Rating_review_model {
    String service_name;
    String review_head;
    String review_desc;
    String user_name;
    String rating;

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getReview_head() {
        return review_head;
    }

    public void setReview_head(String review_head) {
        this.review_head = review_head;
    }

    public String getReview_desc() {
        return review_desc;
    }

    public void setReview_desc(String review_desc) {
        this.review_desc = review_desc;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    String user_image;
}
