package com.tigan_lab.easy_clean.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tigan_lab.easy_clean.Activity.AfterGstPage;
import com.tigan_lab.easy_clean.Adapter.CustomAdapter;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.ModelClass.SearchModel;
import com.tigan_lab.easy_clean.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.Telephony.Carriers.PASSWORD;
import static com.tigan_lab.easy_clean.Constants.Config.CategyList;

import static com.tigan_lab.easy_clean.Constants.Config.ChildCategyList;
import static com.tigan_lab.easy_clean.Constants.Config.CityListUrl;
import static com.tigan_lab.easy_clean.Constants.Config.DEVICE_ID;
import static com.tigan_lab.easy_clean.Constants.Config.EMAIL;
import static com.tigan_lab.easy_clean.Constants.Config.MAINCATELIST;
import static com.tigan_lab.easy_clean.Constants.Config.MOBILE;
import static com.tigan_lab.easy_clean.Constants.Config.MyPrefreance;
import static com.tigan_lab.easy_clean.Constants.Config.NAME;
import static com.tigan_lab.easy_clean.Constants.Config.SignUP;
import static com.tigan_lab.easy_clean.Constants.Config.USER_ID;
import static com.tigan_lab.easy_clean.Constants.Config.USER_STATUS;


public class SignupFragment extends Fragment {


