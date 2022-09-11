package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.CouponOffr;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.tigan_lab.customer.Adapter.OffersRecycleAdapter;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.OffersModelClass;
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

import at.grabner.circleprogress.CircleProgressView;

public class OffersActivity extends AppCompatActivity {

    TextView title,number,number1;
    ImageView back_img,search;


    private List<OffersModelClass> offersModelClasses=new ArrayList<>();
    private RecyclerView recyclerView;
    private OffersRecycleAdapter bAdapter;

    private String title1[] = {"Get 10% off* ","Get 10% off* "};

    CircleProgressView circleProgressView;
    Dialog progressDialog;
    LinearLayout noData;

    public static   String totalAmount,addonamount, ttm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        noData = findViewById(R.id.noData);

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        ttm= getIntent().getStringExtra("totlam");

        circleProgressView = findViewById(R.id.circleView);
        circleProgressView.setVisibility(View.VISIBLE);
        circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkorange));
        circleProgressView.setTextSize(20);
        circleProgressView.setBarColor(getResources().getColor(R.color.darkorange));
        circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkorange));
        circleProgressView.setValue(Float.parseFloat("50"));

        title = findViewById(R.id.title);
        title.setText("Select Offer");

        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OffersActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if(isOnline()){
            couponUrl();
        }
        else{
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }

    }

    private void couponUrl() {
        progressDialog.show();
        offersModelClasses.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("total_amount",ttm);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                CouponOffr, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try{
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<OffersModelClass>>() {
                            }.getType();
                            offersModelClasses = gson.fromJson(response.getString("data"), listType);
                            bAdapter = new OffersRecycleAdapter(getApplicationContext(),offersModelClasses);
                            recyclerView.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            recyclerView.setAdapter(bAdapter);
                            bAdapter.notifyDataSetChanged();
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }                    }
                    progressDialog.dismiss();
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

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
