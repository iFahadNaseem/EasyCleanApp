package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.HomePopularService;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.tigan_lab.customer.Adapter.PopluarAdapter;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.Extra.DatabaseHandler;
import com.tigan_lab.customer.ModelClass.PopularServiceModelClss;
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

public class PopularServicesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView back_img;
    ImageView search;
    TextView title;
    Dialog progressDialog;
    private List<PopularServiceModelClss> cateList=new ArrayList<>();
    DatabaseHandler dbcart;
    Session_management session_management;
    String lat,lng,childid;
    private PopluarAdapter Adapter;
    public static TextView tv_items,tv_price,summary;
    public  static LinearLayout check,bottom_linearrr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_services);
        intiview();
    }

    private void intiview() {
        childid=getIntent().getStringExtra("child_category_id");
        bottom_linearrr = findViewById(R.id.bottom_linear);
        dbcart=new DatabaseHandler(this);

        bottom_linearrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Add_on_Activity.class);
                intent.putExtra("child_category_id",childid);
                startActivity(intent);
            }
        });
        check=findViewById(R.id.check);
        tv_items=findViewById(R.id.items);
        tv_price=findViewById(R.id.price);
        summary=findViewById(R.id.summary);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Cart_Activity.class);
                intent.putExtra("child_category_id",childid);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.rv_populrservices);

        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.title);
        title.setText("Popular Services");

        session_management=new Session_management(this);
        lat=session_management.Lat();
        lng=session_management.Lng();
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);

        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(isOnline()){
            hitServiceUrl(lat,lng);
        } else {
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
        if (dbcart.getCartCount()>0){
            int countquant=dbcart.getCartCount();
            tv_items.setText(String.valueOf(countquant));

            tv_price.setText(getResources().getString(R.string.currency)+dbcart.getTotalAmount());

            bottom_linearrr.setVisibility(View.VISIBLE);
            check.setVisibility(View.VISIBLE);

        }
        else {

            tv_items.setText("0");

            tv_price.setText("0");

            bottom_linearrr.setVisibility(View.GONE);
            check.setVisibility(View.GONE);

        }
    }

    private void hitServiceUrl(final String lat,final String lng) {
        progressDialog.show();
        cateList.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("city_id",session_management.getCityId());

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                HomePopularService, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.show();
                try{
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        progressDialog.dismiss();
                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<PopularServiceModelClss>>() {
                            }.getType();
                            cateList = gson.fromJson(response.getString("data"), listType);
                            Adapter = new PopluarAdapter(getApplicationContext(),cateList);

                            recyclerView.setAdapter(Adapter);
                            Adapter.notifyDataSetChanged();
                        }

                        else {

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
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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