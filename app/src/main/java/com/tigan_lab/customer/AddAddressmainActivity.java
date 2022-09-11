package com.tigan_lab.customer;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.easy_clean.R;

import at.grabner.circleprogress.CircleProgressView;

import static com.tigan_lab.customer.Extra.Config.AddAdress;
import static com.tigan_lab.customer.Extra.Config.EditAddress;
import static com.tigan_lab.customer.Extra.Config.KEY_ID;

public class AddAddressmainActivity extends AppCompatActivity {

    TextView title,number,number1;
    ImageView back_img,search;
    int layout;
    private static String TAG = AddAddressmainActivity.class.getSimpleName();

    EditText et_name,et_phone,et_area,et_pin,et_city,et_state,et_house,et_steet;
    AppCompatButton continue_sub;

    Session_management session_management;

    String active;
    String value,addressid;
    String cityname,userid,name,phone,area,pin,street,state,house,city;
    Dialog progressDialog;
    CircleProgressView circleProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        title = findViewById(R.id.title);
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        session_management=new Session_management(this);

        userid=session_management.getUserDetails().get(KEY_ID);
        cityname=session_management.getCityNAme();

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        et_name=findViewById(R.id.et_name);
        et_area=findViewById(R.id.et_area);
        et_city=findViewById(R.id.et_city);
        et_phone=findViewById(R.id.et_phone);
        et_pin=findViewById(R.id.et_pin);
        et_steet=findViewById(R.id.et_street);
        et_state=findViewById(R.id.et_state);
        et_house=findViewById(R.id.et_house);
        et_city.setText(cityname);
        title.setText("Add Address");
active=getIntent().getStringExtra("active");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setValue(Float.parseFloat("70"));



        continue_sub = findViewById(R.id.continue_sub);
        continue_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(et_name.getText().toString())) {
                    et_name.setError("Enter Name");
                }
                else if (TextUtils.isEmpty(et_phone.getText().toString())) {
                    et_phone.setError("Enter Phone");
                }
                else if (TextUtils.isEmpty(et_house.getText().toString())) {
                    et_house.setError("Enter House/Flat");
                }
                else if (TextUtils.isEmpty(et_steet.getText().toString())) {
                    et_steet.setError("Enter Street");
                }
                else if (TextUtils.isEmpty(et_area.getText().toString())) {
                    et_area.setError("Enter Locality");
                }
                else if (TextUtils.isEmpty(et_pin.getText().toString())) {
                    et_pin.setError("Enter Pin");
                }  else if (TextUtils.isEmpty(et_state.getText().toString())) {
                    et_state.setError("Enter state");
                }
                else {
                    name=et_name.getText().toString();
                    phone=et_phone.getText().toString();
                    city=et_city.getText().toString();
                    state=et_state.getText().toString();
                    street=et_steet.getText().toString();
                    pin=et_pin.getText().toString();
                    house=et_house.getText().toString();
                    area=et_area.getText().toString();

                    progressDialog.show();

                    makeAddAddressRequest(userid,pin,city,house,area,street,state,name,phone);
                    continue_sub.setEnabled(false);

                }
            }
        });

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void makeAddAddressRequest(String user_id, String pincode, String city,
                                       String house_no, String area, String street, String state, String name, String mobile) {

        String tag_json_obj = "json_add_address_req";
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("pin", pincode);
        params.put("city_name", city);
        params.put("house_no", house_no);
        params.put("area_name",area);
        params.put("street",street);
        params.put("state",state);
        params.put("user_name", name);
        params.put("user_phone", mobile);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                AddAdress, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                progressDialog.dismiss();
                try {
                    String status = response.getString("status");
                    String msg = response.getString("message");

                    if (status.contains("1")) {

                        continue_sub.setEnabled(false);
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                     else {
                        Toast.makeText(getApplicationContext(), msg+"", Toast.LENGTH_SHORT).show();
                        continue_sub.setEnabled(true);
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

    private void makeeditAddressRequest(String user_id, String pincode, String city,
                                        String house_no, String area, String street, String state, String name, String mobile, String addressid) {

        String tag_json_obj = "json_add_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("pin", pincode);
        params.put("address_id",addressid);
        params.put("city_name", city);
        params.put("house_no", house_no);
        params.put("area_name",area);
        params.put("street",street);
        params.put("state",state);
        params.put("user_name", name);
        params.put("user_phone", mobile);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                EditAddress, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");

                    if (status.contains("1")) {


                        continue_sub.setEnabled(false);
                        progressDialog.dismiss();

                        if (active.contains("0")){
                            Intent intent = new Intent(getApplicationContext(),SelectAddressActivity.class);

                            startActivity(intent);

                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(),SelectAddressActivity.class);

                            startActivity(intent);

                        }


                    }
                    else {
                        continue_sub.setEnabled(true);
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);}

}
