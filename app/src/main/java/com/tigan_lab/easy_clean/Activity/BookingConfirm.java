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
import android.widget.EditText;
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
import com.tigan_lab.easy_clean.Constants.CustomVolleyJsonRequest;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tigan_lab.easy_clean.Constants.Config.StartBooking;
import static com.tigan_lab.easy_clean.Constants.Config.markedAsComplete;

public class BookingConfirm extends AppCompatActivity {

    Dialog progressDialog;
    String bookingID,partnerName,partnerNo,partnerID;
    TextView name,Phone,location,Submit;
    EditText Codee;
    LinearLayout Confirm;
    Session_management sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);
        iniht();
    }

    private void iniht() {
        sessionManagement=new Session_management(this);
        partnerID=sessionManagement.userId();
        partnerName=sessionManagement.userName();
        partnerNo=sessionManagement.userNo();
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        bookingID=getIntent().getStringExtra("bId");
        Submit=findViewById(R.id.btnStartJOb);
        name=findViewById(R.id.partnerName);
        Phone=findViewById(R.id.tPhone);
        Codee=findViewById(R.id.etCode);
        Confirm=findViewById(R.id.conofirm);
        name.setText(partnerName);
        Phone.setText(partnerNo);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBooking(bookingID,partnerID);
              /*  if (Codee.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter code to continue!", Toast.LENGTH_SHORT).show();

                }else if(!isOnline()){
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    startBooking(bookingID,partnerID);
                }*/
            }
        });
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(!isOnline()){
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    MArkedCmplte(bookingID,partnerID);
                }
            }
        });

    }

    private void MArkedCmplte(String bookingID, String partnerID) {
        progressDialog.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("partner_id", partnerID);
        params.put("booking_id", bookingID);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                markedAsComplete, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("3")) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(getApplicationContext(), MainActivity_Sp.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                }

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
                    //Toast.makeText(getContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }

    private void startBooking(final String bookingID,final String partnerID) {
        progressDialog.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("partner_id", partnerID);
        params.put("booking_id", bookingID);
        params.put("code",Codee.getText().toString().trim());

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                StartBooking, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("3")) {
                        //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        Confirm.setVisibility(View.VISIBLE);
                    }
                    else {
                        //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        Confirm.setVisibility(View.VISIBLE);
                     /*   Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        Confirm.setVisibility(View.GONE);*/
                }
                }

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
                    //Toast.makeText(getContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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