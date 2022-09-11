package com.tigan_lab.easy_clean.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.easy_clean.Constants.CustomVolleyJsonArrayRequest;
import com.tigan_lab.easy_clean.Constants.CustomVolleyJsonRequest;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tigan_lab.easy_clean.Constants.Config.BuyBooking;
import static com.tigan_lab.easy_clean.Constants.Config.serviceDetails;

public class ServiceDetails extends AppCompatActivity {

    Dialog progressDialog;
    String bookingID,unique_code,partnerID,user_name;
    LinearLayout btnRespnd;
    TextView clientName,clientPhone,serbicedes,serviceNAme,location,dateTime,clientLoc,txtCoins;
    Session_management sessionManagement;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);
        inint();
    }

    private void inint() {
        sessionManagement=new Session_management(this);
        partnerID=sessionManagement.userId();
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        bookingID=getIntent().getStringExtra("booking_id");
        back=findViewById(R.id.back);
        btnRespnd=findViewById(R.id.btnRespnd);
        clientName=findViewById(R.id.clientName);
        clientPhone=findViewById(R.id.clientPhone);
        serviceNAme=findViewById(R.id.serbiceName);
        location=findViewById(R.id.locationName);
        dateTime=findViewById(R.id.DateTjime);
        clientLoc=findViewById(R.id.clientLoc);
        txtCoins=findViewById(R.id.txtCoins);
        serbicedes=findViewById(R.id.serbicedes);
        if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
        } else {
            servvIceDetailsURl(bookingID);
        }

        btnRespnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    buyTicket(bookingID,partnerID);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    private void buyTicket(final String bookingID,final String partnerID) {
        progressDialog.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("booking_id",bookingID);
        params.put("partner_id",partnerID);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BuyBooking, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("BuyBooking", response.toString());
                progressDialog.dismiss();
                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONObject obj = response.getJSONObject("data");

                        String booking_id = obj.getString("booking_id");
                        String coins= obj.getString("coins");
                        String time_slot = obj.getString("time_slot");
                        String confirmed_on = obj.getString("confirmed_on");
                        String user_address = obj.getString("user_address");
                        String user_phone = obj.getString("user_phone");
                        unique_code = obj.getString("unique_code");

                        Intent intent=new Intent(getApplicationContext(),StartJob.class);
                        intent.putExtra("booking_id",booking_id);
                        intent.putExtra("user_name",user_name);
                        intent.putExtra("user_phone",user_phone);
                        intent.putExtra("user_address",user_address);
                        startActivity(intent);
                    }}
                catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }

    private void servvIceDetailsURl(final String bookingID) {

        progressDialog.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("booking_id",bookingID);

           CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                   serviceDetails, params, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("asdf", response.toString());

                        try {
                            JSONObject object = response.getJSONObject(0);
                            user_name = object.getString("user_name");
                            String coins= object.getString("coins");
                            String time_slot = object.getString("time_slot");
                            String confirmed_on = object.getString("confirmed_on");

                            JSONArray jsonArray = object.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject obj = jsonArray.getJSONObject(i);

                                String serviceName = obj.getString("service_name");
                                String serviceDescp = obj.getString("service_description");


                                serviceNAme.setText(serviceName);
                                serbicedes.setText(serviceDescp);
                                dateTime.setText(confirmed_on+", "+time_slot);
                                clientName.setText(user_name);
                                txtCoins.setText("("+coins+")"+" Credits");
                            }}
                catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}