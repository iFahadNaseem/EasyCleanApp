package com.tigan_lab.customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tigan_lab.easy_clean.R;

public class SlideTwoFragment extends Fragment {

    private  View view;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.slide_two, container, false);






        return  view;
    }


    }

