package com.tigan_lab.easy_clean.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tigan_lab.easy_clean.Constants.Config;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AfterGstPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Dialog progressDialog;
    Session_management sessionManagement;
    ImageView back;
    EditText RegstrdNO,userName,gender,dadName,address,panCard;
    LinearLayout SAVE,Updtae;
    TextView dob;
    String partnerID,layout;
    ImageView backImg,frontImg;
    Spinner spinner;
    String[] country = {"Select Type","PassPort", "CNIC"};
    private static final int  RESULT_LOAD_IMAGE = 1;
    private static final int  RESULT_LOAD_IMAGE1 = 10;
    Bitmap bitmap,bitmap1;
    Uri filePath,filePath1;
    String encodedImage,encodedImage1;
    CardView cback,cfront;
    private String getdate = "";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gst_page);
        niiit();
    }

    private void niiit() {
        sessionManagement = new Session_management(this);
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        partnerID = sessionManagement.userId();
        layout = getIntent().getStringExtra("id");

        cback = findViewById(R.id.cBack);
        cfront = findViewById(R.id.cfront);
        back = findViewById(R.id.back);
        RegstrdNO = findViewById(R.id.RegstrdNo);
        userName = findViewById(R.id.Nmae);
        dob = findViewById(R.id.Dob);
        gender = findViewById(R.id.gendertypee);
        dadName = findViewById(R.id.dadName);
        address = findViewById(R.id.pAddress);
        frontImg = findViewById(R.id.frntImg);
        backImg = findViewById(R.id.bckImg);


        spinner= (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        SAVE = findViewById(R.id.SAVE);
        Updtae = findViewById(R.id.Updtae);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdate();
            }
        });

        cfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser1();
            }
        });

        cback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser2();
            }
        });

        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RegstrdNO.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter CNIC/PAN number!", Toast.LENGTH_SHORT).show();
                } else if (userName.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter your registered Name!", Toast.LENGTH_SHORT).show();
                } else if (dob.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter your registered Date of Birth!", Toast.LENGTH_SHORT).show();
                } else if (gender.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter your Gender type!", Toast.LENGTH_SHORT).show();
                } else if (dadName.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter your Father's Name!", Toast.LENGTH_SHORT).show();
                } else if (address.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter your registered address!", Toast.LENGTH_SHORT).show();
                }else if (frontImg.getDrawable()==null) {
                    Toast.makeText(getApplicationContext(), "Upload front Image of Id proof!", Toast.LENGTH_SHORT).show();
                }else if (backImg.getDrawable()==null) {
                    Toast.makeText(getApplicationContext(), "Upload back Image of Id proof!", Toast.LENGTH_SHORT).show();
                } else if (!isOnline()) {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    gstAddUrl(encodedImage,encodedImage1);
                }
            }
        });

        if (layout != null) {
            Updtae.setVisibility(View.VISIBLE);
            SAVE.setVisibility(View.GONE);
            Updtae.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (RegstrdNO.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter GST/PAN number!", Toast.LENGTH_SHORT).show();
                    } else if (userName.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter your registered Name!", Toast.LENGTH_SHORT).show();
                    } else if (dob.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter your registered Date of Birth!", Toast.LENGTH_SHORT).show();
                    } else if (gender.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter your Gender type!", Toast.LENGTH_SHORT).show();
                    } else if (dadName.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter your Father's Name!", Toast.LENGTH_SHORT).show();
                    } else if (address.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter your registered address!", Toast.LENGTH_SHORT).show();
                    } else if (!isOnline()) {
                        Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                    } else {
                        updateUrl(encodedImage,encodedImage1);
                    }
                }
            });
        }
    }

    private void gstAddUrl(final String encodedImage,final String encodedImage1) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.gstAadd, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("gstAadd",response);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")){

                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(getApplicationContext(), AfterBankEdit.class);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                            Updtae.setVisibility(View.GONE);
                            SAVE.setVisibility(View.VISIBLE);
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

                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> param = new HashMap<>();
                    param.put("partner_id",partnerID);
                    param.put("identify_proof",spinner.getSelectedItem().toString());
                    param.put("voter_card_number",RegstrdNO.getText().toString());
                    param.put("gender",gender.getText().toString());
                    param.put("d_o_b",dob.getText().toString());
                    param.put("c_o",dadName.getText().toString());
                    param.put("permanent_add",address.getText().toString());
                    param.put("gst_name",userName.getText().toString());
                    param.put("gst_number",RegstrdNO.getText().toString());
                    param.put("front_image",encodedImage);
                    param.put("back_image",encodedImage1);
                    Log.d("encodedImage",encodedImage);
                    Log.d("encodedImage1",encodedImage1);
                    Log.d("spinner",spinner.getSelectedItem().toString());
                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }


    private void updateUrl(final String encodedImage,final String encodedImage1) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.gstUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("updateUrl",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){

                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
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
                HashMap<String,String> param = new HashMap<>();
                param.put("partner_id",partnerID);
                param.put("identify_proof",spinner.getSelectedItem().toString());
                param.put("voter_card_number",RegstrdNO.getText().toString());
                param.put("gender",gender.getText().toString());
                param.put("d_o_b",dob.getText().toString());
                param.put("c_o",dadName.getText().toString());
                param.put("permanent_add",address.getText().toString());
                param.put("gst_name",userName.getText().toString());
                param.put("gst_number",RegstrdNO.getText().toString());
                param.put("front_image",encodedImage);
                param.put("back_image",encodedImage1);
                Log.d("encodedImage",encodedImage);
                Log.d("encodedImage1",encodedImage1);

                Log.d("spinner",spinner.getSelectedItem().toString());
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void showFileChooser1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }
    private void showFileChooser2() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE) {

            filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Picasso.with(getApplicationContext()).load(String.valueOf(bitmap)).into(frontImg);


            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            frontImg.setImageBitmap(bitmap);

        }

        if (requestCode == RESULT_LOAD_IMAGE1) {


            filePath1= data.getData();
            try {

                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);


                Picasso.with(getApplicationContext()).load(String.valueOf(bitmap1)).into(backImg);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();


            encodedImage1 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            backImg.setImageBitmap(bitmap1);

        }

    }

    private void getdate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        getdate = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        dob.setText(getdate);

                        try {
                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                            Date date = inputFormat.parse(getdate);
                            String str = outputFormat.format(date);

                            dob.setText(str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            dob.setText(getdate);
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

}