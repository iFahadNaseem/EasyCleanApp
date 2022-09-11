package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.add_on;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.tigan_lab.customer.Adapter.Add_on_adapter;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.Add_on_model;
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

public class Add_on_Activity extends AppCompatActivity {

    TextView title,title1,type,number,number1;
    ImageView back_img,search;

    Button button;
    private static String TAG = Add_on_Activity.class.getSimpleName();
    List<Add_on_model> add_on_modelList=new ArrayList<>();


    Add_on_adapter adapter;
    String childid,titlee;
    CircleProgressView circleProgressView;
    RecyclerView recyclerView;
    Dialog progressDialog;
    LinearLayout noData;
    public  static CheckBox titleNot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom_cleaning_one);
        circleProgressView = findViewById(R.id.circleView);

        noData = findViewById(R.id.noData);
        titleNot = findViewById(R.id.titleNo);
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);

        title=findViewById(R.id.title_check);
        type = findViewById(R.id.type);
        type.setVisibility(View.GONE);
        title1=findViewById(R.id.title);
        title1.setText("Add-On");

        type.setVisibility(View.GONE);

        circleProgressView.setVisibility(View.VISIBLE);
        circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkorange));
        circleProgressView.setTextSize(20);
        circleProgressView.setBarColor(getResources().getColor(R.color.darkorange));
        circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkorange));
        circleProgressView.setValue(Float.parseFloat("14"));

        recyclerView=findViewById(R.id.rc_list);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



    childid=getIntent().getStringExtra("child_category_id");
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_on_Activity.this, Cart_Activity.class);
                startActivity(intent);
            }
        });


        add_on();


    }
    private void add_on() {

        progressDialog.show();
        add_on_modelList.clear();
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("child_category_id", childid);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                add_on, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                progressDialog.dismiss();
                try {
                     titlee=response.getString("message");

                    title.setVisibility(View.VISIBLE);
                    title.setText(titlee);

                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                            if (status.contains("1")) {

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<Add_on_model>>() {
                                }.getType();

                                add_on_modelList = gson.fromJson(response.getString("data"), listType);
                                adapter = new Add_on_adapter(Add_on_Activity.this, add_on_modelList);
                                recyclerView.setVisibility(View.VISIBLE);
                                noData.setVisibility(View.GONE);
                                recyclerView.setAdapter(adapter);
                            }
                              else {
                                  title.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
                                }


                        progressDialog.dismiss();
                    }
                    else {
                        title.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Add_on_Activity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

}
