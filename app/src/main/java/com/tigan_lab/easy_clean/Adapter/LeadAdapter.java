package com.tigan_lab.easy_clean.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.easy_clean.Activity.ServiceDetails;
import com.tigan_lab.Session_management;
import com.tigan_lab.easy_clean.ModelClass.LeadModelClass;
import com.tigan_lab.easy_clean.R;

import java.util.List;

public class LeadAdapter extends RecyclerView.Adapter<LeadAdapter.MyViewHolder> {

    private List<LeadModelClass> moviesList;
    Context context;
    double dist;
    Session_management session_management;
    public LeadAdapter(Context context, List<LeadModelClass> list) {
        this.context=context;
        this.moviesList=list;
        session_management=new Session_management(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,credits,bookingDte1,Date2,address,distance,tdate;
        CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tname);
            credits = (TextView) view.findViewById(R.id.credits);
            bookingDte1 = (TextView) view.findViewById(R.id.timee1);
            Date2 = (TextView) view.findViewById(R.id.timee2);
            address = (TextView) view.findViewById(R.id.addresss);
            distance = (TextView) view.findViewById(R.id.distance);
            tdate= (TextView) view.findViewById(R.id.tdate);
            cardView=  view.findViewById(R.id.cadView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_leads_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final LeadModelClass movie = moviesList.get(position);
        holder.name.setText(movie.getUser_name());
        holder.credits.setText(movie.getCoins()+" credits");
        holder.bookingDte1.setText(movie.getConfirmed_on()+","+movie.getConfirmed_on());
        holder.Date2.setText(movie.getTime_slot());
        holder.address.setText(movie.getUser_address());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ServiceDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("booking_id",moviesList.get(position).getBooking_id());
                context.startActivity(intent);
            }
        });

        try {
            String totalOff = String.valueOf(Double.parseDouble(movie.getDistance()));

            CharSequence foo = totalOff;
            String bar = foo.toString();
            final String desiredString = bar.substring(0, 6);

            holder.distance.setText(desiredString + " Kms.");
        }catch (Exception e){

        }




    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}