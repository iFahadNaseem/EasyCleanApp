package com.tigan_lab.easy_clean.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.tigan_lab.easy_clean.Activity.RechargePlan;
import com.tigan_lab.easy_clean.Adapter.EarningAdapter;
import com.tigan_lab.easy_clean.Constants.Config;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tigan_lab.easy_clean.Activity.MainActivity_Sp.drawer;


public class CreditBalanceFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    EarningAdapter pageAdapter;
    TabItem tab1, tab2;

    LinearLayout buycredits;
    ImageView slider;
    TextView txtHead,notification,credits,crdts,txttt;
    Session_management sessionManagement;
    String user_id;
    public CreditBalanceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_earnings, container, false);
        sessionManagement=new Session_management(getContext());
        user_id=sessionManagement.userId();
        slider=view.findViewById(R.id.slidr);
        txtHead=view.findViewById(R.id.txtHead);
        buycredits=view.findViewById(R.id.buycredits);
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        txtHead.setText("Credit Balance");
        txttt=view.findViewById(R.id.txttt);

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
        buycredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), RechargePlan.class);
                startActivity(intent);

            }
        });

        tabLayout = view.findViewById(R.id.tablayout12);
        viewPager = view.findViewById(R.id.viewOager1);
        tab1 = view.findViewById(R.id.All);
        tab2 = view.findViewById(R.id.Recharge);

        pageAdapter = new EarningAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
//
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        CoinsCollectUrl();
        return view;
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
                        txttt.setText("Credits "+coinss);

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
