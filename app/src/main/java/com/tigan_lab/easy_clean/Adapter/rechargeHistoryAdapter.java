package com.tigan_lab.easy_clean.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.easy_clean.ModelClass.rechargeHistoryModel;
import com.tigan_lab.easy_clean.R;

import java.util.List;

public class rechargeHistoryAdapter extends RecyclerView.Adapter<rechargeHistoryAdapter.MyViewHolder> {

    private List<rechargeHistoryModel> moviesList;
    Context context;
    public rechargeHistoryAdapter(Context context, List<rechargeHistoryModel> list) {
        this.context=context;
        this.moviesList=list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,credits,Date2,type;
        TextView remianCredits,viewLead;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tname);
            credits = (TextView) view.findViewById(R.id.credits);
            Date2 = (TextView) view.findViewById(R.id.tdate);
            type = (TextView) view.findViewById(R.id.type);
            remianCredits =  view.findViewById(R.id.remianCredits);
            viewLead=  view.findViewById(R.id.viewLead);
        }
    }


    public rechargeHistoryAdapter(List<rechargeHistoryModel> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_earningslist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final rechargeHistoryModel movie = moviesList.get(position);


        holder.viewLead.setText("Rs."+movie.getPrice());
        holder.name.setText("Purchase Online");
        holder.credits.setText(movie.getCoins()+" credits");
        holder.Date2.setText(movie.getRecharge_date());
        holder.type.setText(movie.getStatus());



    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}