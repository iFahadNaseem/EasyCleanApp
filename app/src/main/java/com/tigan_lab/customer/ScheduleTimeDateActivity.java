package com.tigan_lab.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.Adapter.DateDayRecycleAdapter;
import com.tigan_lab.customer.Adapter.TimeRecycleAdapter;
import com.tigan_lab.customer.ModelClass.DateDayModelClass;
import com.tigan_lab.easy_clean.R;

import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;

public class ScheduleTimeDateActivity extends AppCompatActivity {

    TextView title,number,number1;
    ImageView back_img,search;
    Button button;
    int layout;



    private ArrayList<DateDayModelClass> dateDayModelClasses;
    private RecyclerView recyclerView;
    private DateDayRecycleAdapter bAdapter;

    private  String date[] = {"15","16","17","18","19","20","21","22","23","24","25"};
    private String day[] = {"MON","TUS","WEN","Thu","FRI","SAT","SUN","MON","TUS","WEN","Thu","FRI","SAT","SUN","MON"};



    private ArrayList<DateDayModelClass> dateDayModelClasses1;
    private RecyclerView recyclerView1;
    private TimeRecycleAdapter bAdapter1;

    private String time[] = {"09:00","10:00","11:00"};
    private String hours[] = {"am","am","am"};





    private ArrayList<DateDayModelClass> dateDayModelClasses2;
    private RecyclerView recyclerView2;
    private TimeRecycleAdapter bAdapter2;

    private String time2[] = {"12:00","01:00","02:00","03:00","04:00"};
    private String hours2[] = {"pm","pm","pm","pm","pm"};





    private ArrayList<DateDayModelClass> dateDayModelClasses3;
    private RecyclerView recyclerView3;
    private TimeRecycleAdapter bAdapter3;

    private String time3[] = {"06:00","07:00","08:00"};
    private String hours3[] = {"pm","pm","pm"};


    CircleProgressView circleProgressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_time_date);


        title = findViewById(R.id.title);


        Intent j=getIntent();
        layout=j.getIntExtra("layout",0);
        if(layout==1){
            title.setText("Salon at home for Women");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setValue(Float.parseFloat("80"));
        }
        if(layout==2){
            title.setText("Attending Wedding, Party etc.");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setValue(Float.parseFloat("75"));

        }


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
                Intent intent = new Intent(ScheduleTimeDateActivity.this, PaymentActivityy1.class);
                intent.putExtra("layout",layout);
                startActivity(intent);
            }
        });





        recyclerView = findViewById(R.id.recyclerview_date);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ScheduleTimeDateActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        dateDayModelClasses = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            DateDayModelClass mycreditList = new DateDayModelClass(date[i],day[i]);
            dateDayModelClasses.add(mycreditList);
        }
        bAdapter = new DateDayRecycleAdapter(ScheduleTimeDateActivity.this,dateDayModelClasses);
        recyclerView.setAdapter(bAdapter);





        recyclerView1 = findViewById(R.id.recyclerview_morning);

        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(ScheduleTimeDateActivity.this,3);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        dateDayModelClasses1 = new ArrayList<>();

        for (int i = 0; i < time.length; i++) {
            DateDayModelClass mycreditList = new DateDayModelClass(time[i],hours[i]);
            dateDayModelClasses1.add(mycreditList);
        }
        bAdapter1 = new TimeRecycleAdapter(ScheduleTimeDateActivity.this,dateDayModelClasses1);
        recyclerView1.setAdapter(bAdapter1);





        recyclerView2 = findViewById(R.id.recyclerview_afternoon);

        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(ScheduleTimeDateActivity.this,3);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        dateDayModelClasses2 = new ArrayList<>();

        for (int i = 0; i < time2.length; i++) {
            DateDayModelClass mycreditList = new DateDayModelClass(time2[i],hours2[i]);
            dateDayModelClasses2.add(mycreditList);
        }
        bAdapter2 = new TimeRecycleAdapter(ScheduleTimeDateActivity.this,dateDayModelClasses2);
        recyclerView2.setAdapter(bAdapter2);




        recyclerView3 = findViewById(R.id.recyclerview_evening);

        RecyclerView.LayoutManager layoutManager3 = new GridLayoutManager(ScheduleTimeDateActivity.this,3);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());

        dateDayModelClasses3 = new ArrayList<>();

        for (int i = 0; i < time3.length; i++) {
            DateDayModelClass mycreditList = new DateDayModelClass(time3[i],hours3[i]);
            dateDayModelClasses3.add(mycreditList);
        }
        bAdapter3 = new TimeRecycleAdapter(ScheduleTimeDateActivity.this,dateDayModelClasses3);
        recyclerView3.setAdapter(bAdapter3);
    }
}
