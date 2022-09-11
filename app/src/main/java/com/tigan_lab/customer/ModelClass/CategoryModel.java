package com.tigan_lab.customer.ModelClass;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    public String id;
    public String pName;
    public String pImage;

    public String getPchildcatId() {
        return pchildcatId;
    }

    public void setPchildcatId(String pchildcatId) {
        this.pchildcatId = pchildcatId;
    }

    public String pchildcatId;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
}
