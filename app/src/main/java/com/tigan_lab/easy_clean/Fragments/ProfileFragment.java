package com.tigan_lab.easy_clean.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.squareup.picasso.Picasso;
import com.tigan_lab.easy_clean.Activity.ProfileEdit;
import com.tigan_lab.easy_clean.Constants.Config;
import com.tigan_lab.easy_clean.Constants.CustomVolleyJsonRequest;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tigan_lab.easy_clean.Activity.MainActivity_Sp.drawer;
import static com.tigan_lab.easy_clean.Constants.Config.IMAGE_URL;
import static com.tigan_lab.easy_clean.Constants.Config.Profile;


public class ProfileFragment extends Fragment {


    TextView tname,tprofession,tbusinessNAme,twebsite,txtProf,email,mobNo,editbtn,txtupi;
    Dialog progressDialog;
    String coinsss,userId;
    Session_management sessionManagement;
    CircleImageView profimage;
    ImageView slider;
    TextView txtHead,notification,credits,crdts,savebtn;
    EditText edit;
    LinearLayout ll;
    String partnerID;
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_sp, container, false);
        progressDialog = new Dialog(requireActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        sessionManagement=new Session_management(getContext());
        userId= sessionManagement.userId();

        savebtn=view.findViewById(R.id.saveBtn);
        ll=view.findViewById(R.id.sve);
        edit=view.findViewById(R.id.etupi);
        txtupi=view.findViewById(R.id.txtupi);
        editbtn=view.findViewById(R.id.eidt);
        tname=view.findViewById(R.id.name);
        tprofession=view.findViewById(R.id.profession);
        tbusinessNAme=view.findViewById(R.id.businessName);
        twebsite=view.findViewById(R.id.website);
        txtProf=view.findViewById(R.id.txtprofession);
        email=view.findViewById(R.id.temail);
        mobNo=view.findViewById(R.id.tphone);
        profimage=view.findViewById(R.id.profimage);
        slider=view.findViewById(R.id.slidr);
        txtHead=view.findViewById(R.id.txtHead);
        notification=view.findViewById(R.id.notification);
        credits=view.findViewById(R.id.credits);
        crdts=view.findViewById(R.id.crdts);
        tname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(getActivity(), ProfileEdit.class);
              startActivity(intent);

            }
        });
        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditBalanceFragment tm = new CreditBalanceFragment();
                FragmentManager manager21 = getFragmentManager();
                FragmentTransaction transaction21 = manager21.beginTransaction();
                transaction21.replace(R.id.contentPanell, tm);
                transaction21.commit();

            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ll.setVisibility( View.VISIBLE);
               txtupi.setVisibility( View.GONE);

            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hitUrl(userId,edit.getText().toString().trim());
            }
        });


        crdts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditBalanceFragment tm = new CreditBalanceFragment();
                FragmentManager manager21 = getFragmentManager();
                FragmentTransaction transaction21 = manager21.beginTransaction();
                transaction21.replace(R.id.contentPanell, tm);
                transaction21.commit();

            }
        });
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        txtHead.setText("My Profile");
        if (!isOnline()) {
            Toast.makeText(getActivity(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
        } else {
           profileDataUrl();
           CoinsCollectUrl();
        }
        return view;
    }

    private void hitUrl(final String userId,final String upii) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Login",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){

                        ll.setVisibility( View.GONE);
                        txtupi.setVisibility( View.VISIBLE);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        profileDataUrl();
                    }else {
                        ll.setVisibility( View.VISIBLE);
                        txtupi.setVisibility( View.GONE);
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
                param.put("partner_id",userId);
                param.put("upi",upii);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void profileDataUrl() {
        progressDialog.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("partner_id", userId);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Profile, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONObject obj = response.getJSONObject("data");
                        String partner_id = obj.getString("partner_id");
                        String user_fullname = obj.getString("partner_name");
                        String user_email = obj.getString("partner_email");
                        String user_phone = obj.getString("partner_phone");
                        String partner_profesion = obj.getString("partner_profesion");
                        String upi = obj.getString("upi");
                        String coins = obj.getString("coins");
                        String partner_image = obj.getString("partner_image");
                        tprofession.setText(partner_profesion);
                        txtProf.setText(partner_profesion);
                        tname.setText(user_fullname);
                        mobNo.setText(user_phone);
                        email.setText(user_email);
                        if(upi!=null&& !upi.equalsIgnoreCase("null")){
                            txtupi.setText(upi);
                        }else {
                            txtupi.setText("--");
                        }

                        Picasso.with(getContext()).load(IMAGE_URL+partner_image).error(R.drawable.logo).into(profimage);
                    }}
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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
                      String  coinsss = resultObj.getString("coins");
                        credits.setText(coinsss);

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
                param.put("partner_id",userId);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        profileDataUrl();
    }
}
