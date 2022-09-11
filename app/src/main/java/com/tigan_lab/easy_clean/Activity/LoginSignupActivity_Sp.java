package com.tigan_lab.easy_clean.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.tigan_lab.easy_clean.Adapter.PageAdapter;
import com.tigan_lab.easy_clean.R;

import static com.tigan_lab.easy_clean.Fragments.LoginFragment.txtSignUp;

public class LoginSignupActivity_Sp extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem tab1, tab2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup_sp);
        init();
    }

    private void init() {
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.pager_product);
        tab1 = findViewById(R.id.login);
        tab2 = findViewById(R.id.signup);
        txtSignUp=findViewById(R.id.tittie_signin);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
//
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }
}