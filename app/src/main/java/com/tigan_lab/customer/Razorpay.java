package com.tigan_lab.customer;

import static com.android.volley.VolleyLog.TAG;
import static com.tigan_lab.customer.Extra.Config.ADD_ORDER_URL;
import static com.tigan_lab.customer.Extra.Config.KEY_EMAIL;
import static com.tigan_lab.customer.Extra.Config.KEY_ID;
import static com.tigan_lab.customer.Extra.Config.KEY_MOBILE;
import static com.tigan_lab.customer.Extra.Config.KEY_NAME;
import static com.tigan_lab.customer.Extra.Config.cat_id_json_array;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Extra.Config;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.Extra.DatabaseHandler;
import com.tigan_lab.easy_clean.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Razorpay extends AppCompatActivity implements PaymentResultListener {

    Session_management session_management;
    String name,email,phone;
    String amount;
    String getuser_id,addressid,getdate,gettime,getamount;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);

        databaseHandler=new DatabaseHandler(this);
        getamount = Config.Finalam;
        getdate=getIntent().getStringExtra("date");
        gettime=getIntent().getStringExtra("time");
        addressid=getIntent().getStringExtra("addressid");
        session_management=new Session_management(Razorpay.this);
        getuser_id=session_management.getUserDetails().get(KEY_ID);
        name=session_management.getUserDetails().get(KEY_NAME);
        email=session_management.getUserDetails().get(KEY_EMAIL);
        phone=session_management.getUserDetails().get(KEY_MOBILE);

        if (Config.Finalam==""){
            amount=databaseHandler.getTotalAmount();
        }
        else {
            amount=databaseHandler.getTotalAmount();
        }
        startPayment(name,getamount,email,phone);

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
    public void onPaymentSuccess(String razorpayPaymentID) {


        attemptOrder();
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("tag", "Exception in onPaymentSuccess", e);

        }


    }

    @Override
    public void onPaymentError(int i, String s) {


    }
    private void attemptOrder() {


        ArrayList<HashMap<String, String>> items = databaseHandler.getCartAll();
        if (items.size() > 0) {
            JSONArray passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("service_id", map.get("service_id"));
                    jObjP.put("service_qty", map.get("qty"));
                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            if(!isOnline()){
                Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
            }
            else {
                if (cat_id_json_array != null) {
                    for (int j = 0; j < cat_id_json_array.length(); j++) {

                        try {

                            JSONObject obj = cat_id_json_array.getJSONObject(j);

                            String id = obj.getString("addon_id");
                            String name = obj.getString("addon_name");
                            String price = obj.getString("addon_price");

                            if (!id.equalsIgnoreCase("") && id != null) {
                                makeAddOrderRequest(getuser_id, passArray);
                            } else {
                                if (id.equalsIgnoreCase("") && id == null) {
                                } else {
                                    makeAddOrderRequest1(getuser_id, passArray);

                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                } else {
                    makeAddOrderRequest1(getuser_id, passArray);//no addon
                }

            }
        }
    }

    private void makeAddOrderRequest(String userid, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", userid);
        params.put("price",Config.Finalam);
        params.put("address_id", addressid);
        params.put("time_slot", gettime);
        params.put("confirmed_on", getdate);
        params.put("payment_mode", "card");
        params.put("demo_array", passArray.toString());
        params.put("demo_array1",cat_id_json_array.toString());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String status=response.getString("status");
                    if(status.contains("1")){

                        JSONObject jsonObject=response.getJSONObject("data");
                        String timeslot=jsonObject.getString("time_slot");
                        String bokdate=jsonObject.getString("confirmed_on");
                        String uniquecode=jsonObject.getString("unique_code");
                        String paymentmode=jsonObject.getString("payment_mode");
                        String bookingid=jsonObject.getString("booking_id");
                        //JSONArray jsonArray=response.getJSONArray("services");0

                        Intent intent=new Intent(getApplicationContext(),OrderSucessActivity.class);
                        intent.putExtra("timeslot",timeslot);
                        intent.putExtra("bokdate",bokdate);
                        intent.putExtra("uniquecode",uniquecode);
                        intent.putExtra("paymentmode",paymentmode);
                        intent.putExtra("bookingid",bookingid);
                        startActivity(intent);

                    }
                    String message=response.getString("message");
                    Toast.makeText(Razorpay.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                makeAddOrderRequest1(getuser_id, passArray);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Razorpay.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(jsonObjReq);
    }
    private void makeAddOrderRequest1(String userid, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id",userid);
        params.put("price",Config.Finalam);
        params.put("address_id", addressid);
        params.put("time_slot", gettime);
        params.put("confirmed_on", getdate);
        params.put("payment_mode", "card");
        params.put("demo_array", passArray.toString());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String status=response.getString("status");
                    if(status.contains("1")){

                        JSONObject jsonObject=response.getJSONObject("data");
                        String timeslot=jsonObject.getString("time_slot");
                        String bokdate=jsonObject.getString("confirmed_on");
                        String uniquecode=jsonObject.getString("unique_code");
                        String paymentmode=jsonObject.getString("payment_mode");
                        String bookingid=jsonObject.getString("booking_id");
                        //JSONArray jsonArray=response.getJSONArray("services");

                        Intent intent=new Intent(getApplicationContext(),OrderSucessActivity.class);
                        intent.putExtra("timeslot",timeslot);
                        intent.putExtra("bokdate",bokdate);
                        intent.putExtra("uniquecode",uniquecode);
                        intent.putExtra("paymentmode",paymentmode);
                        intent.putExtra("bookingid",bookingid);
                        startActivity(intent);

                    }
                    else {

                    }
                    String message=response.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(jsonObjReq);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}