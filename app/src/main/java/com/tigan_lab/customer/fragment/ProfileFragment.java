package com.tigan_lab.customer.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.AboutUsActivity;
import com.tigan_lab.customer.Adapter.ProfileRecycleAdapter;
import com.tigan_lab.customer.EditProfileActivity;
import com.tigan_lab.customer.Extra.Config;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.FAQmain_Activity;
import com.tigan_lab.customer.Manage_address;
import com.tigan_lab.customer.ModelClass.HomeCategoryModelClass;
import com.tigan_lab.customer.NotificationActivity;
import com.tigan_lab.customer.TermsConditionActivity;
import com.tigan_lab.easy_clean.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private View view;

    private ArrayList<HomeCategoryModelClass> homeCategoryModelClasses;
    private RecyclerView recyclerView;
    private ProfileRecycleAdapter bAdapter;

    private Integer image[] = {R.drawable.ic_notifications,R.drawable.ic_booking,R.drawable.ic_manage_address,R.drawable.ic_help,
    R.drawable.ic_rate_us,R.drawable.ic_share,R.drawable.ic_about,R.drawable.ic_t_and_c};
    private String title[] = {"Notification","My Bookings","Manage Address","Help","Rate us","Share App","About Rapid",
            "Terms & Conditions"};

    Session_management sessionManagement;
    String userId;
    TextView tname,tmob,temail,signOut,tEdit;
    CircleImageView pImage;
    LinearLayout notification,myBooking,myAddress,Faq,RAteUs,shareApp,aboutUs,Termsconditions;
    Dialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        sessionManagement=new Session_management(getContext());
       userId= sessionManagement.userId();
        Log.d("dfgh",userId);

        progressDialog = new Dialog(requireActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        progressDialog.setContentView(R.layout.custom_dialog_progress);
        tEdit= view.findViewById(R.id.tEdit);
        signOut = view.findViewById(R.id.signOut);
        notification = view.findViewById(R.id.llNotice);
        myBooking = view.findViewById(R.id.llBooking);
        myAddress = view.findViewById(R.id.llAdrress);
        Faq = view.findViewById(R.id.llFaq);
        RAteUs= view.findViewById(R.id.llrateUs);
        shareApp = view.findViewById(R.id.llShareApp);
        aboutUs = view.findViewById(R.id.llaboutUs);
        Termsconditions = view.findViewById(R.id.lltC);

        tname = view.findViewById(R.id.tName);
        temail = view.findViewById(R.id.tEmail);
        tmob = view.findViewById(R.id.tMob);
        pImage = view.findViewById(R.id.pImg);
        recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


       notification.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getContext(), NotificationActivity.class);
               startActivity(intent);
           }
       });
        myBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment = new BookingFragment();
                loadFragment(fragment);

            }
        });
        myAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Manage_address.class);
                startActivity(intent);
            }
        });

        Faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), FAQmain_Activity.class);
                startActivity(intent);
            }
        });
        RAteUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaystoreApp();
            }
        });

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               appShare();
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AboutUsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Termsconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), TermsConditionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                sessionManagement.logoutSession();
            }
        });

        tEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        if(isOnline()){
            userProfileUrl();
        }else{
            Toast.makeText(getActivity(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
        }
        return  view;
    }

    private void appShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey there, I am using. \uD83D\uDC4B\uD83C\uDFFC \n" +
                "Link:" + " http://play.google.com/store/apps/details?id=" + getActivity().getPackageName()); //getPackageName()
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    private void PlaystoreApp() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }
    private void userProfileUrl() {
        progressDialog.show();
    String tag_json_obj = "json_category_req";
    Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userId);
    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
            Config.Profile, params, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            Log.d("TAG", response.toString());
            progressDialog.dismiss();
            try {

                String status = response.getString("status");
                String message = response.getString("message");

                if (status.contains("1")) {

                        JSONObject obj = response.getJSONObject("data");
                        String user_id = obj.getString("id");
                        String user_fullname = obj.getString("user_name");
                        String user_email = obj.getString("user_email");
                        String user_phone = obj.getString("user_phone");
                    String user_image = obj.getString("user_image");

                    Picasso.with(getContext()).load(Config.IMAGE_URL+user_image).error(R.drawable.logo_rapid).into(pImage);

                    temail.setText(user_email);
                        tname.setText(user_fullname);
                        tmob.setText(user_phone);

                    }}
            catch (Exception e) {
                        e.printStackTrace();
                    }
progressDialog.dismiss();
                }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            VolleyLog.d("TAG", "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getContext(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        }
    });

    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

}
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void onResume() {
        super.onResume();
        userProfileUrl();
    }

}