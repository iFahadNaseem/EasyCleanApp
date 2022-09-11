package com.tigan_lab.easy_clean.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tigan_lab.easy_clean.Activity.MainActivity_Sp.drawer;
import static com.tigan_lab.easy_clean.Constants.Config.termsCondtiosn;


public class TermsFragment extends Fragment {

    private TextView tv_info;
    String description;
    ImageView slider;
    TextView txtHead;
    public TermsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_terms, container, false);
        tv_info = (TextView) view.findViewById(R.id.tv_info);
        slider=view.findViewById(R.id.slidr);
        txtHead=view.findViewById(R.id.txtHead);
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        txtHead.setText("Terms and Conditions");
        if(isOnline()){
            makeGetInfoRequest();

        } else {
            Toast.makeText(getContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
        return view;
    }
    private void makeGetInfoRequest() {

        String tag_json_obj = "json_info_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, termsCondtiosn, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("termsCondtiosn", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");
                    if (status.contains("1")) {
                        description = response.getString("data");


                        tv_info.setText(Html.fromHtml(description).toString());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
