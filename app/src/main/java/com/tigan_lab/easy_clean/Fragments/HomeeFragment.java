package com.tigan_lab.easy_clean.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tigan_lab.easy_clean.Adapter.LeadAdapter;
import com.tigan_lab.easy_clean.Constants.Config;
import com.tigan_lab.easy_clean.Constants.CustomVolleyJsonRequest;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.ModelClass.LeadModelClass;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tigan_lab.easy_clean.Activity.MainActivity_Sp.drawer;
import static com.tigan_lab.easy_clean.Constants.Config.leadlist;


public class HomeeFragment extends Fragment {

    RecyclerView recyclerView;
    LeadAdapter bAdapter;
    Dialog progressDialog;
    ImageView slider;
    LinearLayout noData;
    TextView txtHead,notification,credits,crdts;
    private List<LeadModelClass> list=new ArrayList<>();
    Session_management sessionManagement;
    String user_id;
    public HomeeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_homee, container, false);

        sessionManagement=new Session_management(getContext());
        user_id=sessionManagement.userId();

        noData=view.findViewById(R.id.noData);
        recyclerView=view.findViewById(R.id.recyclerview);
        progressDialog = new Dialog(requireActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);;
        slider=view.findViewById(R.id.slidr);
        txtHead=view.findViewById(R.id.txtHead);
        notification=view.findViewById(R.id.notification);
        credits=view.findViewById(R.id.credits);

        crdts=view.findViewById(R.id.crdts);
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
        txtHead.setText("New Leads");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(bAdapter);

        if (!isOnline()) {
            Toast.makeText(getActivity(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
        } else {
            leadURl();
            CoinsCollectUrl();
        }
        return view;
    }

    private void leadURl() {
        progressDialog.show();
        list.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("partner_id", sessionManagement.userId());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                leadlist, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("leadlist", response.toString());
                progressDialog.dismiss();
                try{
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        String message = response.getString("message");
                        if (status.contains("1")) {
                            noData.setVisibility(View.GONE);
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<LeadModelClass>>() {
                            }.getType();
                            list = gson.fromJson(response.getString("data"), listType);
                            bAdapter = new LeadAdapter(getActivity(),list);
                            recyclerView.setAdapter(bAdapter);
                            bAdapter.notifyDataSetChanged();

                        }
                        else {
                            noData.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        noData.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally{
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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
                        String coinss = resultObj.getString("coins");
                        credits.setText(coinss);

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

}
