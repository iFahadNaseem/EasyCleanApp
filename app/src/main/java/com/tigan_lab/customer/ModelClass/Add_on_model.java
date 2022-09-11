package com.tigan_lab.customer.ModelClass;

public class Add_on_model {

    String child_name;
    String add_on_price;
    String add_on_des;
    String des_id;

    public Add_on_model(String name) {
        this.add_on_des=name;
    }


    public String getDes_id() {
        return des_id;
    }

    public void setDes_id(String des_id) {
        this.des_id = des_id;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getAdd_on_price() {
        return add_on_price;
    }

    public void setAdd_on_price(String add_on_price) {
        this.add_on_price = add_on_price;
    }

    public String getAdd_on_des() {
        return add_on_des;
    }

    public void setAdd_on_des(String add_on_des) {
        this.add_on_des = add_on_des;
    }
}
