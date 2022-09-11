package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Extra.Config.IMAGE_URL;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.ModelClass.SubCatChildModelclass;
import com.tigan_lab.easy_clean.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CategoryHorizontalRecycleAdapter extends RecyclerView.Adapter<CategoryHorizontalRecycleAdapter.MyViewHolder> {

    Context context;

    private List<SubCatChildModelclass> OfferList;
    int myPos = 0;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView title;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            linear = (LinearLayout) view.findViewById(R.id.linear);

        }

    }


    public CategoryHorizontalRecycleAdapter(Context context, List<SubCatChildModelclass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public CategoryHorizontalRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_horizontal_list, parent, false);


        return new CategoryHorizontalRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final SubCatChildModelclass lists = OfferList.get(position);

        holder.title.setText(lists.getChild_name());
        Picasso.with(context).load(IMAGE_URL + lists.getChild_img()).error(R.drawable.ic_about).into(holder.image);



        if (myPos == position){
            holder.title.setTextColor(Color.parseColor("#0D3469"));//38393f
            holder.title.setBackgroundResource(R.drawable.rectangular_fullorange);

        }
        else {

            holder.title.setTextColor(Color.parseColor("#0D3469"));//acacac
            holder.title.setBackgroundResource(R.drawable.rectangular_black);

        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myPos=position;
                notifyDataSetChanged();


            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myPos=position;
                notifyDataSetChanged();


            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myPos=position;
                notifyDataSetChanged();

            }
        });
    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


