package com.tigan_lab.customer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tigan_lab.customer.Adapter.CustomAdapter;
import com.tigan_lab.customer.Adapter.SelectCountryRecycleAdapter;
import com.tigan_lab.customer.Extra.Config;
import com.tigan_lab.customer.ModelClass.SearchModel;
import com.tigan_lab.customer.ModelClass.SelectCountryModelClass;

import static com.tigan_lab.customer.Extra.Config.CityListUrl;
import static com.tigan_lab.customer.Extra.Config.DEVICE_ID;
import static com.tigan_lab.customer.Extra.Config.EMAIL;
import static com.tigan_lab.customer.Extra.Config.MOBILE;
import static com.tigan_lab.customer.Extra.Config.NAME;
import static com.tigan_lab.customer.Extra.Config.USER_ID;
import static android.provider.Telephony.Carriers.PASSWORD;

public class SignUpActivity extends AppCompatActivity {

    EditText eName,eEmail,eMobile,ePass,erefferalCode;
    Button button;
    Dialog progressDialog;
    String emailPattern,androidID;
    TextView txtSignUp;
    Session_management sessionManagement;

    Dialog slideDialog;
    LinearLayout sppiner;
    ImageView img;
    TextView txt;
    String token;
     ArrayList<SelectCountryModelClass> selectCountryModelClasses;
     RecyclerView recyclerView;
     SelectCountryRecycleAdapter bAdapter;
    String city_id,cityName;
    private AppCompatSpinner city;
    final List<SearchModel> citylist = new ArrayList<>();
    private Integer image[] = {R.drawable.pkflag};
    private String country_name[] = {"Pakistan"};
    private String country_code[] = {"+92"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        inht();
    }

    private void inht() {
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        sessionManagement=new Session_management(this);

        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        token = task.getResult().getToken();
                    } else {
                        token = "";
                    }

                });
        emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        txtSignUp=findViewById(R.id.txtSignUp);
        eName=findViewById(R.id.etName);
        eEmail=findViewById(R.id.etEmail);
        eMobile=findViewById(R.id.etMob);
        ePass=findViewById(R.id.etPassword);
        button=findViewById(R.id.button);
        img = findViewById(R.id.image);
        txt = findViewById(R.id.country_code);
        city=findViewById(R.id.city);
        sppiner = findViewById(R.id.sppiner);
        sppiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideDialog = new Dialog(SignUpActivity.this, R.style.CustomDialogAnimation);
                slideDialog.setContentView(R.layout.select_country_popup);


                slideDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = slideDialog.getWindow();

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                slideDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
                layoutParams.copyFrom(slideDialog.getWindow().getAttributes());

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.65);

                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = height;
                layoutParams.gravity = Gravity.BOTTOM;


                recyclerView = slideDialog.findViewById(R.id.recyclerview);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                selectCountryModelClasses = new ArrayList<>();

                for (int i = 0; i < image.length; i++) {
                    SelectCountryModelClass mycreditList = new SelectCountryModelClass(image[i],country_name[i],country_code[i]);
                    selectCountryModelClasses.add(mycreditList);
                }
                bAdapter = new SelectCountryRecycleAdapter(SignUpActivity.this,selectCountryModelClasses);
                recyclerView.setAdapter(bAdapter);

                slideDialog.getWindow().setAttributes(layoutParams);
                slideDialog.setCancelable(true);
                slideDialog.setCanceledOnTouchOutside(true);
                slideDialog.show();
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });cityUrl();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eName.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Full name required!",Toast.LENGTH_SHORT).show(); }
                else if(eEmail.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Email id required!",Toast.LENGTH_SHORT).show(); }
                else if(!eEmail.getText().toString().trim().matches(emailPattern)){
                    Toast.makeText(getApplicationContext(),"Valid Email id required!",Toast.LENGTH_SHORT).show(); }
                else if(eMobile.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Mobile Number required!",Toast.LENGTH_SHORT).show(); }
                else if (eMobile.getText().toString().trim().length()>10) {
                    Toast.makeText(getApplicationContext(),"Valid Mobile Number required!",Toast.LENGTH_SHORT).show(); }
                else if(ePass.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Password required!",Toast.LENGTH_SHORT).show();
                }
                else if(!isOnline()){
                    Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
                }else {
                    signUpUrl();
                } }
        });

    }

    private void signUpUrl() {

        if (token != null && !token.equalsIgnoreCase("")) {
            hitService();
        } else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            token = task.getResult().getToken();
                            signUpUrl();
                        } else {
                            token = "";
                        }
                    });
        }


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
                            cityName = jsonObject1.getString("city_name");

                            SearchModel cs = new SearchModel(city_id, cityName);

                            citylist.add(cs);
                        }
                        progressDialog.dismiss();

                        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), citylist);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("parent","");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void hitService() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SignUP, new Response.Listener<String>() {
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
                        String user_id = resultObj.getString("id");
                        String mobile = resultObj.getString("user_phone");
                        String email = resultObj.getString("user_email");
                        String name = resultObj.getString("user_name");
                        /*String user_status = resultObj.getString("status");*/
                        String adminID = resultObj.getString("device_id");
                        String user_image= resultObj.getString("user_image");
                        String cityid= resultObj.getString("city_id");
                        String cityname= resultObj.getString("city_name");
                        sessionManagement = new Session_management(SignUpActivity.this);
                        sessionManagement.createLoginSession(user_id, email, name, mobile, user_image,ePass.getText().toString());
                        sessionManagement.setCityID(cityid);
                        sessionManagement.setCityName(cityname);

                        SharedPreferences.Editor editor = getSharedPreferences(Config.MyPrefreance,MODE_PRIVATE).edit();
                        editor.putString(USER_ID,user_id);
                        editor.putString(MOBILE,mobile);
                        editor.putString(EMAIL,email);
                        editor.putString(NAME,name);
                        //editor.putString(USER_STATUS,user_status);
                        editor.putString(DEVICE_ID,adminID);
                        editor.putString(PASSWORD,ePass.getText().toString());
                        editor.apply();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(getApplicationContext(),OTPsignupActivity.class);
                        intent.putExtra("MobNo",mobile);
                        System.out.println("Starting otp activity from signup activity");
                        startActivity(intent);

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
                HashMap<String,String> param = new HashMap<>();
                param.put("user_phone",txt.getText().toString()+eMobile.getText().toString());
                param.put("user_name",eName.getText().toString());
                param.put("user_email",eEmail.getText().toString());
                param.put("password",ePass.getText().toString());
                param.put("city_id",city_id);
                param.put("device_id",token);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    public void selectedCountry(Integer image, String country_code) {
        img.setImageResource(image);
        txt.setText(country_code);
        slideDialog.dismiss();
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}