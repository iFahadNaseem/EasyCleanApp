package com.tigan_lab.customer.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.Cart_Activity;

import java.util.List;

import com.tigan_lab.customer.Extra.DatabaseHandler;
import com.tigan_lab.Session_management;
import com.tigan_lab.customer.ModelClass.Add_on_model;
import com.tigan_lab.easy_clean.R;

public class AddonShowadapter extends RecyclerView.Adapter<AddonShowadapter.ProductHolder> {
   List<Add_on_model> list;
    Activity activity;
    String Reward;
    SharedPreferences preferences;
    String language;

    Session_management session_management;
    int lastpostion;
    DatabaseHandler dbHandler;





    public AddonShowadapter(Cart_Activity activity, List<Add_on_model> ss) {
        this.activity=activity;
        this.list=ss;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addons_addlist, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {

        Add_on_model sas=list.get(position);
        holder.tv_title.setText(sas.getAdd_on_des());


      }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_price,tv_contetiy;
        public ImageView iv_plus, iv_minus;

        public ProductHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}



