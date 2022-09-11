package com.tigan_lab.customer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tigan_lab.easy_clean.R;

public class BridalMakupOrderSuccessActivity extends AppCompatActivity {


    TextView title,number,number1;
    ImageView back_img,search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridal_makup_order_success);

        title = findViewById(R.id.title);
        title.setText("Bridal Makeup Artist");

        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
