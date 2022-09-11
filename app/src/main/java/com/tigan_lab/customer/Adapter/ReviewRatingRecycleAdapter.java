package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Extra.Config.IMAGE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.ModelClass.Rating_review_model;
import com.tigan_lab.easy_clean.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ReviewRatingRecycleAdapter extends RecyclerView.Adapter<ReviewRatingRecycleAdapter.MyViewHolder> {
    Context context;


    private List<Rating_review_model> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView rating,tv_reviewhead,tv_reviewdes,username;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            rating = (TextView) view.findViewById(R.id.rating);
            tv_reviewhead=view.findViewById(R.id.review_head);
            tv_reviewdes=view.findViewById(R.id.review_description);
            username=view.findViewById(R.id.username);




        }

    }


    public ReviewRatingRecycleAdapter(Context context, List<Rating_review_model> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public ReviewRatingRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review_rating_list, parent, false);


        return new ReviewRatingRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Rating_review_model lists = OfferList.get(position);

        holder.tv_reviewhead.setText(lists.getReview_head());
        holder.tv_reviewdes.setText(lists.getReview_desc());
        holder.username.setText(lists.getUser_name());

        holder.rating.setText(lists.getRating());

        Picasso.with(context).load(IMAGE_URL + lists.getUser_image()).error(R.drawable.ic_about).into(holder.image);

    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


