package com.tigan_lab.customer.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Adapter.BookingOnGoingAdapter;
import com.tigan_lab.customer.Extra.Config;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.HomePageActivity;
import com.tigan_lab.customer.ModelClass.OnGoingBookingModelClass;
import com.tigan_lab.easy_clean.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OnGoingFragment extends Fragment {


    private View view;


    private List<OnGoingBookingModelClass> onGoingBookingModelClasses =new ArrayList<>();
    public static RecyclerView recyclerView;
    private BookingOnGoingAdapter bAdapter;

    private String title[] = {"Salon at home for Women","Massage for Men"};
    Dialog progressDialog;
    Session_management sessionManagement;
    String userID;
    TextView btnBook;
    public static FrameLayout noData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_on_going, container, false);

        noData= view.findViewById(R.id.noData);
        btnBook= view.findViewById(R.id.btnBook);
        sessionManagement=new Session_management(getContext());
        userID=sessionManagement.userId();

        progressDialog = new Dialog(requireActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);

        recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(getContext(), HomePageActivity.class);
               startActivity(intent);
            }
        });
        if(isOnline()){
            hitOngoingUrl(userID);
        }
        else {
            Toast.makeText(getContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
        return  view;
    }

    private void hitOngoingUrl(String userID) {
        progressDialog.show();
        onGoingBookingModelClasses.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", userID);

            CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                    Config.bookingonGOING, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("TAG", response.toString());
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<OnGoingBookingModelClass>>() {
                                }.getType();
                                onGoingBookingModelClasses = gson.fromJson(response.getString("data"), listType);
                                bAdapter = new BookingOnGoingAdapter(getContext(), onGoingBookingModelClasses);
                                recyclerView.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.GONE);
                                recyclerView.setAdapter(bAdapter);
                                bAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                            else {
                                recyclerView.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);

                            }
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
}
