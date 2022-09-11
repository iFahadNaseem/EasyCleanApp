package com.tigan_lab.easy_clean.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tigan_lab.easy_clean.R;

public class StartJob extends AppCompatActivity {

    Dialog progressDialog;
    String bookingID,userName,userPhone,userAddress;
    TextView name,Phone,location,BtnStartjob;
    EditText Codee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_job);
        inint();
    }

    private void inint() {
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        bookingID=getIntent().getStringExtra("booking_id");
        userName=getIntent().getStringExtra("user_name");
        userPhone=getIntent().getStringExtra("user_phone");
        userAddress=getIntent().getStringExtra("user_address");

        BtnStartjob=findViewById(R.id.btnStartJOb);
        name=findViewById(R.id.tname);
        Phone=findViewById(R.id.tPhone);
        location=findViewById(R.id.tAddress);

        name.setText(userName);
        Phone.setText(userPhone);
        location.setText(userAddress);

        BtnStartjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(),BookingConfirm.class);
                intent.putExtra("bId",bookingID);
                startActivity(intent);
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}