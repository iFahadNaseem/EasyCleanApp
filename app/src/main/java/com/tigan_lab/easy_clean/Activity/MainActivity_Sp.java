package com.tigan_lab.easy_clean.Activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.squareup.picasso.Picasso;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.Fragments.ContactFragment;
import com.tigan_lab.easy_clean.Fragments.CreditBalanceFragment;
import com.tigan_lab.easy_clean.Fragments.GSTdetailsFragment;
import com.tigan_lab.easy_clean.Fragments.HomeeFragment;
import com.tigan_lab.easy_clean.Fragments.JobHistoryFragment;
import com.tigan_lab.easy_clean.Fragments.ProfileFragment;
import com.tigan_lab.easy_clean.Fragments.TermsFragment;
import com.tigan_lab.easy_clean.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.tigan_lab.easy_clean.Constants.Config.IMAGE_URL;


public class MainActivity_Sp extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private Menu nav_menu;
    private Session_management sessionManagement;
    ImageView slider;
    TextView txtHead;
    String bookingID,partnerName,partnerNo,partnerID,partner_image;
    ImageView imageView;
    TextView name,number;
    public static MeowBottomNavigation navigation;
    public static DrawerLayout drawer;
    BottomNavigationView.OnNavigationItemSelectedListener navItemSelectedListener;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManagement=new Session_management(this);
        partnerName=sessionManagement.userName();
        partnerNo=sessionManagement.userNo();
        partner_image=sessionManagement.userNo();

        Toolbar toolbar = findViewById(R.id.toolbar);
        navigation = findViewById(R.id.menu_bottom);
        setSupportActionBar(toolbar);
        slider=findViewById(R.id.slidr);
        txtHead=findViewById(R.id.txtHead);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        imageView=header.findViewById(R.id.imageView);

        name=header.findViewById(R.id.name);
        number=header.findViewById(R.id.No);
        if(partnerNo!=null){
        name.setText(partnerName);
        number.setText(partnerNo);
             Picasso.with(getApplicationContext()).load(IMAGE_URL+partner_image).error(R.drawable.logo).into(imageView);

        }


        txtHead.setText("New Leads");


        drawer = findViewById(R.id.drawer_layout);
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity_Sp.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        Menu m = navigationView.getMenu();

        for (
                int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                }
            }

        }
        View headerView = navigationView.getHeaderView(0);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();


        initComponent();
        loadFragment(new HomeeFragment());
    }
    private void loadFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentPanell, fragment)
                .commitAllowingStateLoss();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.nav_homed) {
            fm = new HomeeFragment();
            navigation.show(1, true);

        } else if (id == R.id.nav_earnings) {
            fm  = new CreditBalanceFragment();
            navigation.show(2, true);

        } else if (id == R.id.nav_booking) {
            fm  = new JobHistoryFragment();
            navigation.show(3, true);

        } else if (id == R.id.nav_profile) {
            fm  = new ProfileFragment();
            navigation.show(4, true);

        }
        else if (id == R.id.nav_credit) {
            fm  = new CreditBalanceFragment();

        }
        else if (id == R.id.nav_jobhistory) {
            fm  = new JobHistoryFragment();

        }
        else if (id == R.id.nav_gst) {
            fm  = new GSTdetailsFragment();

        }
        else if (id == R.id.nav_terms) {
            fm  = new TermsFragment();

        } else if (id == R.id.nav_contct) {
            fm = new ContactFragment();

        } else if (id == R.id.nav_log_out) {
            sessionManagement.logoutSession();
            finish();
        }
        if (fm != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanell, fm).addToBackStack(null).commit();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }






    @Override
    public boolean onSupportNavigateUp() {

      return false;
    }
    private void initComponent() {

        navigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_homew));
        navigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_monetization_new));
        navigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_clipboard_new));
        navigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_name));
        navigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {

                case 1:
                    fragment = new HomeeFragment();
                    break;

                case 2:
                    fragment = new CreditBalanceFragment();
                    break;

                case 3:
                    fragment = new JobHistoryFragment();
                    break;

                case 4:
                    fragment = new ProfileFragment();
                    break;

            }
            loadFragment(fragment);
            return null;
        });
        navigation.show(1, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}