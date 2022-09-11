package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.NotificAtionList;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Adapter.NotificationAdapter;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.NoticeModelClss;
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

public class NotificationActivity extends AppCompatActivity {

    ImageView back_img,circle,search;
    TextView title;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    Session_management sessionManagement;
    String userID;
    Dialog progressDialog;
    NotificationAdapter bAdapter;
    List<NoticeModelClss>noticeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
    }

    private void init() {
        swipeRefreshLayout=findViewById(R.id.swipRefrsh);
        sessionManagement=new Session_management(this);
        userID=sessionManagement.userId();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionbar_color));
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.actionbar_color));
        }

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.title);
        title.setText("Notification");

        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerNotification);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager1);
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        hitNotificationUrl(userID);

    }

    private void hitNotificationUrl(String userID) {

        progressDialog.show();
        noticeList.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userID);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                NotificAtionList, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try{
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<NoticeModelClss>>() {
                            }.getType();
                            noticeList = gson.fromJson(response.getString("data"), listType);
                            bAdapter = new NotificationAdapter(getApplicationContext(),noticeList);
                            recyclerView.setAdapter(bAdapter);
                            bAdapter.notifyDataSetChanged();
                        }
                        else {
                        }
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally{
                    progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }
}