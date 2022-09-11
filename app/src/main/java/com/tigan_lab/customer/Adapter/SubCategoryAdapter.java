package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Extra.Config.HomeChildCategory;
import static com.tigan_lab.customer.Extra.Config.IMAGE_URL;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.tigan_lab.customer.ModelClass.SubCatChildModelclass;
import com.tigan_lab.customer.ModelClass.SubCateModelClass;
import com.tigan_lab.easy_clean.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    Context context;
    private final List<SubCateModelClass> subcateList;
    private List<SubCatChildModelclass> subcatechildList;
    SubCateChildAdapter adapter;
    PopupWindow pw;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView proImag;
        RecyclerView recyclerView;
        View subLayout;
        LayoutInflater inflater;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView1);
            proImag = (ImageView) view.findViewById(R.id.imageView1);
            inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            pw = new PopupWindow(inflater.inflate(R.layout.team_subcategory, (ViewGroup) view, false),ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pw.setAnimationStyle(R.style.CustomDialogAnimation);
            subLayout = pw.getContentView();
            recyclerView = subLayout.findViewById(R.id.recyclerChildCate);
        }
    }


    public SubCategoryAdapter(Context context,List<SubCateModelClass> subcateList) {
        this.context = context;
        this.subcateList = subcateList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubCateModelClass movie = subcateList.get(position);
        holder.title.setText(movie.getSub_category_name());


        Picasso.with(context).load(IMAGE_URL + movie.getSub_category_img()).error(R.drawable.ic_about).into(holder.proImag);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.showAtLocation(v, Gravity.CENTER, 0, 0);
                hitChild_category(movie.getSub_category_id(), holder.recyclerView);
            }

            private void hitChild_category(String sub_category_id, RecyclerView recyclerView) {
                String tag_json_obj = "json_category_req";
                Map<String, String> params = new HashMap<String, String>();
                params.put("sub_category_id", sub_category_id);
                CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                        HomeChildCategory, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("TAG", response.toString());
                        try{
                            if (response != null && response.length() > 0) {
                                String status = response.getString("status");

                                if (status.contains("1")) {
                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<SubCatChildModelclass>>() {
                                    }.getType();
                                    subcatechildList = gson.fromJson(response.getString("data"), listType);
                                    adapter = new SubCateChildAdapter(context,subcatechildList);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("TAG", "Error: " + error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        }
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.getCache().clear();
                requestQueue.add(jsonObjReq);
            }


        });
    }



    @Override
    public int getItemCount() {
        return subcateList.size();
    }
}

