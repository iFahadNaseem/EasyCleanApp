package com.tigan_lab.customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.ForgotPassActivity;
import com.tigan_lab.customer.LoginActivity;
import com.tigan_lab.customer.ModelClass.SelectCountryModelClass;
import com.tigan_lab.customer.SignUpActivity;
import com.tigan_lab.easy_clean.R;

import java.util.List;


public class SelectCountryRecycleAdapter extends RecyclerView.Adapter<SelectCountryRecycleAdapter.MyViewHolder> {

    Context context;


    private List<SelectCountryModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView country_name, country_code;
        LinearLayout selectedCountry;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            country_name = (TextView) view.findViewById(R.id.country_name);
            country_code = (TextView) view.findViewById(R.id.country_code);
            selectedCountry = view.findViewById(R.id.selected_country);


        }

    }


    public SelectCountryRecycleAdapter(Context context, List<SelectCountryModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public SelectCountryRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_coutnry_list, parent, false);


        return new SelectCountryRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final SelectCountryModelClass lists = OfferList.get(position);
        holder.image.setImageResource(lists.getImage());
        holder.country_name.setText(lists.getCountry_name());
        holder.country_code.setText(lists.getCountry_code());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof LoginActivity){
                    ((LoginActivity)context).selectedCountry(lists.getImage(),lists.getCountry_code());
                }
                if (context instanceof SignUpActivity){
                    ((SignUpActivity)context).selectedCountry(lists.getImage(),lists.getCountry_code());
                }
                if (context instanceof ForgotPassActivity){
                    ((ForgotPassActivity)context).selectedCountry(lists.getImage(),lists.getCountry_code());
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


