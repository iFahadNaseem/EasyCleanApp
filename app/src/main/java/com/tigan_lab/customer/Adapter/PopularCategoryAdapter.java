package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Extra.Config.IMAGE_URL;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.tigan_lab.customer.ModelClass.CategoryModel;
import com.tigan_lab.customer.PopularServicesActivity;
import com.tigan_lab.customer.fragment.HomeFragment;
import com.tigan_lab.easy_clean.R;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.util.List;


public class PopularCategoryAdapter extends RecyclerView.Adapter<PopularCategoryAdapter.MyViewHolder> {

    private final List<CategoryModel> cateList;
    Context context;
    Class<HomeFragment> homeFragmentClass;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ProdNAme;
        ImageView icons1;

        public MyViewHolder(View view) {
            super(view);

            ProdNAme = view.findViewById(R.id.ProdName);
            icons1 = view.findViewById(R.id.icons1);

        }
    }
    public PopularCategoryAdapter(Context context, List<CategoryModel> cateList, Class<HomeFragment> homeFragmentClass) {
        this.context=context;
        this.cateList=cateList;
        this.homeFragmentClass=homeFragmentClass;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_homeproduct_r1, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CategoryModel cc = cateList.get(position);
        holder.ProdNAme.setText(cc.getpName());
        Picasso.with(context).load(IMAGE_URL + cc.getpImage()).error(R.drawable.ic_about).into(holder.icons1);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PopularServicesActivity.class);
            intent.putExtra("categoryid",cateList.get(position).getId());
            intent.putExtra("child_category_id",cateList.get(position).getPchildcatId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cateList.size();
    }

}
