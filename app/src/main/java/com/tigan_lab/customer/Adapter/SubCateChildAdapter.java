package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Extra.Config.IMAGE_URL;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.ModelClass.SubCatChildModelclass;
import com.tigan_lab.customer.ServicesListActivity;
import com.tigan_lab.easy_clean.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCateChildAdapter extends RecyclerView.Adapter<SubCateChildAdapter.MyViewHolder> {

    Context context;
    private List<SubCatChildModelclass> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView imageView;
        LinearLayout headerlayout;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView1);
            imageView =  view.findViewById(R.id.imageView1);
            headerlayout=  view.findViewById(R.id.headerlayout);
        }
    }


    public SubCateChildAdapter(Context context,List<SubCatChildModelclass> moviesList) {
        this.context=context;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_header, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubCatChildModelclass movie = moviesList.get(position);
        holder.title.setText(movie.getChild_name());

        Picasso.with(context).load(IMAGE_URL + movie.getChild_img()).error(R.drawable.ic_about).into(holder.imageView);

        holder.headerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ServicesListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("childCat_id",movie.getChild_category_id());
                intent.putExtra("SubCat_id",movie.getSub_category_id());
                intent.putExtra("SubCatNAme",movie.getChild_name());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
