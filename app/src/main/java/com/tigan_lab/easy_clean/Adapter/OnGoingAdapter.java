package com.tigan_lab.easy_clean.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.easy_clean.ModelClass.HistoryModelClass;
import com.tigan_lab.easy_clean.R;

import java.util.List;

public class OnGoingAdapter extends RecyclerView.Adapter<OnGoingAdapter.MyViewHolder> {

    private List<HistoryModelClass> moviesList;
    Context context;
    public OnGoingAdapter(Context context, List<HistoryModelClass> list) {
        this.context=context;
        this.moviesList=list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,credits,Date2,address;
        LinearLayout llcancl;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tname);
            credits = (TextView) view.findViewById(R.id.credits);
            Date2 = (TextView) view.findViewById(R.id.timee);
            address = (TextView) view.findViewById(R.id.addresss);
            llcancl =  view.findViewById(R.id.llcancl);
        }
    }


    public OnGoingAdapter(List<HistoryModelClass> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_bookinglist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final HistoryModelClass movie = moviesList.get(position);

        holder.llcancl.setVisibility(View.GONE);

        holder.name.setText(movie.getUser_name());
        holder.credits.setText(movie.getCoins()+" credits");
        holder.Date2.setText(movie.getConfirmed_on()+", "+movie.getTime_slot());
        holder.address.setText(movie.getUser_address());


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}