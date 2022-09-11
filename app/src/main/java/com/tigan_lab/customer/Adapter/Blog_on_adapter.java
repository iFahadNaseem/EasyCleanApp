package com.tigan_lab.customer.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.tigan_lab.customer.ModelClass.Blog_on_model;
import com.tigan_lab.easy_clean.R;


public class Blog_on_adapter extends RecyclerView.Adapter<Blog_on_adapter.MyViewHolder> {

    Context context;



    private List<Blog_on_model> OfferList;



    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView title,point;


        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.tv_name);


        }

    }


    public Blog_on_adapter(Context context, List<Blog_on_model> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public Blog_on_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_name, parent, false);


        return new Blog_on_adapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull Blog_on_adapter.MyViewHolder holder, final int position) {
        final Blog_on_model lists = OfferList.get(position);

        holder.title.setText(lists.getPoints());






    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}



