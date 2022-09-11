package com.tigan_lab.customer.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.tigan_lab.customer.ModelClass.AcSecondModelClass;
import com.tigan_lab.easy_clean.R;

public class AcSecondRecycleAdapter extends RecyclerView.Adapter<AcSecondRecycleAdapter.MyViewHolder> {

    Context context;


    private List<AcSecondModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView title,subtitle;
        LinearLayout linear;


        public MyViewHolder(View view) {
            super(view);

            subtitle = (TextView) view.findViewById(R.id.subtitle);
            title = (TextView) view.findViewById(R.id.title);
            linear = (LinearLayout) view.findViewById(R.id.linear);


        }

    }


    public AcSecondRecycleAdapter(Context context, List<AcSecondModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public AcSecondRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ac_second_list, parent, false);


        return new AcSecondRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final AcSecondModelClass lists = OfferList.get(position);
        holder.subtitle.setText(lists.getSubtitle());
        holder.title.setText(lists.getTitle());



    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


