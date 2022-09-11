package com.tigan_lab.easy_clean.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.easy_clean.Constants.Config;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.ModelClass.withdrawModelClass;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawalAdapter extends RecyclerView.Adapter<WithdrawalAdapter.MyViewHolder> {

    private List<withdrawModelClass> moviesList;
    Context context;
    String partnerId;
    Session_management sessionManagement;
    public WithdrawalAdapter(Context context, List<withdrawModelClass> list) {
        this.context=context;
        this.moviesList=list;
        this.sessionManagement = new Session_management(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,credits,Date2,address;
        LinearLayout llcancl;
        Button send;

        public MyViewHolder(View view) {
            super(view);
            sessionManagement = new Session_management(context);

            partnerId=sessionManagement.userId();
            name = (TextView) view.findViewById(R.id.tname);
            credits = (TextView) view.findViewById(R.id.credits);
            Date2 = (TextView) view.findViewById(R.id.timee);
            address = (TextView) view.findViewById(R.id.addresss);
            llcancl =  view.findViewById(R.id.llcancl);
            send =  view.findViewById(R.id.btnSned);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_bookinglist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final withdrawModelClass movie = moviesList.get(position);

        holder.llcancl.setVisibility(View.VISIBLE);

        holder.name.setText(movie.getUser_name());
        holder.credits.setText(movie.getCoins()+" credits");
        holder.Date2.setText(movie.getConfirmed_on()+", "+movie.getTime_slot());

        if(movie.getComplete().equalsIgnoreCase("0")){
            holder.address.setText("Processing...");
            holder.address.setTextColor(context.getResources().getColor(R.color.red));
            holder.send.setVisibility(View.GONE);

        }else if(movie.getComplete().equalsIgnoreCase("1")) {
            holder.address.setText("Payment Completed...");
            holder.address.setTextColor(context.getResources().getColor(R.color.orange));
            holder.send.setVisibility(View.GONE);
        }
        else if(movie.getComplete().equalsIgnoreCase("2")) {
            holder.address.setText("Approved...");
            holder.address.setTextColor(context.getResources().getColor(R.color.orange));
            holder.send.setVisibility(View.GONE);

        }
        else if(movie.getComplete().equalsIgnoreCase("")) {
            holder.send.setVisibility(View.VISIBLE);
            holder.address.setVisibility(View.GONE);
        }
        else if(movie.getComplete().equalsIgnoreCase("null")) {
            holder.send.setVisibility(View.VISIBLE);
            holder.address.setVisibility(View.GONE);

        }
        else {
            holder.send.setVisibility(View.VISIBLE);
            holder.address.setVisibility(View.GONE);

        }
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hitUrl(movie.getBooking_id(),partnerId);
            }

            private void hitUrl(final String booking_id, final String partnerId) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SEND_REQ, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Login",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("0")){

                                holder.send.setText("Processing...");
                                showDialog();
                                hitUrl(movie.getBooking_id(),partnerId);
                            }
                            else if(status.equalsIgnoreCase("1")) {
                                holder.send.setText("Processing...");
                                showDialog1();
                                hitUrl(movie.getBooking_id(),partnerId);
                            }
                            else if(status.equalsIgnoreCase("2")) {
                                showDialog2();
                                hitUrl(movie.getBooking_id(),partnerId);
                            }else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                        param.put("partner_id",partnerId);
                        param.put("booking_id",booking_id);

                        return param;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.getCache().clear();
                requestQueue.add(stringRequest);


            }
        });
    }
    private void showDialog2() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Please enter UPI/Paypal Id for payment!");

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        alertDialog.show();


    }


    private void showDialog1() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(true);
        alertDialog.setMessage("Withdraw request sent successfully!");

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        alertDialog.show();


    }

    private void showDialog() {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setCancelable(true);
            alertDialog.setMessage("Withdraw request already sent!");

            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });

            alertDialog.show();


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}