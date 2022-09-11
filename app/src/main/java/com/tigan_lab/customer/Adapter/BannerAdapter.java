package com.tigan_lab.customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tigan_lab.easy_clean.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerImage> {


    private Context context;
    private ArrayList<String> images;

    public BannerAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public BannerAdapter.BannerImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false);
        return new BannerImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.BannerImage holder, int position) {
     Picasso.with(context).load(images.get(position)).error(R.drawable.ic_about).into(holder.img);

    }

    @Override
    public int getItemCount() {
        if (images.size() > 0) {
            return images.size();
        } else {
            return 0;
        }

    }

    public static class BannerImage extends RecyclerView.ViewHolder {

        private ImageView img;

        public BannerImage(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.banner_image);
        }

    }
}
