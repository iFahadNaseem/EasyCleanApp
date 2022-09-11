package com.tigan_lab.customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.ModelClass.Blog_on_model;
import com.tigan_lab.customer.ModelClass.ServiceDetalisModelClass;
import com.tigan_lab.easy_clean.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class Service_heading_adapter extends RecyclerView.Adapter<Service_heading_adapter.MyViewHolder> {

    Context context;



    private List<ServiceDetalisModelClass> OfferList;
    private List<Blog_on_model> bloglist=new ArrayList<>();



    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView title,point;

        RecyclerView rc_list;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.heading);

            rc_list=view.findViewById(R.id.rc_list);


        }

    }


    public Service_heading_adapter(Context context, List<ServiceDetalisModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public Service_heading_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_serice_des, parent, false);


        return new Service_heading_adapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull Service_heading_adapter.MyViewHolder holder, final int position) {
        final ServiceDetalisModelClass lists = OfferList.get(position);

        holder.title.setText(lists.getBlog_heading());



        try {

            JSONArray audtioarray = new JSONArray(lists.getBlog_point());



            bloglist.clear();
            for (int i=0;i<audtioarray.length();i++){

                Blog_on_model blog_on_model=new Blog_on_model();

                blog_on_model.setPoints(audtioarray.getString(i));

                bloglist.add(blog_on_model);

            }

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

            holder.rc_list.setLayoutManager(layoutManager);

            holder.rc_list.setItemAnimator(new DefaultItemAnimator());


            Blog_on_adapter blog_on_adapter=new Blog_on_adapter(context,bloglist);

            holder.rc_list.setAdapter(blog_on_adapter);

            blog_on_adapter.notifyDataSetChanged();



        } catch (JSONException e) {
            e.printStackTrace();
        }




    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}



