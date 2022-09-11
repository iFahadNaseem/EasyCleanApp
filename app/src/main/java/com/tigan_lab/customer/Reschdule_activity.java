package com.tigan_lab.customer;

import static com.tigan_lab.customer.Extra.Config.SplitTime;
import static com.tigan_lab.customer.Extra.Config.bookingReschedule;

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
import android.widget.LinearLayout;
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

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class Reschdule_activity extends AppCompatActivity {
    Button button;

    int layout;

    String timeslot;

    String bookingid;

    private ArrayList<timing_model> dateDayModelClasses1;

    RecyclerView recyclerView1;

    private Timing_Adapter bAdapter1;


    Dialog progressDialog;

    LinearLayout bottom_linear;

    HorizontalCalendar horizontalCalendar;




    String addressid;

    String todaydate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reschdule_);
        Calendar startDate = Calendar.getInstance();

        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        bookingid=getIntent().getStringExtra("booking_id");

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeslot=bAdapter1.gettimeslot();


                progressDialog.show();
                reschdule(timeslot);


            }
        });

        recyclerView1 = findViewById(R.id.rv_time);

        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(this,2);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        recyclerView1.setHasFixedSize(true);


        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();

        endDate.add(Calendar.MONTH, 1);

        todaydate= String.valueOf(DateFormat.format("yyyy-MM-dd",startDate));

        bottom_linear=findViewById(R.id.bottom_linear);


        horizontalCalendar = new HorizontalCalendar.Builder(Reschdule_activity.this,R.id.calendarview)
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

                todaydate = String.valueOf(DateFormat.format("yyyy-MM-dd", date));
                makeGetAddressRequest(todaydate);
            }

        });


        dateDayModelClasses1 = new ArrayList<>();
        makeGetAddressRequest(todaydate);

    }
    private void makeGetAddressRequest(String date) {

        dateDayModelClasses1.clear();
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("selected_date", date);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                SplitTime, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("", response.toString());

                try {
                    String status = response.getString("status");
                    if (status.contains("1")) {

                        JSONArray jsonArray = response.getJSONArray("data");

                        if (jsonArray.length()>0) {
                            recyclerView1.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                String data = jsonArray.getString(i);
                                timing_model mycreditList = new timing_model();
                                mycreditList.setTiming(data);
                                dateDayModelClasses1.add(mycreditList);

                            }

                            bAdapter1 = new Timing_Adapter(Reschdule_activity.this, dateDayModelClasses1);
                            recyclerView1.setAdapter(bAdapter1);
                            bAdapter1.notifyDataSetChanged();
                        }
                        else {
                            recyclerView1.setVisibility(View.GONE);
                        }
                    }
                    else {
                            recyclerView1.setVisibility(View.GONE);

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
                    if (Reschdule_activity.this != null) {
                        Toast.makeText(Reschdule_activity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }
    private void reschdule(String slot) {


        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("date", todaydate);
        params.put("booking_id",bookingid);
        params.put("time_slot",slot);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                bookingReschedule, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("", response.toString());

                try {
                    String status = response.getString("status");
                    String message=response.getString("message");
                    if (status.contains("1")) {

                        progressDialog.dismiss();
                        Intent intent=new Intent(Reschdule_activity.this,HomePageActivity.class);
                        startActivity(intent);
                    }
                    else {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(Reschdule_activity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    if (Reschdule_activity.this != null) {
                        progressDialog.dismiss();
                        Toast.makeText(Reschdule_activity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);  }

}
