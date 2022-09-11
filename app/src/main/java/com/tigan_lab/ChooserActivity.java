package com.tigan_lab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tigan_lab.customer.HomePageActivity;
import com.tigan_lab.customer.LoginActivity;
import com.tigan_lab.easy_clean.Activity.LoginSignupActivity_Sp;
import com.tigan_lab.easy_clean.Activity.MainActivity_Sp;
import com.tigan_lab.easy_clean.R;

public class ChooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        init();

        enableLocation();

        Session_management sessionManagement = new Session_management(this);

        if (sessionManagement.loginType().equals("DRIVER")) {
            Intent intent1 = new Intent(ChooserActivity.this, MainActivity_Sp.class);
            startActivity(intent1);
            finish();
        }
        if (sessionManagement.loginType().equals("CUSTOMER")) {
            Intent intent1 = new Intent(ChooserActivity.this, HomePageActivity.class);
            startActivity(intent1);
            finish();
        }


    }

    private void init() {
        Button buttonCustomer = findViewById(R.id.btn_customer);
        Button buttonDriver = findViewById(R.id.btn_driver);


        if (buttonCustomer != null) {
            buttonCustomer.setOnClickListener(v -> {

                Session_management sessionManagement = new Session_management(this);

                if (sessionManagement.loginType().equals("CUSTOMER")) {
                    Intent intent1 = new Intent(ChooserActivity.this, HomePageActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Intent intent1 = new Intent(ChooserActivity.this, LoginActivity.class);
                    startActivity(intent1);
                    finish();
                }
            });
        }


        if (buttonDriver != null) {
            buttonDriver.setOnClickListener(v -> {

                Session_management sessionManagement = new Session_management(this);

                if (sessionManagement.loginType().equals("DRIVER")) {
                    Intent intent1 = new Intent(ChooserActivity.this, MainActivity_Sp.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Intent intent2 = new Intent(ChooserActivity.this, LoginSignupActivity_Sp.class);
                    startActivity(intent2);
                    finish();
                }
            });
        }
    }

    private void enableLocation() {
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
            new AlertDialog.Builder(ChooserActivity.this )
                    .setMessage( "Please enable location" )
                    .setPositiveButton( "Settings" , new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                }
                            })
                    .setNegativeButton( "Cancel" , null )
                    .show() ;
        }
    }
}
