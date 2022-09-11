package com.tigan_lab.customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.ModelClass.Cancel_bookin_model;
import com.tigan_lab.easy_clean.R;

import java.util.List;

public class Cancelbookin_adapter extends RecyclerView.Adapter<Cancelbookin_adapter.MyViewHolder> {

    Context context;

    boolean showingfirst = true;
    int myPos = 0;

    String getlist;
    private List<Cancel_bookin_model> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView time;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);


            time = (TextView) view.findViewById(R.id.list);
            linear = (LinearLayout) view.findViewById(R.id.linear);


        }

    }


    public Cancelbookin_adapter(Context context, List<Cancel_bookin_model> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public Cancelbookin_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cancelbooking, parent, false);


        return new Cancelbookin_adapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final Cancelbookin_adapter.MyViewHolder holder, final int position) {
        final Cancel_bookin_model lists = OfferList.get(position);

        holder.time.setText(lists.getCancel_points());





              }




    public String getlistid() {
        return getlist;
    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}

