package com.tigan_lab.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Adapter.AddonShowadapter;
import com.tigan_lab.customer.Adapter.Blog_on_adapter;
import com.tigan_lab.customer.Adapter.Cart_adapter;

import com.tigan_lab.customer.Extra.Config;
import com.tigan_lab.customer.Extra.DatabaseHandler;
import com.tigan_lab.customer.ModelClass.Add_on_model;
import com.tigan_lab.easy_clean.R;

import at.grabner.circleprogress.CircleProgressView;

import static com.tigan_lab.customer.Extra.Config.Finalam;
import static com.tigan_lab.customer.Extra.Config.cat_id_json_array;

public class Cart_Activity extends AppCompatActivity {
    ArrayList<String> title_array = new ArrayList<String>();
    ArrayList<String> notice_array = new ArrayList<String>();
    TextView title,number,number1,type,addon;
    ImageView back_img,search;
    private DatabaseHandler db;

    SharedPreferences sharedPreferences;
    String getamount;
    RecyclerView rv_list;


    public static TextView total,totalprice,txtAddon;

    ImageView add,add1,minus,minus1;
    LinearLayout linear_add,linear_add1,linear_count1,linear_count,bottom_linear;

    TextView tv_coupon;
    Button button;
    int count=1,adult=1;
String couponid;
    Session_management session_management;
    int layout;
    AddonShowadapter adapter1;
    String addonamount,addOnname;
    CardView CardViewOffer;
    CircleProgressView circleProgressView;
    Blog_on_adapter subCategoryPagerAdapter;
    public  static RecyclerView recyclerAddons;
    private List<Add_on_model> liatt = new ArrayList<>();

    String am,finalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cleaning_three);

        session_management=new Session_management(this);
        db=new DatabaseHandler(this);

        linear_add = findViewById(R.id.linear_add);
        linear_count = findViewById(R.id.linear_count);
        add = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        number = findViewById(R.id.number);

        totalprice=findViewById(R.id.totalprice);
        rv_list=findViewById(R.id.rc_list);
        type = findViewById(R.id.type);
        type.setVisibility(View.GONE);
        title = findViewById(R.id.title);
        CardViewOffer=findViewById(R.id.CardViewOffer);

        recyclerAddons=findViewById(R.id.recyclerAddons);
        tv_coupon=findViewById(R.id.tv_coupon);
        total=findViewById(R.id.total);
        txtAddon=findViewById(R.id.taddon);
        addon=findViewById(R.id.insd);
        couponid=getIntent().getStringExtra("couponid");
        getamount=getIntent().getStringExtra("amount");
        finalAmount=getIntent().getStringExtra("remainingAmount");

        am=db.getTotalAmount();
        sharedPreferences=getSharedPreferences("value",0);
        addonamount=sharedPreferences.getString("addon","0");


        if(addonamount!=null){
            txtAddon.setText(getResources().getString(R.string.currency)+addonamount);
        }else {
            txtAddon.setText(getResources().getString(R.string.currency)+"0");
        }



        if(addonamount!=null){//&& cat_id_json_array!=null && cat_id_json_array.length()>0
            txtAddon.setText(getResources().getString(R.string.currency)+addonamount);

            int databasee= Integer.valueOf(am);
            int addamountt= Integer.valueOf(addonamount);
            getamount= String.valueOf(databasee+addamountt);
            Config.Totalamount=getamount;

            totalprice.setText(getResources().getString(R.string.currency)+Config.Totalamount);
        }else {
            txtAddon.setText(getResources().getString(R.string.currency)+"0");
        }
        if (finalAmount != null) {
            totalprice.setText(getResources().getString(R.string.currency) + finalAmount);
            Finalam=finalAmount;
            tv_coupon.setText("Coupon Applied!");


        }else {
            totalprice.setText(getResources().getString(R.string.currency) + Config.Totalamount);
            Finalam= Config.Totalamount;

        }


        Config.Coupocode=couponid;
        total.setText(getResources().getString(R.string.currency)+am);

        Intent i=getIntent();

        layout = i.getIntExtra("layout",0);

        rv_list.setLayoutManager(new LinearLayoutManager(Cart_Activity.this));

        ArrayList<HashMap<String, String>> map = db.getCartAll();

        Cart_adapter adapter = new Cart_adapter(Cart_Activity.this, map);
        rv_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();



if(cat_id_json_array!=null){
    liatt.clear();
    for (int j = 0; j < cat_id_json_array.length(); j++) {

        try {

            JSONObject obj = cat_id_json_array.getJSONObject(j);

            String id = obj.getString("addon_id");
            String name = obj.getString("addon_name");
            String price = obj.getString("addon_price");

            if (id.equalsIgnoreCase("")) {
                recyclerAddons.setVisibility(View.GONE);
            } else {
                Add_on_model homeC1 = new Add_on_model(name);
                liatt.add((homeC1));
                recyclerAddons.setVisibility(View.VISIBLE);
                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(Cart_Activity.this);
                recyclerAddons.setLayoutManager(layoutManager1);
                recyclerAddons.setItemAnimator(new DefaultItemAnimator());
                AddonShowadapter adapter1 = new AddonShowadapter(Cart_Activity.this, liatt);
                recyclerAddons.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
        CardViewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_Activity.this,OffersActivity.class);
                intent.putExtra("totlam",Config.Totalamount);
                startActivity(intent);
            }
        });

        title.setText("Cart");


            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkorange));
            circleProgressView.setValue(Float.parseFloat("42"));

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

                     Intent intent = new Intent(Cart_Activity.this,SelectAddressActivity.class);
                    startActivity(intent);
            }
        });
    }
}
