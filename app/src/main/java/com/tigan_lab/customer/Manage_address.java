package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.ShowAddress;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.daimajia.swipe.util.Attributes;
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Adapter.Manage_address_adapter;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.Delivery_address_model;
import com.tigan_lab.easy_clean.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;

public class Manage_address extends AppCompatActivity {
    TextView title,AddAddress,number1;
    ImageView back_img,search;
    int layout;
    String userid;
    private static String TAG = SelectAddressActivity.class.getSimpleName();
    private RecyclerView rv_address;
    String location_id = "";
    String address = "";

    private Manage_address_adapter adapter;
    private List<Delivery_address_model> delivery_address_modelList = new ArrayList<>();

    Session_management session_management;
    CircleProgressView circleProgressView;
    AppCompatButton bottom_linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);

        rv_address = (RecyclerView) findViewById(R.id.rv_deli_address);
        rv_address.setLayoutManager(new LinearLayoutManager(Manage_address.this));
        bottom_linear=findViewById(R.id.submit);
        session_management=new Session_management(this);
        search=findViewById(R.id.search);
        title=findViewById(R.id.title);
        title.setText("Manage Address");
        search.setVisibility(View.GONE);
        userid=session_management.userId();




        AddAddress = findViewById(R.id.AddAddress);

        AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manage_address.this,AddAddressmainActivity.class);
                intent.putExtra("value","2");
                intent.putExtra("active","0");
                startActivity(intent);

            }
        });

        if(isOnline()) {
            makeGetAddressRequest(userid);
        }else {
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private void makeGetAddressRequest(String user_id) {

        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ShowAddress, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    if (status.contains("1")) {

                        delivery_address_modelList.clear();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Delivery_address_model>>() {
                        }.getType();

                        delivery_address_modelList = gson.fromJson(response.getString("data"), listType);

                        //RecyclerView.Adapter adapter1 = new Delivery_get_address_adapter(delivery_address_modelList);
                        adapter = new Manage_address_adapter(delivery_address_modelList);
                        ((Manage_address_adapter) adapter).setMode(Attributes.Mode.Single);
                        rv_address.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (delivery_address_modelList.isEmpty()) {

                            if (Manage_address.this != null) {
                                //Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (Manage_address.this != null) {
                        Toast.makeText(Manage_address.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);}

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}