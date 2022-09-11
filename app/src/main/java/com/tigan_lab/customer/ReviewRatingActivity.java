package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.KEY_ID;
import static com.tigan_lab.customer.Extra.Config.KEY_NAME;
import static com.tigan_lab.customer.Extra.Config.addRating;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.HomeCategoryModelClass;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;

public class ReviewRatingActivity extends AppCompatActivity {

    ImageView back_img;
    TextView title;


    AppCompatButton continue_sub;
    RatingBar ratingBar;
    EditText et_rating,et_rating_head;
    private ArrayList<HomeCategoryModelClass> homeCategoryModelClasses;

    Dialog progressDialog;
    Session_management session_management;
    ImageView search;
    String userid,username;

    String service_id,ratingc;
    CircleProgressView circleProgressView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review_rating);
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);
        et_rating_head=findViewById(R.id.et_rating_head);
        service_id=getIntent().getStringExtra("service_id");

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        et_rating=findViewById(R.id.et_rating);

        ratingBar=findViewById(R.id.ratingg);

        continue_sub=findViewById(R.id.continue_sub);

        session_management=new Session_management(this);

        userid=session_management.getUserDetails().get(KEY_ID);

        username=session_management.getUserDetails().get(KEY_NAME);
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.title);
        title.setText("Rating & Reviews");



        circleProgressView = findViewById(R.id.circleView);
        circleProgressView.setVisibility(View.VISIBLE);
        circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkorange));
        circleProgressView.setTextSize(20);
        circleProgressView.setBarColor(getResources().getColor(R.color.darkorange));
        circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkorange));
        circleProgressView.setValue(Float.parseFloat("20"));



        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratingc= String.valueOf(rating);
            }
        });

        continue_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_rating_head.getText().toString().length()==0){
                    Toast.makeText(ReviewRatingActivity.this, "Enter Rating Head", Toast.LENGTH_SHORT).show();
                }
                else if (et_rating.getText().toString().length()==0){
                    Toast.makeText(ReviewRatingActivity.this, "Enter Rating Description", Toast.LENGTH_SHORT).show();

                }
                else {
                    progressDialog.show();
                    rating();
                }

            }
        });


    }
    private void rating() {

        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();


        params.put("id",userid);

        params.put("review_head",et_rating_head.getText().toString());
        params.put("review_desc",et_rating.getText().toString());
        params.put("rating",ratingc);
        params.put("service_id",service_id);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                addRating, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("", response.toString());

                try {

                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        String message=response.getString("message");

                        if (status.contains("1")) {

                            progressDialog.dismiss();
                            et_rating.setText("");
                            ratingBar.setRating(0);
                            Toast.makeText(ReviewRatingActivity.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(ReviewRatingActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(ReviewRatingActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue mRequestQueue = Volley.newRequestQueue(ReviewRatingActivity.this);

        mRequestQueue.add(jsonObjReq);
    }

}
