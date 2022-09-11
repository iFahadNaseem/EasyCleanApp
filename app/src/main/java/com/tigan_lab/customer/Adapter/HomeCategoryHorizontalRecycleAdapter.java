package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Extra.Config.HomeSubCategory;
import static com.tigan_lab.customer.Extra.Config.IMAGE_URL;
import static com.tigan_lab.customer.SubCategoryActivity.noData;
import static com.tigan_lab.customer.SubCategoryActivity.progressDialog;
import static com.tigan_lab.customer.SubCategoryActivity.recyclerView;
import static com.tigan_lab.customer.SubCategoryActivity.tab_value;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.HomeCateModelClass;
import com.tigan_lab.customer.ModelClass.SubCateModelClass;
import com.tigan_lab.easy_clean.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeCategoryHorizontalRecycleAdapter extends RecyclerView.Adapter<HomeCategoryHorizontalRecycleAdapter.MyViewHolder> {

    Context context;
    private List<SubCateModelClass> subcateList = new ArrayList<>();
    SubCategoryAdapter subCategoryAdapter;

    private List<HomeCateModelClass> OfferList;
    int myPos = tab_value;
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


    public HomeCategoryHorizontalRecycleAdapter(Context context, List<HomeCateModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public HomeCategoryHorizontalRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_horizontal_list, parent, false);


        return new HomeCategoryHorizontalRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final HomeCateModelClass lists = OfferList.get(position);

        holder.title.setText(lists.getCategory_name());
        Picasso.with(context).load(IMAGE_URL + lists.getCategory_image()).error(R.drawable.ic_about).into(holder.image);




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


             getId(OfferList.get(position).getCategory_id());
            }
        });



    }

    private void getId(String category_id, int tab_value) {
        progressDialog.show();
        subcateList.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("category_id", category_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                HomeSubCategory, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", response.toString());
                try{
                    progressDialog.dismiss();
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<SubCateModelClass>>() {
                            }.getType();
                            subcateList = gson.fromJson(response.getString("data"), listType);
                            subCategoryAdapter = new SubCategoryAdapter(context,subcateList);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(subCategoryAdapter);

                            subCategoryAdapter.notifyDataSetChanged();
                            noData.setVisibility(View.GONE);
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

    private void getId(String getcat_id) {
        progressDialog.show();
        subcateList.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("category_id", getcat_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                HomeSubCategory, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", response.toString());
                try{
                    progressDialog.dismiss();
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<SubCateModelClass>>() {
                            }.getType();
                            subcateList = gson.fromJson(response.getString("data"), listType);
                            subCategoryAdapter = new SubCategoryAdapter(context,subcateList);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(subCategoryAdapter);
                            subCategoryAdapter.notifyDataSetChanged();
                            noData.setVisibility(View.GONE);
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }



    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


