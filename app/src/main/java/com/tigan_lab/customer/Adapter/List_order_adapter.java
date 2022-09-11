package com.tigan_lab.customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.Session_management;
import com.tigan_lab.customer.ModelClass.List_order_model;
import com.tigan_lab.easy_clean.R;

import java.util.List;

public class List_order_adapter extends RecyclerView.Adapter<List_order_adapter.MyViewHolder> {
List<List_order_model> list;
    Context context;



    Session_management session_management;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView price;
        TextView title,quantity;



        public MyViewHolder(View view) {
            super(view);


            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            quantity=(TextView) view.findViewById(R.id.quty);


        }

    }


    public List_order_adapter(Context context, List<List_order_model> list) {

        this.list = list;

        session_management=new Session_management(context);

        this.context = context;

    }

    @Override
    public List_order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_success, parent, false);


        return new List_order_adapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull List_order_adapter.MyViewHolder holder, final int position) {

        List_order_model getlist = list.get(position);

        holder.title.setText(getlist.getService_name());



        holder.price.setText( context.getResources().getString(R.string.currency)+getlist.getService_price());

        holder.quantity.setText(getlist.getQty());







    }



    @Override
    public int getItemCount() {
        return list.size();

    }

}

