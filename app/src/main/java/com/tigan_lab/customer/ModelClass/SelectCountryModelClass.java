package com.tigan_lab.customer.ModelClass;



public class SelectCountryModelClass {

    Integer image;
    String country_name,country_code;

    public SelectCountryModelClass(Integer image, String country_name, String country_code) {
        this.image = image;
        this.country_name = country_name;
        this.country_code = country_code;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
