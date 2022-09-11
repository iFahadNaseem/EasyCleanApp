package com.tigan_lab.customer;

import static com.android.volley.VolleyLog.TAG;
import static com.tigan_lab.customer.Extra.Config.ADD_ORDER_URL;
import static com.tigan_lab.customer.Extra.Config.KEY_ID;
import static com.tigan_lab.customer.Extra.Config.cat_id_json_array;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import at.grabner.circleprogress.CircleProgressView;
public class Payment_order_activty extends AppCompatActivity {

    TextView title, number, number1, place_req;
    ImageView back_img, search;
    CardView cardview;
    LinearLayout linear, bottom_linear;
    String getuser_id, gettime, getdate, addressid;
    DatabaseHandler dbcart;

    SharedPreferences sharedPreferences;
    String addonamount;
    String getamount;
    RadioButton rb_cash, rb_debit, rb_credit, rb_netbanking, rb_paypal, rb_razorpay;
    String currency;
    Dialog progressDialog;
    TextView tv_price;
    Session_management session_management;
    int layout;
    CircleProgressView circleProgressView;
    JSONArray passArray;

    public static final int PAYPAL_REQUEST_CODE = 123;

    private static final PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_user);

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        place_req = findViewById(R.id.place_req);

        session_management = new Session_management(this);
        bottom_linear = findViewById(R.id.bottom_linear);
        rb_cash = findViewById(R.id.rb_cash);
        rb_credit = findViewById(R.id.rb_credit);
        rb_debit = findViewById(R.id.rb_debit);
        rb_netbanking = findViewById(R.id.rb_netbanking);
        rb_paypal = findViewById(R.id.rb_paypal);
        rb_razorpay = findViewById(R.id.rb_razorpay);

        sharedPreferences = getSharedPreferences("value", 0);

        addonamount = sharedPreferences.getString("addon", "");

        currency = getResources().getString(R.string.currency);
        dbcart = new DatabaseHandler(this);

        title = findViewById(R.id.title);

        tv_price = findViewById(R.id.price);

        getdate = getIntent().getStringExtra("date");
        gettime = getIntent().getStringExtra("time");

        addressid = getIntent().getStringExtra("address_id");

        Intent j = getIntent();
        layout = j.getIntExtra("layout", 0);
        title.setText("Select Payment Method");
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);
        if (layout == 1) {

            title.setText("Select Payment Method");


            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("85"));
        }
        if (layout == 2) {

            title.setText("Select Payment Method");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("85"));
        }
        if (layout == 3) {

            title.setText("Select Payment Method");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("85"));
        }
        if (layout == 4) {


            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("85"));
        }
        if (layout == 5) {

            title.setText("Select Payment Method");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("85"));
        }

        getamount = Config.Finalam;

        tv_price.setText(getResources().getString(R.string.currency) + Config.Finalam);





        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linear = findViewById(R.id.linear);
        getuser_id = session_management.getUserDetails().get(KEY_ID);
        ArrayList<HashMap<String, String>> items = dbcart.getCartAll();
        if (items.size() > 0) {
            passArray = new JSONArray();
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
        }

        bottom_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rb_cash.isChecked()) {

                    if (cat_id_json_array != null && cat_id_json_array.length()>0) {
                        for (int j = 0; j < cat_id_json_array.length(); j++) {

                            try {
                                JSONObject obj = cat_id_json_array.getJSONObject(j);
                                String id = obj.getString("addon_id");
                                String name = obj.getString("addon_name");
                                String price = obj.getString("addon_price");

                                if (!id.equalsIgnoreCase("") && id != null) {
                                    makeAddOrderRequest(getuser_id, passArray);
                                } else {
                                    if(id.equalsIgnoreCase("")&& id==null) {
                                    }else {
                                        makeAddOrderRequest1(getuser_id, passArray);

                                    }
                                }
                            } catch (JSONException e) {
                            }
                        }
                    }else {
                        makeAddOrderRequest1(getuser_id, passArray);//no addon
                    }

                } else if (rb_razorpay.isChecked()) {

                        Intent intent = new Intent(Payment_order_activty.this, Razorpay.class);
                        intent.putExtra("time", gettime);
                        intent.putExtra("date", getdate);
                        intent.putExtra("addressid", addressid);

                        startActivity(intent);
                } else if (rb_paypal.isChecked()) {

                    getPaypal();
                }

            }
        });
    }

    private void attemptOrder() {


        if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
        } else {
            if (cat_id_json_array != null) {
                for (int j = 0; j < cat_id_json_array.length(); j++) {

                    try {

                        JSONObject obj = cat_id_json_array.getJSONObject(j);

                        String id = obj.getString("addon_id");
                        String name = obj.getString("addon_name");
                        String price = obj.getString("addon_price");

                        Log.d("asdfgh", id);
                        if (!id.equalsIgnoreCase("") && id != null) {
                            makeAddOrderRequest(getuser_id, passArray);

                        } else {
                        }
                    } catch (JSONException e) {
                    }
                }
                makeAddOrderRequest1(getuser_id, passArray);
            }
        }
    }

    private void makeAddOrderRequest(String userid, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id",userid);
        params.put("price",Config.Finalam);
        params.put("address_id", addressid);
        params.put("time_slot", gettime);
        params.put("confirmed_on", getdate);
        params.put("payment_mode", "cash");
        params.put("price",getamount);

        params.put("demo_array", passArray.toString());
        params.put("demo_array1",cat_id_json_array.toString());
        progressDialog.show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String status=response.getString("status");
                    if(status.contains("1")){
                        dbcart.clearCart();
                        JSONObject jsonObject=response.getJSONObject("data");
                        String timeslot=jsonObject.getString("time_slot");
                        String bokdate=jsonObject.getString("confirmed_on");
                        String uniquecode=jsonObject.getString("unique_code");
                        String paymentmode=jsonObject.getString("payment_mode");
                        String bookingid=jsonObject.getString("booking_id");
                        //JSONArray jsonArray=response.getJSONArray("services");

                        place_req.setEnabled(false);
                        progressDialog.dismiss();

                        Intent intent=new Intent(Payment_order_activty.this,OrderSucessActivity.class);
                        intent.putExtra("timeslot",timeslot);
                        intent.putExtra("bokdate",bokdate);
                        intent.putExtra("uniquecode",uniquecode);
                        intent.putExtra("paymentmode",paymentmode);
                        intent.putExtra("bookingid",bookingid);
                        startActivity(intent);

                    }
                    else {
                        place_req.setEnabled(true);
                        progressDialog.dismiss();
                    }
                    String message=response.getString("message");
                    Toast.makeText(Payment_order_activty.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    place_req.setEnabled(true);

                    Toast.makeText(Payment_order_activty.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
        params.put("payment_mode", "cash");
        params.put("price",getamount);

        params.put("demo_array", passArray.toString());
        progressDialog.show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String status=response.getString("status");
                    if(status.contains("1")){
                        //progressDialog.show();
                        dbcart.clearCart();
                        JSONObject jsonObject=response.getJSONObject("data");
                        String timeslot=jsonObject.getString("time_slot");
                        String bokdate=jsonObject.getString("confirmed_on");
                        String uniquecode=jsonObject.getString("unique_code");
                        String paymentmode=jsonObject.getString("payment_mode");
                        String bookingid=jsonObject.getString("booking_id");
                        //JSONArray jsonArray=response.getJSONArray("services");

                        place_req.setEnabled(false);
                        progressDialog.dismiss();

                        Intent intent=new Intent(Payment_order_activty.this,OrderSucessActivity.class);
                        intent.putExtra("timeslot",timeslot);
                        intent.putExtra("bokdate",bokdate);
                        intent.putExtra("uniquecode",uniquecode);
                        intent.putExtra("paymentmode",paymentmode);
                        intent.putExtra("bookingid",bookingid);
                        startActivity(intent);

                    }
                    else {
                        place_req.setEnabled(true);
                        progressDialog.dismiss();
                    }
                    String message=response.getString("message");
                    Toast.makeText(Payment_order_activty.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    place_req.setEnabled(true);
                    Toast.makeText(Payment_order_activty.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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

    private void getPaypal() {
        try {
            PayPalPayment payment = new PayPalPayment(new BigDecimal(getamount), getString(R.string.payment_currency), "Coding Fee",
                    PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    //PAYPAL ACTIVITY RESULT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("payment", paymentDetails);
                       attemptOrder();

                    } catch (JSONException e) {
                        Log.e("payment", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("payment", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("payment", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

}

