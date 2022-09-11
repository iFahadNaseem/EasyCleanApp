package com.tigan_lab.easy_clean.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.tigan_lab.easy_clean.Constants.CustomVolleyJsonRequest;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import static com.android.volley.VolleyLog.TAG;
import static com.tigan_lab.easy_clean.Constants.Config.ADD_ORDER_URL;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_EMAIL;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_ID;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_MOBILE;
import static com.tigan_lab.easy_clean.Constants.Config.KEY_NAME;

public class Razorpay extends AppCompatActivity implements PaymentResultListener {

    Session_management session_management;
    String name,email,phone;
    String amount;
    String getuser_id,coins,price,plan_id;

    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay_sp);
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        session_management=new Session_management(Razorpay.this);
        getuser_id=session_management.getUserDetails().get(KEY_ID);
        name=session_management.getUserDetails().get(KEY_NAME);
        email=session_management.getUserDetails().get(KEY_EMAIL);
        phone=session_management.getUserDetails().get(KEY_MOBILE);
        coins=getIntent().getStringExtra("Coins");
        price=getIntent().getStringExtra("price");
        plan_id=getIntent().getStringExtra("planId");
        startPayment(name,price,email,phone);

    }
    public void startPayment(String name, String amount, String email, String phone) {


        final Activity activity = this;
        final Checkout co = new Checkout();
        try {

            JSONObject options = new JSONObject();

            options.put("name", name);
            options.put("description", "Shopping Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", getString(R.string.payment_currency));
            options.put("amount", Double.parseDouble(amount) * 100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phone);

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    @SuppressLint("JavascriptInterface")
    @SuppressWarnings("unused")

    @Override
    public void onPaymentSuccess(String s) {
        makeAddOrderRequest(plan_id,"success",coins,price,getuser_id);
    }

    @Override
    public void onPaymentError(int i, String s) {
        progressDialog.dismiss();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        finish();

    }

    private void makeAddOrderRequest(String plan_id,String paymentStatus,String coins,String price,String getuser_id) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("plan_id", plan_id);
        params.put("status", paymentStatus);
        params.put("coins", coins);
        params.put("price", price);
        params.put("partner_id", getuser_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String status=response.getString("status");
                    String message=response.getString("message");
                    if(status.equalsIgnoreCase("1")){
                        Intent intent=new Intent(Razorpay.this,OrderSuccessfull.class); //HomePageActivity
                        startActivity(intent);
                        finish();
                    }
                    else {

                        finish();
                        Toast.makeText(Razorpay.this, message, Toast.LENGTH_SHORT).show();
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