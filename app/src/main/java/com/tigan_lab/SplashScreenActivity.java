package com.tigan_lab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tigan_lab.customer.HomePageActivity;
import com.tigan_lab.easy_clean.Activity.MainActivity_Sp;
import com.tigan_lab.easy_clean.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.List;

import static java.lang.Thread.sleep;

public class SplashScreenActivity extends AppCompatActivity {
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    Session_management session_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setFinishOnTouchOutside(true);
        session_management = new Session_management(SplashScreenActivity.this);
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            Toast.makeText(getApplicationContext(), "Gps already enabled", Toast.LENGTH_SHORT).show();
            //getActivity().finish();
            redirectionScreen();
        }

        if (!hasGPSDevice(this)) {
            Toast.makeText(getApplicationContext(), "Gps not Supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            Toast.makeText(getApplicationContext(), "Gps not enabled", Toast.LENGTH_SHORT).show();


            enableLoc();
        }
    }

    private void redirectionScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session_management.loginType().equals("CUSTOMER")) {
                    Intent intent1 = new Intent(SplashScreenActivity.this, HomePageActivity.class);
                    startActivity(intent1);
                    finish();
                }
                if (session_management.loginType().equals("DRIVER")) {
                    Intent intent1 = new Intent(SplashScreenActivity.this, MainActivity_Sp.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Intent intent1 = new Intent(SplashScreenActivity.this, ChooserActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        redirectionScreen();

                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.e("connected", "connected");
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.e("suspended", "suspended");
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.e("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(7 * 1000);  //30 * 1000
            locationRequest.setFastestInterval(5 * 1000); //5 * 1000
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {

                    Log.e("result", "result");
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.e("RESOLUTION_REQUIRED", "");
                            try {
                                status.startResolutionForResult(SplashScreenActivity.this, REQUEST_LOCATION);

                            } catch (IntentSender.SendIntentException e) {
                                Log.e("error", "error");
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
