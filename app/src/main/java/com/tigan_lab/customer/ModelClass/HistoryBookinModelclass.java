package com.tigan_lab.customer.ModelClass;

public class HistoryBookinModelclass {

    String confirmed_on;
    String time_slot;
    String price;
    String booking_date;
    String services;
    String booking_id;



    public String getConfirmed_on() {
        return confirmed_on;
    }

    public void setConfirmed_on(String confirmed_on) {
        this.confirmed_on = confirmed_on;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    String booking_status;
}
