package com.tigan_lab.easy_clean.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Window;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tigan_lab.easy_clean.Activity.ForgotPassActivity;
import com.tigan_lab.easy_clean.Activity.MainActivity_Sp;
import com.tigan_lab.easy_clean.Constants.Config;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LoginFragment extends Fragment {


    EditText etPhone,ePass;
    TextView forgOtPass,btnLogin,showpass;
    Dialog progressDialog;
    String androiId,user_id;
    CheckBox mCbShowPwd;
    Session_management sessionManagement;
   public static TextView txtSignUp;
    String token;
    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressDialog = new Dialog(requireActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            token = "";
                            return;
                        }
                        token = Objects.requireNonNull(task.getResult()).getToken();
                    }
                });
        androiId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        etPhone=view.findViewById(R.id.etPhone);
        ePass=view.findViewById(R.id.etPass);
        forgOtPass=view.findViewById(R.id.forgotPass);
        txtSignUp=view.findViewById(R.id.tittie_signin);
        btnLogin=view.findViewById(R.id.button_Signup);
        mCbShowPwd=view.findViewById(R.id.cbShowPwd);
        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if (value)
                {
                    // Show Password
                    ePass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    // Hide Password
                    ePass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupFragment tm = new SignupFragment();
                FragmentManager manager21 = getFragmentManager();
                FragmentTransaction transaction21 = manager21.beginTransaction();
                transaction21.replace(R.id.contentPanell, tm);
                transaction21.commit();
            }
        });

        forgOtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ForgotPassActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Mobile Number required!", Toast.LENGTH_SHORT).show();
                } else if (etPhone.getText().toString().trim().length() < 9) {
                    Toast.makeText(getActivity(), "Valid Mobile Number required!", Toast.LENGTH_SHORT).show();
                } else if (ePass.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Password required!", Toast.LENGTH_SHORT).show();
                } else if (!isOnline()) {
                    Toast.makeText(getActivity(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    loginUrl();
                }
            }
        });

        return view;
    }


    private void loginUrl() {

        if (token != null && !token.equalsIgnoreCase("")) {
            hitService();
        } else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                token = task.getResult().getToken();
                                loginUrl();
                            } else {
                                token = "";
                            }
                        }
                    });
        }


    }

    private void hitService() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Login",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){
                        JSONObject resultObj = jsonObject.getJSONObject("data");

                        user_id = resultObj.getString("partner_id");
                        String user_fullname = resultObj.getString("partner_name");
                        String user_email = resultObj.getString("partner_email");
                        String user_phone = resultObj.getString("partner_phone");
                        String partner_profesion = resultObj.getString("partner_profesion");
                        String category_id = resultObj.getString("category_id");
                        String lat = resultObj.getString("lat");
                        String lng = resultObj.getString("lng");
                        String user_image = resultObj.getString("partner_image");
                        String delivery_range = resultObj.getString("range");
                        Session_management sessionManagement = new Session_management(getActivity());
                        sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, user_image,ePass.getText().toString(),lat,lng,delivery_range,partner_profesion);

                        CoinsCollectUrl();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getActivity(), MainActivity_Sp.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
                param.put("partner_phone",etPhone.getText().toString());
                param.put("partner_password",ePass.getText().toString());
                param.put("device_id",token);

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void CoinsCollectUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.COINS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Login",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){
                        JSONObject resultObj = jsonObject.getJSONObject("data");
                        String coins = resultObj.getString("coins");

                        Session_management sessionManagement = new Session_management(getActivity());
                        sessionManagement.Coins(coins);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("partner_id",user_id);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}

