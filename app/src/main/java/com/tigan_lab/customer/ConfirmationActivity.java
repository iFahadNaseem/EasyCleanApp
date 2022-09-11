package com.tigan_lab.customer;

import static com.android.volley.VolleyLog.TAG;
import static com.tigan_lab.customer.Extra.Config.ADD_ORDER_URL;
import static com.tigan_lab.customer.Extra.Config.Coupocode;
import static com.tigan_lab.customer.Extra.Config.KEY_EMAIL;
import static com.tigan_lab.customer.Extra.Config.KEY_ID;
import static com.tigan_lab.customer.Extra.Config.KEY_MOBILE;
import static com.tigan_lab.customer.Extra.Config.KEY_NAME;
import static com.tigan_lab.customer.Extra.Config.cat_id_json_array;

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
import com.tigan_lab.easy_clean.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {
    private String amount;

    DatabaseHandler databaseHandler;

    Session_management session_management;
    String Used_Wallet_amount;

    String getdate,gettime,addressid,getuser_id,name,email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        databaseHandler=new DatabaseHandler(this);

        getdate=getIntent().getStringExtra("date");
        gettime=getIntent().getStringExtra("time");
        addressid=getIntent().getStringExtra("addressid");
        session_management=new Session_management(ConfirmationActivity.this);
        getuser_id=session_management.getUserDetails().get(KEY_ID);
        name=session_management.getUserDetails().get(KEY_NAME);
        email=session_management.getUserDetails().get(KEY_EMAIL);
        phone=session_management.getUserDetails().get(KEY_MOBILE);

        amount=databaseHandler.getTotalAmount();

        if (Config.Totalamount==""){
            amount=databaseHandler.getTotalAmount();
        }

        else {
            amount=databaseHandler.getTotalAmount();
        }

        attemptOrder();

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
            }else {
                makeAddOrderRequest(getuser_id,passArray);
            }

        }
    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void makeAddOrderRequest(String userid, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", userid);
        params.put("address_id", addressid);
        params.put("time_slot", gettime);
        params.put("confirmed_on", getdate);
        params.put("payment_mode", "card");
        params.put("coupon_id", Coupocode);
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

                        Intent intent=new Intent(ConfirmationActivity.this,HomePageActivity.class);

                        startActivity(intent);

                    }

                    String message=response.getString("message");
                    Toast.makeText(ConfirmationActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(ConfirmationActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);  }

}
