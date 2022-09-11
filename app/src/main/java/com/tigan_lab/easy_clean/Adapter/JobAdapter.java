package com.tigan_lab.easy_clean.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tigan_lab.easy_clean.Fragments.HistoryFragment;
import com.tigan_lab.easy_clean.Fragments.OngoingFragment;
import com.tigan_lab.easy_clean.Fragments.WithdrawlFragment;


public class JobAdapter extends FragmentPagerAdapter {
    private int numsoftabs;


    public JobAdapter(FragmentManager fm, int numsoftabs) {
        super(fm);
        this.numsoftabs = numsoftabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0 :
                return new OngoingFragment();
            case 1:
                return new HistoryFragment();
            case 2:
                return new WithdrawlFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numsoftabs;
    }


}
