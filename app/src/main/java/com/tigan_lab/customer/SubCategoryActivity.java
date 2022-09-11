package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.HomeCategory;
import static com.tigan_lab.customer.Extra.Config.HomeSubCategory;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.tigan_lab.customer.Adapter.HomeCategoryHorizontalRecycleAdapter;
import com.tigan_lab.customer.Adapter.SubCategoryAdapter;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.HomeCateModelClass;
import com.tigan_lab.customer.ModelClass.SubCateModelClass;
import com.tigan_lab.easy_clean.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoryActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    public static int tab_value;
    ImageView back_img;
    private List<HomeCateModelClass> menu_models = new ArrayList<>();
    HomeCategoryHorizontalRecycleAdapter bAdapter;
    private List<SubCateModelClass> subcateList = new ArrayList<>();
    SubCategoryAdapter subCategoryAdapter;
    private List<String> cat_menu_id = new ArrayList<>();
    public static  RecyclerView recyclerView;
    public static RecyclerView recyclerCate;
    public static  String cateId;
   public static   Dialog progressDialog;
    String id;
    ImageView search;
    public static  LinearLayout noData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__services_);

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        noData = findViewById(R.id.noData);
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);
        cateId=getIntent().getStringExtra("categoryid");
        back_img = findViewById(R.id.back_img);
        recyclerCate=findViewById(R.id.recyclerCate);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(SubCategoryActivity.this);
        recyclerCate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCate.setItemAnimator(new DefaultItemAnimator());

        recyclerView=findViewById(R.id.rv_subcategory);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        tab_value = intent.getIntExtra("layout", 0);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionbar_color));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.actionbar_color));

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        Typeface mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);






        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String getcat_id=cat_menu_id.get(tab.getPosition());

                Log.d("fafd",cat_menu_id.get(tab.getPosition()));


                if (getApplicationContext()!=null){

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        makeGetCategoryRequest(tab_value);


        makeGetProductRequest(cateId);
    }

    private void makeGetProductRequest(String getcat_id) {
        progressDialog.show();
        subcateList.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("category_id", getcat_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                HomeSubCategory, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", response.toString());
                try{
                    progressDialog.dismiss();
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<SubCateModelClass>>() {
                            }.getType();
                            subcateList = gson.fromJson(response.getString("data"), listType);
                            subCategoryAdapter = new SubCategoryAdapter(getApplicationContext(),subcateList);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(subCategoryAdapter);
                            subCategoryAdapter.notifyDataSetChanged();
                            noData.setVisibility(View.GONE);
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }
    //Get Shop By Catogary
    private void makeGetCategoryRequest(int tab_value) {
        progressDialog.show();
        menu_models.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                HomeCategory, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", response.toString());
                progressDialog.dismiss();
                try{
                if (response != null && response.length() > 0) {
                    String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<HomeCateModelClass>>() {}.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            bAdapter = new HomeCategoryHorizontalRecycleAdapter(getApplicationContext(),menu_models);
                            recyclerCate.setAdapter(bAdapter);
                            bAdapter.notifyDataSetChanged();
                            recyclerCate.scrollToPosition(tab_value);
                            //makeGetProductRequest(cateId);
                            if (!menu_models.isEmpty()) {
                                //tabLayout.setVisibility(View.VISIBLE);
                                cat_menu_id.clear();
                                for (int i = 0; i < menu_models.size(); i++) {
                                    cat_menu_id.add(menu_models.get(i).getCategory_id());
                                    tabLayout.addTab(tabLayout.newTab().setText(menu_models.get(i).getCategory_name() + "test bb"));
                                }
                            }
                        }
                        else {}
                    }
                progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
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
