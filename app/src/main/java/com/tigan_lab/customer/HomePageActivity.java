package com.tigan_lab.customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tigan_lab.customer.Extra.FetchAddressTask;
import com.tigan_lab.customer.Extra.GPSTracker;
import com.tigan_lab.customer.fragment.BookingFragment;
import com.tigan_lab.customer.fragment.CategoryFragment;
import com.tigan_lab.customer.fragment.HomeFragment;
import com.tigan_lab.customer.fragment.ProfileFragment;

import static com.tigan_lab.customer.Extra.Config.ADDRESS;
import static com.tigan_lab.customer.Extra.Config.CITY;
import static com.tigan_lab.customer.Extra.Config.COUNTRY;
import static com.tigan_lab.customer.Extra.Config.KEY_PINCODE;
import static com.tigan_lab.customer.Extra.Config.LAT;
import static com.tigan_lab.customer.Extra.Config.LONG;
import static com.tigan_lab.customer.Extra.Config.MyPrefreance;
import static com.tigan_lab.customer.Extra.Config.STATE;

public class HomePageActivity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted {


    FrameLayout frameLayout;
    private View locationLayout, toolbar_sp;

    LinearLayout linear, city_linear;
    TextView title, location,skip;
    List<Address> addresses = new ArrayList<>();
    String latitude, longitude, address, city, state, country, postalCode;
    LocationManager locationManager;
    Session_management sessionManagement;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    String lat,lng;
    private Fragment fragment;
    MeowBottomNavigation navigation;

    TextView cancel,tLogin,tSignUP;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet,ll_loc;

    SharedPreferences placePref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        sessionManagement=new Session_management(this);

        cancel=findViewById(R.id.cancel);
        tLogin=findViewById(R.id.Login);
        tSignUP=findViewById(R.id.SignUP);

        toolbar_sp = findViewById(R.id.toolbarLayout_sp);
        locationLayout = findViewById(R.id.toolbarLayout);
        linear = findViewById(R.id.linear);
        city_linear = findViewById(R.id.city_linear);
        title = findViewById(R.id.title);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottom_sheet.setVisibility(View.VISIBLE);

        placePref = getSharedPreferences("getlatlng",MODE_PRIVATE);
        lat=placePref.getString("getlat","");
        lng=placePref.getString("getlng","");
        location = findViewById(R.id.location);

