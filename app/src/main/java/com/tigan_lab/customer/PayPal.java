package com.tigan_lab.customer;

import static com.android.volley.VolleyLog.TAG;
import static com.tigan_lab.customer.Extra.Config.ADD_ORDER_URL;
import static com.tigan_lab.customer.Extra.Config.KEY_EMAIL;
import static com.tigan_lab.customer.Extra.Config.KEY_ID;
import static com.tigan_lab.customer.Extra.Config.KEY_MOBILE;
import static com.tigan_lab.customer.Extra.Config.KEY_NAME;
import static com.tigan_lab.customer.Extra.Config.cat_id_json_array;

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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Extra.Config;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.Extra.DatabaseHandler;
import com.tigan_lab.customer.Extra.PayPalConfig;
import com.tigan_lab.easy_clean.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayPal extends AppCompatActivity {
    private String amount;

    DatabaseHandler databaseHandler;

    Session_management session_management;
    String Used_Wallet_amount;

    String getdate,gettime,addressid,getuser_id,name,email,phone;
    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        databaseHandler=new DatabaseHandler(this);

        getdate=getIntent().getStringExtra("date");
        gettime=getIntent().getStringExtra("time");
        addressid=getIntent().getStringExtra("addressid");
        session_management=new Session_management(PayPal.this);
        getuser_id=session_management.getUserDetails().get(KEY_ID);
        name=session_management.getUserDetails().get(KEY_NAME);
        email=session_management.getUserDetails().get(KEY_EMAIL);
        phone=session_management.getUserDetails().get(KEY_MOBILE);

        amount=databaseHandler.getTotalAmount();
        if (Config.Finalam==""){
            amount=databaseHandler.getTotalAmount();
        }
        else {
            amount=databaseHandler.getTotalAmount();
        }


        getPayment();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void getPayment() {
        //Getting the amount from editText

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", "Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);
                        attemptOrder();

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", PaymentActivity.RESULT_EXTRAS_INVALID + "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
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
                if(cat_id_json_array!=null) {
                    for (int j = 0; j < cat_id_json_array.length(); j++) {

                        try {

                            JSONObject obj = cat_id_json_array.getJSONObject(j);

                            String id = obj.getString("addon_id");
                            String name = obj.getString("addon_name");
                            String price = obj.getString("addon_price");

                            Log.d("asdfgh", id);
                            if (!id.equalsIgnoreCase("")&& id!=null) {
                                makeAddOrderRequest(getuser_id, passArray);

                            } else {
                                makeAddOrderRequest1(getuser_id, passArray);
                            }
                        } catch (JSONException e) {
                        }
                    }
                    //makeAddOrderRequest1(getuser_id, passArray);
                }
            }
        }
    }
    private void makeAddOrderRequest(String userid, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", userid);
        params.put("address_id", addressid);
        params.put("time_slot", gettime);
        params.put("confirmed_on", getdate);
        params.put("payment_mode", "card");
        params.put("price", Config.Finalam);
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
                        databaseHandler.clearCart();

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
                    String message=response.getString("message");
                    Toast.makeText(PayPal.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(PayPal.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }
    private void makeAddOrderRequest1(String userid, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", userid);
        params.put("address_id", addressid);
        params.put("time_slot", gettime);
        params.put("confirmed_on", getdate);
        params.put("payment_mode", "card");
        params.put("price", Config.Finalam);
        params.put("demo_array", passArray.toString());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String status=response.getString("status");
                    if(status.contains("1")){
                        databaseHandler.clearCart();

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
                    String message=response.getString("message");
                    Toast.makeText(PayPal.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(PayPal.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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