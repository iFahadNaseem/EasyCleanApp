package com.tigan_lab.easy_clean.Activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.easy_clean.Constants.Config;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassActivity extends AppCompatActivity {
    ImageView back_img;
    Dialog progressDialog;
    Button button;
    EditText etMob;
    Dialog slideDialog;
    LinearLayout sppiner;
    ImageView img;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_sp);
        init();
    }

    private void init() {
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        back_img = findViewById(R.id.back_img);
        button = findViewById(R.id.button);

        etMob= findViewById(R.id.etMob);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMob.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter mobile no.", Toast.LENGTH_SHORT).show(); }

                else if(!isOnline()){
                    Toast.makeText(getApplicationContext(),"Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                }else {
                    hitUrl();
                }
            }
        });

    }

    private void hitUrl() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.forgotPss, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("mob sned",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("1")){
                        JSONObject resultObj = jsonObject.getJSONObject("data");

                        Toast.makeText(getApplicationContext(), msg+"", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(),OTPActivity.class);
                        intent.putExtra("MobNo",etMob.getText().toString());
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("partner_phone",etMob.getText().toString());

                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}