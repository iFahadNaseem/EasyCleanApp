package com.tigan_lab.customer.ModelClass;

import java.io.Serializable;

public class SubCateModelClass implements Serializable {
    String sub_category_id;
    String sub_category_name;
    String sub_category_img;

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getSub_category_img() {
        return sub_category_img;
    }

    public void setSub_category_img(String sub_category_img) {
        this.sub_category_img = sub_category_img;
    }
}
