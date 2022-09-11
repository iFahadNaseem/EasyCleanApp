package com.tigan_lab.customer.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Adapter.BannerAdapter;
import com.tigan_lab.customer.Adapter.HomeCategoryRecycleAdapter;
import com.tigan_lab.customer.Adapter.PopularCategoryAdapter;
import com.tigan_lab.customer.Extra.Config;
import com.tigan_lab.customer.Extra.CustomSlider;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.CategoryModel;
import com.tigan_lab.customer.ModelClass.HomeCateModelClass;
import com.tigan_lab.easy_clean.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class HomeFragment extends Fragment {

    private  View view;
    private List<HomeCateModelClass> menu_models = new ArrayList<>();
    private RecyclerView recyclerView,imgSlider,recyclerSlider1;
    HomeCateModelClass mycreditList;
    private HomeCategoryRecycleAdapter bAdapter;
    private static String TAG = HomeFragment.class.getSimpleName();

    private BannerAdapter bannerAdapter;
    ArrayList<String> imageString = new ArrayList<>();
    ArrayList<String> imageString1 = new ArrayList<>();
     SliderLayout imgSlider1,imgSlider2;
    List<CategoryModel> cateList=new ArrayList<>();
    PopularCategoryAdapter categoryAdapter;
    RecyclerView recyclerViewPopular;
    String latitude,longitude;
    Session_management sessionManagement;
    TextView txtPpp,txtSss;
    ViewPager viewPager;
    Dialog progressDialog;
    //MyCustomPagerAdapter myCustomPagerAdapter;
    Timer timer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        progressDialog = new Dialog(requireActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);

        sessionManagement = new Session_management(requireContext());
        latitude=sessionManagement.Lat();
        longitude=sessionManagement.Lng();
        txtPpp=view.findViewById(R.id.txtPpp);
        txtSss=view.findViewById(R.id.txtSss);
        bannerAdapter = new BannerAdapter(getActivity(), imageString1);
        imgSlider =  view.findViewById(R.id.recyclerSlider);
        recyclerSlider1 =  view.findViewById(R.id.recyclerSlider1);
        recyclerSlider1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerSlider1.setAdapter(bannerAdapter);
        imgSlider1=view.findViewById(R.id.banner);
        imgSlider2=view.findViewById(R.id.banner1);
        recyclerViewPopular=view.findViewById(R.id.recycler_popularServices);


        recyclerView = view.findViewById(R.id.CategoryRecyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(isOnline()){
            makeGetSliderRequest();
            popularcategoryUrl();
            makeGetSliderRequest1();
            category();

        }else {
            Toast.makeText(getContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }

        return  view;
    }
    private void makeGetSliderRequest() {
        imageString.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST, Config.HomeBanner, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fghgh", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length() <= 0) {
                                recyclerSlider1.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> url_maps = new HashMap<String, String>();
                                    url_maps.put("banner_name", jsonObject.getString("banner_name"));
                                    url_maps.put("banner_id", jsonObject.getString("id"));
                                    url_maps.put("banner_image", Config.IMAGE_URL + jsonObject.getString("banner_image"));
                                    imageString.add(Config.IMAGE_URL + jsonObject.getString("banner_image"));
                                    listarray.add(url_maps);
                                }
                                bannerAdapter.notifyDataSetChanged();

                                for (HashMap<String, String> name : listarray) {
                                    CustomSlider textSliderView = new CustomSlider(getActivity());
                                    textSliderView.description(name.get("")).image(name.get("banner_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                    textSliderView.bundle(new Bundle());
                                    textSliderView.getBundle().putString("extra", name.get("banner_name"));
                                    textSliderView.getBundle().putString("extra", name.get("banner_id"));
                                      imgSlider1.addSlider(textSliderView);
                                    final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                    textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(BaseSliderView slider) { }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);


    }

    private void makeGetSliderRequest1() {
        imageString1.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST, Config.HomeBanner,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fghgh", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            JSONArray jsonArray=response.getJSONArray("data");
                            if (jsonArray.length()<=0)
                            {
                                recyclerSlider1.setVisibility(View.GONE);
                            }
                            else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> url_maps = new HashMap<String, String>();
                                    url_maps.put("banner_name", jsonObject.getString("banner_name"));
                                    url_maps.put("banner_id", jsonObject.getString("id"));
                                    url_maps.put("banner_image", Config.IMAGE_URL + jsonObject.getString("banner_image"));
                                    imageString1.add(Config.IMAGE_URL + jsonObject.getString("banner_image"));
                                    listarray.add(url_maps);
                                }

                                bannerAdapter.notifyDataSetChanged();

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
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

    private void popularcategoryUrl() {
    progressDialog.show();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.HomePopularService, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("homeCateeeeeee",response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")){
                    progressDialog.dismiss();
                    txtPpp.setVisibility(View.VISIBLE);
                    cateList.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                        CategoryModel homeC1 = new CategoryModel();
                        homeC1.id = jsonObject3.getString("service_id");
                        homeC1.pName = jsonObject3.getString("service_name");
                        homeC1.pImage = jsonObject3.getString("service_img");
                        homeC1.pchildcatId = jsonObject3.getString("child_category_id");
                        cateList.add(homeC1);
                    }

                    recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    categoryAdapter = new PopularCategoryAdapter(getContext(),cateList,HomeFragment.class);
                    recyclerViewPopular.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();


                }else {
                    txtPpp.setVisibility(View.GONE);
                    JSONObject resultObj = jsonObject.getJSONObject("results");
                    String msg = resultObj.getString("message");
                    Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressDialog.dismiss();

        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String,String> params = new HashMap<>();
            params.put("city_id",sessionManagement.getCityId());
            Log.d("hlredtyyg",sessionManagement.getCityId());
            return params;
        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
    requestQueue.getCache().clear();
    requestQueue.add(stringRequest);

}

    private void category() {
        progressDialog.show();
        menu_models.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Config.HomeCategory, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
              //
                try {
                    if (response != null && response.length() > 0) {
                        txtSss.setVisibility(View.VISIBLE);
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            progressDialog.dismiss();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<HomeCateModelClass>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            bAdapter = new HomeCategoryRecycleAdapter(getActivity(),menu_models);
                            recyclerView.setAdapter(bAdapter);

                            bAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        else {
                            txtSss.setVisibility(View.GONE);
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}

