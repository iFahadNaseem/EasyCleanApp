package com.tigan_lab.easy_clean.ModelClass;

import java.io.Serializable;

public class SearchModel implements Serializable {
    String id,pNAme;

    public SearchModel(String product_id, String product_name) {
        this.id=product_id;
        this.pNAme=product_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpNAme() {
        return pNAme;
    }

    public void setpNAme(String pNAme) {
        this.pNAme = pNAme;
    }
}
