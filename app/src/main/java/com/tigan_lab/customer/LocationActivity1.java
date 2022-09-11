package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.GET_CITY_BOUNDRIES;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Adapter.PlaceAutocompleteAdapter;
import com.tigan_lab.customer.Extra.SavedPlaceListener;
import com.tigan_lab.customer.ModelClass.SavedAddress;
import com.tigan_lab.easy_clean.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LocationActivity1 extends AppCompatActivity implements View.OnClickListener, SavedPlaceListener {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 1000;
    private static final long FASTEST_INTERVAL = 1000 * 5000;
    Button btnFusedLocation;
    EditText tvLocation;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    TextView txt;
    String mLastUpdateTime;
    LinearLayout detect;
    ImageView back_img;
    private RecyclerView mRecyclerView;
    LinearLayoutManager llm;
    PlaceAutocompleteAdapter mAdapter;
    List<SavedAddress> mSavedAddressList;
    private static LatLngBounds BOUNDS_PAKISTAN;


    String lat, lng;
    AppCompatButton saved;

    String latNorth, lngNorth, latSouth, lngSouth;

    Button close_places;
    ImageView mClear;

    SharedPreferences placePref;
    SharedPreferences.Editor editor;
    String city_name;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Session_management sessionManagement;
    CardView cardview;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        placePref = getSharedPreferences("getlatlng", MODE_PRIVATE);


        city_name = "Noida";
        editor = placePref.edit();



        setContentView(R.layout.activity_location);

        sessionManagement=new Session_management(this);
        txt = findViewById(R.id.txt);
        tvLocation = findViewById(R.id.et_location);
        cardview = findViewById(R.id.cardview);
        saved = findViewById(R.id.saved);
        detect = findViewById(R.id.detect);

        lat = placePref.getString("getlat", "");

        lng = placePref.getString("getlng", "");


        if (lat.contains("")) {

            tvLocation.setHint("Search Location");
        } else {
            Geocoder gcd = new Geocoder(LocationActivity1.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
                tvLocation.setText(locality);


            }

        }


        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });
        getLatlngBounds();

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity1.this, HomePageActivity.class);
                intent.putExtra("getlat","23.6363");
                intent.putExtra("getlng", "85.5124");

                editor.putString("getlat", "23.6363");
                editor.putString("getlng", "85.5124");
                editor.putString("value", "true");
                sessionManagement.LatLng(lat,lng);
                editor.commit();

                setResult(345, intent);



                finish();

            }
        });
    }





    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }





    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
             lat = String.valueOf(mCurrentLocation.getLatitude());
             lng = String.valueOf(mCurrentLocation.getLongitude());
            editor.putString("getlat",lat);
            editor.putString("getlng",lng);
            editor.putString("value","true");
            editor.commit();

            Geocoder gcd = new Geocoder(LocationActivity1.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng) , 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
               String locality = addresses.get(0).getAddressLine(0);

               tvLocation.setText(locality);
            }


        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void getLatlngBounds(){

        String finalCityName = city_name.replaceAll(" ","%20");
        RequestQueue requestQueue = Volley.newRequestQueue(LocationActivity1.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_CITY_BOUNDRIES+finalCityName,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i =0;i<jsonArray.length();i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        JSONObject geometry = jsonObject1.getJSONObject("geometry");
                        JSONObject bounds = geometry.getJSONObject("bounds");
                        JSONObject northeast = bounds.getJSONObject("northeast");
                        JSONObject southwest = bounds.getJSONObject("southwest");
                        try {
                            latNorth = northeast.optString("lat").trim();
                            lngNorth = northeast.optString("lng").trim();

                            latSouth = southwest.optString("lat").trim();
                            lngSouth = southwest.optString("lng").trim();


                            BOUNDS_PAKISTAN = new LatLngBounds(
                                    new LatLng(Double.parseDouble(latSouth), Double.parseDouble(lngSouth)),
                                    new LatLng(Double.parseDouble(latNorth), Double.parseDouble(lngNorth)));
                        } catch (Exception e){
                            e.getCause();
                        }

                    }

                    initViews();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);

    }



    private void initViews(){

        mRecyclerView = (RecyclerView)findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);

        mClear = (ImageView)findViewById(R.id.clear);
        back_img=findViewById(R.id.back_img);

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == mClear){
                    tvLocation.setText("");
                    if(mAdapter!=null){
                        mAdapter.clearList();
                    }

                }
            }
        });
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tvLocation.getText().length()>=1){
                    txt.setVisibility(View.VISIBLE);
                    searchUrl(tvLocation.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLocation.setText("ramgarh cantt jharkhand");
                txt.setVisibility(View.GONE);
            }
        });
    }

    private void searchUrl(String text) {

       String ss="ramgarh cantt jharkhand";
        txt.setText(ss);
    }

    @Override
    public void onSavedPlaceClick(ArrayList<SavedAddress> mResultList, int position) {
        if(mResultList!=null){
            try {
                Intent data = new Intent();
                data.putExtra("lat", "23.6363");
                data.putExtra("lng", "85.5124");
                setResult(LocationActivity1.RESULT_OK, data);
                finish();

            }
            catch (Exception e){

            }

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View v) {

    }
}

