package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Cart_Activity.total;
import static com.tigan_lab.customer.Cart_Activity.totalprice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.Session_management;
import com.tigan_lab.customer.Extra.DatabaseHandler;
import com.tigan_lab.easy_clean.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart_adapter extends RecyclerView.Adapter<Cart_adapter.ProductHolder> {
    ArrayList<HashMap<String, String>> list;
    Activity activity;
    String Reward;
    SharedPreferences preferences;
    String language;

    Session_management session_management;
    int lastpostion;
    DatabaseHandler dbHandler;

    public Cart_adapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

        dbHandler = new DatabaseHandler(activity);
        session_management=new Session_management(activity);

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final HashMap<String, String> map_list = list.get(position);
        holder.tv_title.setText(map_list.get("service_name"));



        holder.tv_price.setText(activity.getResources().getString(R.string.currency)+map_list.get("service_price"));

        holder.tv_contetiy.setText(map_list.get("qty"));

        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = 0;

                if (!holder.tv_contetiy.getText().toString().equalsIgnoreCase(""))

                    count = Integer.valueOf(holder.tv_contetiy.getText().toString());

                if (count > 0) {

                    count = count - 1;

                    holder.tv_contetiy.setText(String.valueOf(count));


                    HashMap<String, String> map = new HashMap<>();

                    map.put("service_id", map_list.get("service_id"));
                    map.put("service_name", map_list.get("service_name"));
                    map.put("service_description", map_list.get("service_description"));
                    map.put("service_price", map_list.get("service_price"));
                    map.put("service_image", map_list.get("service_image"));


                    if (!holder.tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                        if (dbHandler.isInCart(map_list.get("service_id"))) {

                            dbHandler.setCart(map, Float.valueOf(holder.tv_contetiy.getText().toString()));

                        } else {

                            dbHandler.setCart(map, Float.valueOf(holder.tv_contetiy.getText().toString()));

                        }

                    } else {

                        dbHandler.removeItemFromCart(map_list.get("service_id"));
                    }

                    holder.tv_contetiy.setText(String.valueOf(count));
                    total.setText(activity.getResources().getString(R.string.currency)+dbHandler.getTotalAmount());

                    totalprice.setText(activity.getResources().getString(R.string.currency)+dbHandler.getTotalAmount());
                }

                if(count<1){
                    dbHandler.removeItemFromCart(map_list.get("service_id"));

                    list.remove(position);
                    notifyDataSetChanged();
                    total.setText(activity.getResources().getString(R.string.currency)+dbHandler.getTotalAmount());

                    totalprice.setText(activity.getResources().getString(R.string.currency)+dbHandler.getTotalAmount());

                }


            }

        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = 0;

                count = Integer.valueOf(holder.tv_contetiy.getText().toString());


                if (count>0) {


                    count++;
                    HashMap<String, String> map = new HashMap<>();
                    map.put("service_id", map_list.get("service_id"));
                    map.put("service_name", map_list.get("service_name"));
                    map.put("service_description", map_list.get("service_description"));
                    map.put("service_price", map_list.get("service_price"));
                    map.put("service_image", map_list.get("service_image"));

                    holder.tv_contetiy.setText(String.valueOf(count));

                    if (!holder.tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                        if (dbHandler.isInCart(map_list.get("service_id"))) {

                            dbHandler.setCart(map, Float.valueOf(holder.tv_contetiy.getText().toString()));

                        } else {

                            dbHandler.setCart(map, Float.valueOf(holder.tv_contetiy.getText().toString()));

                        }

                    }
                    else {

                        dbHandler.removeItemFromCart(map_list.get("service_id"));

                    }
                    total.setText(activity.getResources().getString(R.string.currency)+dbHandler.getTotalAmount());

                    totalprice.setText(activity.getResources().getString(R.string.currency)+dbHandler.getTotalAmount());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_price,tv_contetiy,mrp;
        public ImageView iv_plus, iv_minus;

        public ProductHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.title);
            tv_price = (TextView) view.findViewById(R.id.price);
            tv_contetiy = (TextView) view.findViewById(R.id.number);
            iv_plus = (ImageView) view.findViewById(R.id.plus);
            iv_minus = (ImageView) view.findViewById(R.id.minus);
            mrp= (TextView) view.findViewById(R.id.mrp);
        }
    }
}