        navigation = findViewById(R.id.menu_bottom);

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setPeekHeight(300);

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            sheetBehavior.setPeekHeight(0);

            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        sheetBehavior.setPeekHeight(0);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        sheetBehavior.setPeekHeight(300);

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        sheetBehavior.setPeekHeight(0);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        if(sessionManagement.loginType().equals("CUSTOMER")){
            bottom_sheet.setVisibility(View.GONE);
        }
        else {
            bottom_sheet.setVisibility(View.VISIBLE);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sheetBehavior.setPeekHeight(0);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        tLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(in);
            }
        });
        tSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(in);
            }
        });

        if (savedInstanceState == null) {
            loadHome();
        }


        navigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_homew));
        navigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_different_squares));
        navigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_history));
        navigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_user));
        navigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {

                case 1:
                    loadHome();
                    break;

                case 2:
                    toolbar_sp.setVisibility(View.VISIBLE);
                    city_linear.setVisibility(View.GONE);
                    locationLayout.setVisibility(View.GONE);
                    title.setText("Category");
                    fragment = new CategoryFragment();
                    break;

                case 3:
                    if (sessionManagement.loginType().equals("CUSTOMER")) {

                        toolbar_sp.setVisibility(View.VISIBLE);
                        city_linear.setVisibility(View.GONE);
                        locationLayout.setVisibility(View.GONE);
                        title.setText("Booking");
                        fragment = new BookingFragment();
                        break;
                    } else {
                        Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
                        startActivity(i);
                    }

                case 4:
                    if (sessionManagement.loginType().equals("CUSTOMER")) {

                        toolbar_sp.setVisibility(View.VISIBLE);
                        city_linear.setVisibility(View.GONE);
                        locationLayout.setVisibility(View.GONE);
                        title.setText("Profile");
                        fragment = new ProfileFragment();
                        break;
                    } else {
                        Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
                loadFragment(fragment);
                return null;
        });
        navigation.show(1, true);

        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, LocationActivity.class);
                startActivityForResult(intent,345);
            }
        });


        GPSTracker mGPS = new GPSTracker(getApplicationContext());
        if (mGPS.canGetLocation) {
            mGPS.getLocation();

            latitude = String.valueOf(mGPS.getLatitude());
            longitude = String.valueOf(mGPS.getLongitude());


            sessionManagement.LatLng(latitude,longitude);

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(mGPS.getLatitude(), mGPS.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
                location.setText(locality);
            }
            if (address != null) {
                address = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                String title = address + "-" + city + "-" + state;
                Log.d("addresss", title + country + "-" + postalCode);

                SharedPreferences.Editor editor = getSharedPreferences(MyPrefreance, MODE_PRIVATE).edit();
                editor.putString(ADDRESS, address);
                editor.putString(CITY, city);
                editor.putString(STATE, state);
                editor.putString(KEY_PINCODE, postalCode);
                editor.putString(COUNTRY, country);
                editor.putString(LAT, latitude);
                editor.putString(LONG, longitude);
                editor.apply();
                editor.commit();
            }
        }

        //location.setText(address+","+city+","+state);

        getLocation();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    new FetchAddressTask(HomePageActivity.this, HomePageActivity.this).execute(location);
                }
            }
        });

        if(isLocationEnabled()) {

            if (latitude.isEmpty()) {

                if (!lat.isEmpty()) {
                    sessionManagement.LatLng(lat, lng);
                    Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses != null && addresses.size() > 0) {
                        String locality = addresses.get(0).getAdminArea();
                        location.setText(locality);
                    }
                } else {
                    location.setText("Set Location");

                }
            } else {
                Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    String locality = addresses.get(0).getAddressLine(0);
                    location.setText(locality);
                }


                if (savedInstanceState == null) {
                    loadHome();
                }
            }
        } 
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Warning!");
            alert.setCancelable(false);
            alert.setMessage("Please turn on your location");
            alert.setPositiveButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog=alert.create();
            alertDialog.show();
        }

        loadHome();
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    private void loadHome() {
        navigation.show(1, true);
        toolbar_sp.setVisibility(View.GONE);
        city_linear.setVisibility(View.VISIBLE);
        locationLayout.setVisibility(View.VISIBLE);
        title.setText("Home");
        Fragment fragment = new HomeFragment();
        loadFragment(fragment);
    }

    @Override
    public void onTaskCompleted(String result) {

    }


    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);

                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG", "Granted");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    return;
                }
                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            new FetchAddressTask(HomePageActivity.this, HomePageActivity.this)
                                    .execute(location);
                        }
                    }
                });


            } else {

                Toast.makeText(HomePageActivity.this, "Location permission is necessary", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d("TAG", "getLocation: permissions granted");
        }

        loadHome();
    }


    public void onResume() {
        super.onResume();
        placePref = getSharedPreferences("getlatlng",MODE_PRIVATE);

        lat=placePref.getString("getlat","");
        lng=placePref.getString("getlng","");
        sessionManagement.LatLng(lat, lng);
        if (!lat.isEmpty()) {
            sessionManagement.LatLng(lat, lng);
            Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
                location.setText(locality);
            }

            loadHome();
        }
    }
    public void onPause() {
        super.onPause();
        placePref = getSharedPreferences("getlatlng",MODE_PRIVATE);

        lat=placePref.getString("getlat","");
        lng=placePref.getString("getlng","");
        sessionManagement.LatLng(lat, lng);
        if (!lat.isEmpty()) {
            sessionManagement.LatLng(lat, lng);
            Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
                location.setText(locality);
            }

            //loadHome();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 345) {

            placePref = getSharedPreferences("getlatlng",MODE_PRIVATE);

            lat=placePref.getString("getlat","");
            lng=placePref.getString("getlng","");
            sessionManagement.LatLng(lat, lng);
            Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
                location.setText(locality);
            }

            loadHome();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager)
                getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(HomePageActivity.this )
                    .setMessage( "Please enable location" )
                    .setPositiveButton( "Settings" , new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                }
                            })
                    .setNegativeButton( "Cancel" , null )
                    .show();
            return false;
        }
        else {
            return true;
        }
    }
}
