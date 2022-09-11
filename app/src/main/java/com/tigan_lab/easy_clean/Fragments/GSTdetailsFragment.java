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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tigan_lab.easy_clean.Activity.BankEdit;
import com.tigan_lab.easy_clean.Activity.GstPage;
import com.tigan_lab.easy_clean.Activity.PanPage;
import com.tigan_lab.easy_clean.Constants.CustomVolleyJsonRequest;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tigan_lab.easy_clean.Activity.MainActivity_Sp.drawer;
import static com.tigan_lab.easy_clean.Constants.Config.IMAGE_URL;
import static com.tigan_lab.easy_clean.Constants.Config.bankShow;
import static com.tigan_lab.easy_clean.Constants.Config.gstShow;


public class GSTdetailsFragment extends Fragment {

    Dialog progressDialog;
    Session_management sessionManagement;
    LinearLayout llImg;
    TextView GSTchnge,Panchnge,BankChnge,edit1,edit2,edit3;
    TextView gstNumbr,nameProof,idProof,gstName,BnkName,AccNo,IFSC,userName,PAnCard;
    String partnerID;
    ImageView slider,frntImg,bckImg;
    TextView txtHead;
    public GSTdetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_gst_pan, container, false);

        sessionManagement=new Session_management(getContext());
        progressDialog = new Dialog(requireActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        slider=view.findViewById(R.id.slidr);
        txtHead=view.findViewById(R.id.txtHead);
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        txtHead.setText("Account Details");
        partnerID=sessionManagement.userId();


        llImg=view.findViewById(R.id.llImg);
        idProof=view.findViewById(R.id.idProof);
        nameProof=view.findViewById(R.id.nameproof);
        GSTchnge=view.findViewById(R.id.chnge1);
        Panchnge=view.findViewById(R.id.chnge2);
        BankChnge=view.findViewById(R.id.chnge3);
        edit1=view.findViewById(R.id.edit1);
        edit2=view.findViewById(R.id.edit2);
        edit3=view.findViewById(R.id.edit3);

        frntImg=view.findViewById(R.id.frntImg);
        bckImg=view.findViewById(R.id.bckImg);
        PAnCard=view.findViewById(R.id.pancard);
        gstNumbr=view.findViewById(R.id.gstNumber);
        gstName=view.findViewById(R.id.GSTname);

        BnkName=view.findViewById(R.id.bnkNme);
        AccNo=view.findViewById(R.id.accNo);
        IFSC=view.findViewById(R.id.ifsc);
        userName=view.findViewById(R.id.NAme);

        GSTchnge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), GstPage.class);
                startActivity(intent);
            }
        });

        BankChnge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), BankEdit.class);
                startActivity(intent);
            }
        });

        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), GstPage.class);
                intent.putExtra("id","a");
                startActivity(intent);
            }
        });
        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), PanPage.class);
                intent.putExtra("id","a");
                startActivity(intent);
            }
        });
        edit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), BankEdit.class);
                intent.putExtra("id","a");
                startActivity(intent);
            }
        });


        if (!isOnline()) {
            Toast.makeText(getActivity(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
        } else {
           bankListURl(partnerID);
           gstPanListURl(partnerID);

        }
        return view;
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
                            userName.setText(holder_name);
                            BankChnge.setVisibility(View.GONE);
                            edit3.setVisibility(View.VISIBLE);
                        }
                    }else {
                        BankChnge.setVisibility(View.VISIBLE);
                        edit3.setVisibility(View.GONE);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }

    private void gstPanListURl(final String partnerID) {
        progressDialog.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("partner_id", partnerID);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                gstShow, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("gstPAnShow", response.toString());
                progressDialog.dismiss();
                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONArray jsonArray = response.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject obj=jsonArray.getJSONObject(i);

                            String identify_proof = obj.getString("identify_proof");
                            String front_image = obj.getString("front_image");
                            String back_image = obj.getString("back_image");
                            String voter_card_number = obj.getString("voter_card_number");
                            String gender = obj.getString("gender");
                            String c_o = obj.getString("c_o");
                            String d_o_b = obj.getString("d_o_b");
                            String permanent_add = obj.getString("permanent_add");

                            if(identify_proof.equalsIgnoreCase("GST Details")){
                                idProof.setText("GST Number");
                                nameProof.setText("Name (as registered in GST)");
                            }else {
                                idProof.setText("PAN Number");
                                nameProof.setText("Name (as registered in PAN)");
                            }
                            gstName.setText(c_o);
                            gstNumbr.setText(voter_card_number);
                            Picasso.with(getContext()).load(IMAGE_URL+front_image).into(frntImg);
                            Picasso.with(getContext()).load(IMAGE_URL+back_image).into(bckImg);

                            GSTchnge.setVisibility(View.GONE);
                            edit1.setVisibility(View.VISIBLE);
                            llImg.setVisibility(View.VISIBLE);
                        }
                    }else {
                        GSTchnge.setVisibility(View.VISIBLE);
                        edit1.setVisibility(View.GONE);
                        llImg.setVisibility(View.GONE);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }


    @Override
    public void onResume() {
        super.onResume();
        bankListURl(sessionManagement.userId());
        gstPanListURl(sessionManagement.userId());

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