    EditText name,email,phone,password,etProfression,etAddress,etrange;
    TextView txtogin,btnSignUp;
    private AppCompatSpinner city, categry_spinner,mainCategorySpinner,childcatespinner;
    final List<SearchModel> citylist = new ArrayList<>();
    final List<SearchModel> catelist = new ArrayList<>();
    final List<SearchModel> Maincatelist = new ArrayList<>();
    final List<SearchModel> childcatelist = new ArrayList<>();
    String city_id,cityName,category_id,categoryName,emailPattern,maincategory_id,maincategoryName,child_category_id,child_category_name;
    Dialog progressDialog;
    String androiId;
    String token;
    public SignupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);
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

        emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        etrange=view.findViewById(R.id.etrange);
        name=view.findViewById(R.id.etName);
        email=view.findViewById(R.id.etemailid);
        phone=view.findViewById(R.id.etmobileno);
        password=view.findViewById(R.id.etpasswrd);
        etProfression=view.findViewById(R.id.etProfression);
        etAddress=view.findViewById(R.id.etAddress);

        btnSignUp=view.findViewById(R.id.button_Signup);
        city=view.findViewById(R.id.city);
        categry_spinner=view.findViewById(R.id.categry_spinner);
        mainCategorySpinner=view.findViewById(R.id.maincate);
        childcatespinner=view.findViewById(R.id.childcatespinner);

        if (!isOnline()) {
            Toast.makeText(getActivity(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
        } else {
            cityUrl();
            maincateUrl();
        }
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Full name required!", Toast.LENGTH_SHORT).show();
                } else if (phone.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Mobile Number required!", Toast.LENGTH_SHORT).show();
                } else if (phone.getText().toString().trim().length() < 9) {
                    Toast.makeText(getActivity(), "Valid Mobile Number required!", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Email Id required!", Toast.LENGTH_SHORT).show();
                }  else if(!email.getText().toString().trim().matches(emailPattern)){
                    Toast.makeText(getActivity(),"Valid Email id required!",Toast.LENGTH_SHORT).show(); }
                else if (etrange.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Delivery Range required!", Toast.LENGTH_SHORT).show(); }
                else if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Password required!", Toast.LENGTH_SHORT).show();
                } else if (!isOnline()) {
                    Toast.makeText(getActivity(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    SignupUrl();
                }
            }
        });




        return view;
    }

    private void SignupUrl() {
        if (token != null && !token.equalsIgnoreCase("")) {
            hitService(token,city_id,category_id,maincategory_id,child_category_id);
        } else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                token = task.getResult().getToken();
                                SignupUrl();
                            } else {
                                token = "";
                            }
                        }
                    });
        }

    }

    private void hitService(final String token,final String city_id,final String category_id,final String maincategory_id,final String child_category_id) {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SignUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SignUP",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){

                        JSONObject resultObj = jsonObject.getJSONObject("data");
                        String user_id = resultObj.getString("partner_id");
                        String mobile = resultObj.getString("partner_phone");
                        String partner_profesion = resultObj.getString("partner_profesion");
                        String email = resultObj.getString("partner_email");
                        String name = resultObj.getString("partner_name");
                        String user_status = resultObj.getString("status");
                        String adminID = resultObj.getString("device_id");
                        String user_image= resultObj.getString("partner_image");
                        String lat = resultObj.getString("lat");
                        String lng = resultObj.getString("lng");
                        String delivery_range = resultObj.getString("range");
                        Session_management sessionManagement = new Session_management(getActivity());
                        sessionManagement.createLoginSession(user_id, email, name, mobile, user_image,password.getText().toString(),lat,lng,delivery_range,partner_profesion);

                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MyPrefreance,MODE_PRIVATE).edit();
                        editor.putString(USER_ID,user_id);
                        editor.putString(MOBILE,mobile);
                        editor.putString(EMAIL,email);
                        editor.putString(NAME,name);
                        editor.putString(USER_STATUS,user_status);
                        editor.putString(DEVICE_ID,adminID);
                        editor.putString(PASSWORD,password.getText().toString());
                        editor.apply();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getActivity(), AfterGstPage.class);
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

                param.put("device_id",token);
                param.put("city_id",city_id);
                param.put("category_id",maincategory_id);
                param.put("sub_cat_id",category_id);
                param.put("sub_child_cat_id",child_category_id);
                param.put("partner_name",name.getText().toString());
                param.put("partner_phone",phone.getText().toString());
                param.put("partner_profesion","abc");
                param.put("partner_email",email.getText().toString());
                param.put("partner_password",password.getText().toString());
                param.put("partner_add",etAddress.getText().toString());
                param.put("delivery_range",etrange.getText().toString());


                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(stringRequest);

    }
    private void maincateUrl() {
        Maincatelist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MAINCATELIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Maincatelist", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        Maincatelist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            maincategory_id = jsonObject1.getString("category_id");
                            String maincaet_name = jsonObject1.getString("category_name");

                            SearchModel cs2 = new SearchModel(maincategory_id, maincaet_name);

                            Maincatelist.add(cs2);
                        }
                        progressDialog.dismiss();

                        CustomAdapter customAdapter = new CustomAdapter(getContext(), Maincatelist);
                        mainCategorySpinner.setAdapter(customAdapter);
                        mainCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                maincategoryName=Maincatelist.get(i).getpNAme();
                                maincategory_id=Maincatelist.get(i).getId();

                                categryUrl();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
                    } else {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("parent","");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void cityUrl() {
        citylist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CityListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cityyyyyyyy", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        citylist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            city_id = jsonObject1.getString("city_id");
                            String city_name = jsonObject1.getString("city_name");

                            SearchModel cs = new SearchModel(city_id, city_name);

                            citylist.add(cs);
                        }
                        progressDialog.dismiss();

                        CustomAdapter customAdapter = new CustomAdapter(getContext(), citylist);
                        city.setAdapter(customAdapter);
                        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                cityName=citylist.get(i).getpNAme();
                                city_id=citylist.get(i).getId();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
                    } else {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("parent","");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void categryUrl() {
        catelist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CategyList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cityyyyyyyy", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        catelist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            category_id = jsonObject1.getString("sub_category_id");
                            String child_name = jsonObject1.getString("sub_category_name");

                            SearchModel cs1 = new SearchModel(category_id, child_name);
                            catelist.add(cs1);

                            CustomAdapter customAdapter1 = new CustomAdapter(getContext(), catelist);
                            categry_spinner.setAdapter(customAdapter1);
                            categry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    try{
                                        categoryName=catelist.get(i).getpNAme();
                                        category_id=catelist.get(i).getId();
                                        ChildCateUrl();
                                        childcatespinner.setVisibility(View.GONE);
                                    }catch (Exception e){
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }

                    } else {
                        childcatespinner.setVisibility(View.GONE);
                        CustomAdapter customAdapter1 = new CustomAdapter(getContext(), catelist);
                        categry_spinner.setAdapter(customAdapter1);
                        categry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try{
                                    categoryName="";
                                    category_id="";

                                }catch (Exception e){
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("category_id",maincategory_id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(stringRequest);

    }

    private void ChildCateUrl() {
        childcatelist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ChildCategyList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ChildCategyList", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        childcatelist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("sub_category");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            child_category_id = jsonObject1.getString("child_category_id");
                            String child_name1 = jsonObject1.getString("child_name");

                            SearchModel cs11 = new SearchModel(child_category_id, child_name1);

                            childcatelist.add(cs11);


                            CustomAdapter customAdapter21 = new CustomAdapter(getContext(), childcatelist);
                            childcatespinner.setAdapter(customAdapter21);
                            childcatespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    try{
                                        child_category_name=childcatelist.get(i).getpNAme();
                                        child_category_id=childcatelist.get(i).getId();
                                    }catch (Exception e){
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }}else {
                        CustomAdapter customAdapter21 = new CustomAdapter(getContext(), childcatelist);
                        childcatespinner.setAdapter(customAdapter21);
                        childcatespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                try{
                                    child_category_name="Select Child";
                                    child_category_id="";
                                }catch (Exception e){
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("sub_cat_id",category_id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
