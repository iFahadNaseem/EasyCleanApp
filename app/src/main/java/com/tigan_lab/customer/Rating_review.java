package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.ratinglist;

import android.content.Context;
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
import com.tigan_lab.customer.Adapter.ReviewRatingRecycleAdapter;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.Rating_review_model;
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


public class Rating_review extends AppCompatActivity {
    private ReviewRatingRecycleAdapter bAdapter;

    private static String TAG="sss";
    ImageView back_img,search;
    List<Rating_review_model> list=new ArrayList<>();
    ReviewRatingRecycleAdapter reviewRatingRecycleAdapter;
    RecyclerView recyclerView;
    TextView title;
    LinearLayout noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_review);
        noData = findViewById(R.id.noData);
        recyclerView = findViewById(R.id.rc);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Rating_review.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        title = findViewById(R.id.title);
        title.setText("Review Rating");
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(isOnline()){
            ratinglist();
        } else {
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
}
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void ratinglist() {
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ratinglist, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Rating_review_model>>() {
                            }.getType();
                            list = gson.fromJson(response.getString("data"), listType);
                            bAdapter = new ReviewRatingRecycleAdapter(Rating_review.this,list);
                            recyclerView.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            recyclerView.setAdapter(bAdapter);
                            bAdapter.notifyDataSetChanged();
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Rating_review.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }


}
