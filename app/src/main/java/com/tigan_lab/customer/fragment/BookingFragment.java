package com.tigan_lab.customer.fragment;

import android.graphics.Typeface;
import android.os.Bundle;

import com.tigan_lab.easy_clean.R;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tigan_lab.customer.Adapter.CategoryPagerAdapterBooking;

public class BookingFragment extends Fragment {

    private View view;


    private TabLayout tabLayout;
    private int tab_value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_booking, container, false);


        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);


        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText("On Going"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));




        Typeface mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final ViewPager viewPager1 = (ViewPager) view.findViewById(R.id.pager);
        CategoryPagerAdapterBooking adapter = new CategoryPagerAdapterBooking(getChildFragmentManager(), 2);
        viewPager1.setAdapter(adapter);
        viewPager1.setOffscreenPageLimit(1);
        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        if (tab_value >= 0) {
            tabLayout.setScrollPosition(tab_value, 0f, true);
            viewPager1.setCurrentItem(tab_value);
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager1.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



        return  view;
    }





}
