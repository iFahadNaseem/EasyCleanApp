package com.tigan_lab.customer;

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Adapter.List_order_adapter;
import com.tigan_lab.customer.Extra.Config;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.Extra.DatabaseHandler;

import com.tigan_lab.customer.ModelClass.List_order_model;
import com.tigan_lab.easy_clean.R;

import at.grabner.circleprogress.CircleProgressView;

import static com.tigan_lab.customer.Extra.Config.ADD_ORDER_URL;
import static com.tigan_lab.customer.Extra.Config.KEY_ID;
import static com.tigan_lab.customer.Extra.Config.cat_id_json_array;
import static com.android.volley.VolleyLog.TAG;

public class OrderSucessActivity extends AppCompatActivity {

    TextView title,number,number1,tv_otp,tv_mode;
    ImageView back_img,search;
    int layout;

    TextView tv_time,tv_subtotal,tv_paid;
    String time,date;
    String jsonaaray;
    String amount,addressid;


    String getuser_id;
    String mode;
    DatabaseHandler dbHandler;
    Session_management session_management;


    List<List_order_model> list=new ArrayList<>();
    List_order_adapter list_order_adapter;
    RecyclerView recyclerView;
    String otp;
    TextView tv_bookingid;
    CardView place_service;
    CircleProgressView circleProgressView;
    String timeslot,bokdate,paymentmode,bookingid,uniquecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sucess);
        dbHandler = new DatabaseHandler(OrderSucessActivity.this);
        session_management=new Session_management(OrderSucessActivity.this);

        place_service=findViewById(R.id.place_service);
        tv_otp=findViewById(R.id.otp);

        tv_mode=findViewById(R.id.mode);

        tv_bookingid=findViewById(R.id.bookingid);
        tv_subtotal=findViewById(R.id.subtotal);
        tv_paid=findViewById(R.id.paid_amount);
        time=getIntent().getStringExtra("time");
        date=getIntent().getStringExtra("date");
        amount=getIntent().getStringExtra("amount");
        mode=getIntent().getStringExtra("mode");
        addressid=getIntent().getStringExtra("addressid");

        timeslot=getIntent().getStringExtra("timeslot");
        bokdate=getIntent().getStringExtra("bokdate");
        uniquecode=getIntent().getStringExtra("uniquecode");
        bookingid=getIntent().getStringExtra("bookingid");
        paymentmode=getIntent().getStringExtra("paymentmode");

        ArrayList<HashMap<String, String>> map = dbHandler.getCartAll();

        title = findViewById(R.id.title);
        title.setText("Order Successful..");
        tv_time=findViewById(R.id.time);


        if (Config.Finalam==""){
            tv_subtotal.setText(getResources().getString(R.string.currency)+dbHandler.getTotalAmount());

            tv_paid.setText(getResources().getString(R.string.currency)+dbHandler.getTotalAmount());
        }

        else {
            tv_subtotal.setText(getResources().getString(R.string.currency)+Config.Finalam);

            tv_paid.setText(getResources().getString(R.string.currency)+Config.Finalam);
        }
        recyclerView=findViewById(R.id.rc_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderSucessActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        jsonaaray=getIntent().getStringExtra("array");


        place_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderSucessActivity.this,HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);
        back_img = findViewById(R.id.back_img);
        back_img.setVisibility(View.GONE);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(bookingid!=null&& !bookingid.equalsIgnoreCase("")){
            dbHandler.clearCart();
        tv_time.setText(timeslot+" "+bokdate);
        tv_otp.setText(uniquecode);
        tv_mode.setText(paymentmode);
        tv_bookingid.setText("Booking ID #"+bookingid);
        }
        else {

        }

    }
    private void attemptOrder() {


        ArrayList<HashMap<String, String>> items = dbHandler.getCartAll();
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

            getuser_id = session_management.getUserDetails().get(KEY_ID);
            if(!isOnline()){
                Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
            }else {
                if(cat_id_json_array!=null) {
                    for (int j = 0; j < cat_id_json_array.length(); j++) {

                        try {

                            JSONObject obj = cat_id_json_array.getJSONObject(j);

                            String id = obj.getString("addon_id");
                            String name = obj.getString("addon_name");
                            String price = obj.getString("addon_price");

                            Log.d("asdfgh", id);
                            if (!id.equalsIgnoreCase("") && id!=null) {
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

    private void makeAddOrderRequest1(String getuser_id, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id",getuser_id);
        params.put("address_id", addressid);
        params.put("time_slot", time);
        params.put("confirmed_on", date);
        params.put("payment_mode", mode);
        params.put("coupon_id", Config.Coupocode);
        params.put("demo_array", passArray.toString());
        Log.d("demo_array",passArray.toString());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String status=response.getString("status");
                    if(status.contains("1")){
                        dbHandler.clearCart();

                        JSONObject jsonObject=response.getJSONObject("data");
                        String timeslot=jsonObject.getString("time_slot");
                        String bokdate=jsonObject.getString("confirmed_on");
                        String uniquecode=jsonObject.getString("unique_code");
                        String paymentmode=jsonObject.getString("payment_mode");
                        String bookingid=jsonObject.getString("booking_id");
                        JSONArray jsonArray=response.getJSONArray("services");


                        tv_time.setText(timeslot+" "+bokdate);

                        tv_otp.setText(uniquecode);
                        tv_mode.setText(paymentmode);
                        tv_bookingid.setText("Booking ID #"+bookingid);

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String servicename=jsonObject1.getString("service_name");
                            String qty=jsonObject1.getString("qty");
                            String price=jsonObject1.getString("price");
                            List_order_model list_order_model=new List_order_model();
                            list_order_model.setQty(qty);
                            list_order_model.setService_name(servicename);
                            list_order_model.setService_price(price);
                            list.add(list_order_model);
                        }

                        list_order_adapter=new List_order_adapter(OrderSucessActivity.this,list);
                        recyclerView.setAdapter(list_order_adapter);
                        list_order_adapter.notifyDataSetChanged();


                    }
                    else {
                    }
                    String message=response.getString("message");
                    Toast.makeText(OrderSucessActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(OrderSucessActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

    private void makeAddOrderRequest(String userid, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id",userid);
        params.put("address_id", addressid);
        params.put("time_slot", time);
        params.put("confirmed_on", date);
        params.put("payment_mode", mode);
        params.put("coupon_id", Config.Coupocode);
        params.put("demo_array", passArray.toString());
        params.put("demo_array1",cat_id_json_array.toString());
        Log.d("demo_array1",cat_id_json_array.toString());

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String status=response.getString("status");
                    if(status.contains("1")){
                        dbHandler.clearCart();
                        for (int i = 0; i < cat_id_json_array.length(); i++) {
                            try {
                                JSONObject obj = cat_id_json_array.getJSONObject(i);

                                String id = obj.getString("addon_id");
                                 cat_id_json_array.remove(Integer.parseInt(id));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        JSONObject jsonObject=response.getJSONObject("data");
                        String timeslot=jsonObject.getString("time_slot");
                        String bokdate=jsonObject.getString("confirmed_on");
                        String uniquecode=jsonObject.getString("unique_code");
                        String paymentmode=jsonObject.getString("payment_mode");
                        String bookingid=jsonObject.getString("booking_id");
                        JSONArray jsonArray=response.getJSONArray("services");


                        tv_time.setText(timeslot+" "+bokdate);

                        tv_otp.setText(uniquecode);
                        tv_mode.setText(paymentmode);
                        tv_bookingid.setText("Booking ID #"+bookingid);

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String servicename=jsonObject1.getString("service_name");
                            String qty=jsonObject1.getString("qty");
                            String price=jsonObject1.getString("price");
                            List_order_model list_order_model=new List_order_model();
                            list_order_model.setQty(qty);
                            list_order_model.setService_name(servicename);
                            list_order_model.setService_price(price);
                            list.add(list_order_model);
                        }

                        list_order_adapter=new List_order_adapter(OrderSucessActivity.this,list);
                        recyclerView.setAdapter(list_order_adapter);
                        list_order_adapter.notifyDataSetChanged();


                    }
                    else {
                    }
                    String message=response.getString("message");
                    Toast.makeText(OrderSucessActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                   Toast.makeText(OrderSucessActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq); }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(OrderSucessActivity.this,HomePageActivity.class);
        startActivity(intent);
    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
