package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.FAQansList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.tigan_lab.customer.Adapter.FAQlastAdapter;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.FaqLastModelClass;
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

public class FaqlastActivity extends AppCompatActivity {

    ImageView back_img,circle,search;
    TextView title,btnSave;
    RecyclerView recyclerview;
    FAQlastAdapter adapter;
    List<FaqLastModelClass> faqList=new ArrayList<>();
    Dialog progressDialog;
    String faqId1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqlast);
        init();
    }
    private void init() {
        faqId1=getIntent().getStringExtra("desc_Id");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionbar_color));
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.actionbar_color));
        }
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.title);
        title.setText("F.A.Q.");
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        recyclerview=findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager1);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        if(isOnline()){
            faqAnsUrl();
        } else {
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
    }

    private void faqAnsUrl(){
        progressDialog.show();
        faqList.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("faq_cat_id",faqId1);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                FAQansList, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try{
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<FaqLastModelClass>>() {
                            }.getType();
                            faqList = gson.fromJson(response.getString("data"), listType);
                            adapter = new FAQlastAdapter(getApplicationContext(),faqList);
                            recyclerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        else {}
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