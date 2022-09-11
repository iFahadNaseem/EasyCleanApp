package com.tigan_lab.easy_clean.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.easy_clean.Constants.Config;
import com.tigan_lab.easy_clean.Constants.CustomVolleyJsonRequest;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tigan_lab.easy_clean.Constants.Config.bankShow;

public class BankEdit extends AppCompatActivity {
    Dialog progressDialog;
    Session_management sessionManagement;
    ImageView back;
    EditText Regstrdname,IFSC,AccNo,confrmAccNo;
    LinearLayout SAVE,Updtae;
    String partnerID,layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_edit);
        iniit();
    }

    private void iniit() {
        sessionManagement=new Session_management(this);
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        back=findViewById(R.id.back);
        partnerID=sessionManagement.userId();
        layout=getIntent().getStringExtra("id");
        Regstrdname=findViewById(R.id.Regstrdname);
        AccNo=findViewById(R.id.accNo);
        confrmAccNo=findViewById(R.id.confrmAccNO);
        IFSC=findViewById(R.id.IFSC);
        SAVE=findViewById(R.id.SAVE);
        Updtae=findViewById(R.id.Updtae);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Regstrdname.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter your name registered in bank!", Toast.LENGTH_SHORT).show();
                } else if(AccNo.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter your account number!", Toast.LENGTH_SHORT).show();
                } else if(confrmAccNo.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Re-enter your account number!", Toast.LENGTH_SHORT).show();
                } else if(!confrmAccNo.getText().toString().trim().matches(AccNo.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please re-confirm your account number!", Toast.LENGTH_SHORT).show();
                } else  if(IFSC.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter IFSC code number!", Toast.LENGTH_SHORT).show();
                } else  if (!isOnline()) {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                } else  {
                    bankAddUrl();
                }
            }
        });

        if(layout!=null){
            bankListURl(partnerID);
            Updtae.setVisibility(View.VISIBLE);
            SAVE.setVisibility(View.GONE);
            Updtae.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Regstrdname.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter your name registered in bank!", Toast.LENGTH_SHORT).show();
                    } else if(AccNo.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter your account number!", Toast.LENGTH_SHORT).show();
                    } else if(confrmAccNo.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Re-enter your account number!", Toast.LENGTH_SHORT).show();
                    } else if(!confrmAccNo.getText().toString().trim().matches(AccNo.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please re-confirm your account number!", Toast.LENGTH_SHORT).show();
                    } else  if(IFSC.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter IFSC code number!", Toast.LENGTH_SHORT).show();
                    } else  if (!isOnline()) {
                        Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                    } else  {
                        updateBankUrl();
                    }
                }
            });
        }
    }

    private void updateBankUrl() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.bankUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("bankUpdate",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){

                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
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
                HashMap<String,String> param = new HashMap<>();
                param.put("partner_id",partnerID);
                param.put("holder_name",Regstrdname.getText().toString());
                param.put("acc_no",AccNo.getText().toString());
                param.put("cnf_no",confrmAccNo.getText().toString());
                param.put("ifsc_code",IFSC.getText().toString());

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void bankAddUrl() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.bankAadd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("bankAadd",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){

                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        Updtae.setVisibility(View.GONE);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        Updtae.setVisibility(View.GONE);
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
                HashMap<String,String> param = new HashMap<>();
                param.put("partner_id",partnerID);
                param.put("holder_name",Regstrdname.getText().toString());
                param.put("acc_no",AccNo.getText().toString());
                param.put("cnf_no",confrmAccNo.getText().toString());
                param.put("ifsc_code",IFSC.getText().toString());

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void bankListURl(final String partnerID) {
        progressDialog.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("partner_id", partnerID);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                bankShow, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("bankShow", response.toString());
                progressDialog.dismiss();
                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONArray jsonArray = response.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject obj=jsonArray.getJSONObject(i);

                            String bank_id = obj.getString("bank_id");
                            String holder_name = obj.getString("holder_name");
                            String acc_no = obj.getString("acc_no");
                            String ifsc_code = obj.getString("ifsc_code");

                            AccNo.setText(acc_no);
                            IFSC.setText(ifsc_code);
                            Regstrdname.setText(holder_name);

                        }
                    }else {

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