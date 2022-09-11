package com.tigan_lab.easy_clean.Activity;

import static com.android.volley.VolleyLog.TAG;

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
import java.util.HashMap;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;
public class RechargePaymentMethod extends AppCompatActivity {

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
    String coins,price,plan_id;

    public static final int PAYPAL_REQUEST_CODE = 123;

    private static final PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payemnt_provider);

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        place_req = findViewById(R.id.place_req);

        session_management = new Session_management(this);
        bottom_linear = findViewById(R.id.bottom_linear);
        rb_paypal = findViewById(R.id.rb_paypal);
        rb_razorpay = findViewById(R.id.rb_razorpay);

        currency = getResources().getString(R.string.currency);
        title = findViewById(R.id.title);
        tv_price = findViewById(R.id.price);

        title.setText("Select Payment Method");
        coins = getIntent().getStringExtra("Coins");
        price = getIntent().getStringExtra("price");
        plan_id = getIntent().getStringExtra("planId");
        getuser_id = session_management.getUserDetails().get(com.tigan_lab.easy_clean.Constants.Config.KEY_ID);

        getamount = price;

        tv_price.setText(getResources().getString(R.string.currency) + getamount);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bottom_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (rb_razorpay.isChecked()) {
                    Intent intent = new Intent(RechargePaymentMethod.this, Razorpay.class);
                    intent.putExtra("planId", plan_id);
                    intent.putExtra("Coins", coins);
                    intent.putExtra("price", price);
                    startActivity(intent);
                    finish();

                } else if (rb_paypal.isChecked()) {

                    getPaypal();
                }

            }
        });
    }




    private void makeAddOrderRequest(String plan_id,String paymentStatus,String coins,String price,String getuser_id) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("plan_id", plan_id);
        params.put("status", paymentStatus);
        params.put("coins", coins);
        params.put("price", price);
        params.put("partner_id", getuser_id);

        com.tigan_lab.easy_clean.Constants.CustomVolleyJsonRequest jsonObjReq = new com.tigan_lab.easy_clean.Constants.CustomVolleyJsonRequest(Request.Method.POST,
                com.tigan_lab.easy_clean.Constants.Config.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String status=response.getString("status");
                    String message=response.getString("message");
                    if(status.equalsIgnoreCase("1")){
                        Intent intent=new Intent(RechargePaymentMethod.this,OrderSuccessfull.class); //HomePageActivity
                        startActivity(intent);
                        finish();
                    }
                    else {

                        finish();
                        Toast.makeText(RechargePaymentMethod.this, message, Toast.LENGTH_SHORT).show();
                    }


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

                    Toast.makeText(RechargePaymentMethod.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
                        makeAddOrderRequest(plan_id,"success",coins,price,getuser_id);

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

