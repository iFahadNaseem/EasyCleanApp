package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.SplitTime;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.tigan_lab.customer.Adapter.Timing_Adapter;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.timing_model;
import com.tigan_lab.easy_clean.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class Delivery_date_time extends AppCompatActivity {


    TextView title,number,number1,type;

    ImageView back_img,search;

    Button button;

    int layout;

    String timeslot;


    private ArrayList<timing_model> dateDayModelClasses1;

    private RecyclerView recyclerView1;

    private Timing_Adapter bAdapter1;
    String todaydatee;


    LinearLayout bottom_linear;

    HorizontalCalendar horizontalCalendar;





    String addressid;


    CircleProgressView circleProgressView;
    TextView noData;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_cleaning_fifth);
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        noData=findViewById(R.id.noData);
        addressid=getIntent().getStringExtra("address_id");

        Calendar startDate = Calendar.getInstance();

        todaydatee= String.valueOf(DateFormat.format("yyyy-MM-dd",startDate));
        dateDayModelClasses1 = new ArrayList<>();

        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();

        endDate.add(Calendar.MONTH, 1);

        bottom_linear=findViewById(R.id.bottom_linear);

        horizontalCalendar = new HorizontalCalendar.Builder(Delivery_date_time.this,R.id.calendarView)
                .range(startDate, endDate)

                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .textSize(11f, 20f, 12f)
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.GRAY, Color.BLACK)
                .end()
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                dateDayModelClasses1.clear();
               todaydatee = String.valueOf(DateFormat.format("yyyy-MM-dd", date));
                makeGetAddressRequest(todaydatee);
            }

        });


        type = findViewById(R.id.type);
        title = findViewById(R.id.title);

        type.setVisibility(View.GONE);
        Intent j=getIntent();
        layout = j.getIntExtra("layout",0);
        title.setText("Select Date & Time");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setValue(Float.parseFloat("70"));

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

                timeslot=bAdapter1.gettimeslot();

                    Intent intent = new Intent(Delivery_date_time.this, Payment_order_activty.class);
                    intent.putExtra("address_id",addressid);
                    intent.putExtra("date",todaydatee);
                    intent.putExtra("time",timeslot);
                    startActivity(intent);
            }
        });




        recyclerView1 = findViewById(R.id.recyclerview_timing);

        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(Delivery_date_time.this,2);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());






        makeGetAddressRequest(todaydatee);


    }
    private void makeGetAddressRequest(String date) {
        progressDialog.show();
        dateDayModelClasses1.clear();

        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("selected_date", date);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                SplitTime, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("dfgh", response.toString());

                progressDialog.dismiss();
                try {
                    String status = response.getString("status");
                    String msg = response.getString("message");
                    if (status.contains("1")) {

                        dateDayModelClasses1.clear();
                        JSONArray jsonArray = response.getJSONArray("data");

                        if (jsonArray.length()>0) {


                            recyclerView1.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                String data = jsonArray.getString(i);
                                timing_model mycreditList = new timing_model();
                                mycreditList.setTiming(data);
                                dateDayModelClasses1.add(mycreditList);

                            }
                            recyclerView1.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            bAdapter1 = new Timing_Adapter(Delivery_date_time.this, dateDayModelClasses1);
                            recyclerView1.setAdapter(bAdapter1);
                            bAdapter1.notifyDataSetChanged();
                        }
                        else {
                            recyclerView1.setVisibility(View.GONE);
                            noData.setText(msg);
                            noData.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        recyclerView1.setVisibility(View.GONE);
                        noData.setText(msg);
                        noData.setVisibility(View.VISIBLE);


                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (Delivery_date_time.this != null) {
                        progressDialog.dismiss();
                    }
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

}
