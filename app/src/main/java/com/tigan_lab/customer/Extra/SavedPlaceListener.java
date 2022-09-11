package com.tigan_lab.customer.Extra;

import java.util.ArrayList;

import com.tigan_lab.customer.ModelClass.SavedAddress;



public interface SavedPlaceListener {
    public void onSavedPlaceClick(ArrayList<SavedAddress> mResultList, int position);
}
