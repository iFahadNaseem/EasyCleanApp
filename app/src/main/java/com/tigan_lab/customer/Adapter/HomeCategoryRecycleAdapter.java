package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Extra.Config.IMAGE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.ModelClass.HomeCateModelClass;
import com.tigan_lab.customer.SubCategoryActivity;
import com.tigan_lab.easy_clean.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class HomeCategoryRecycleAdapter extends RecyclerView.Adapter<HomeCategoryRecycleAdapter.MyViewHolder> {

    Context context;
    private final List<HomeCateModelClass> OfferList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public MyViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);

        }
    }
    public HomeCategoryRecycleAdapter(Context context, List<HomeCateModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @NotNull
    @Override
    public HomeCategoryRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_homeprod_r2, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final HomeCateModelClass lists = OfferList.get(position);

        Picasso.with(context).load(IMAGE_URL + lists.getCategory_image()).error(R.drawable.ic_about).into(holder.image);
        holder.title.setText(lists.getCategory_name());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubCategoryActivity.class);
            intent.putExtra("categoryid",lists.getCategory_id());
            intent.putExtra("layout",position);
            Log.d("pos", String.valueOf(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return OfferList.size(); }
}




