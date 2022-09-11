package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Extra.Config.DeleteAddress;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.tigan_lab.customer.EditAddressActivity;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.SelectAdressModelClss;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowAddressAdapter extends RecyclerView.Adapter<ShowAddressAdapter.MyViewHolder> {

    Context context;
    private List<SelectAdressModelClss> moviesList;
    private int lastSelectedPosition = -1;
    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;
    private boolean ischecked = false;
    private String  getphone, getname, getaddress,getcityname,getareaname,addressid;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView phone, userName, taddrss;
        RadioButton radioButton;
        LinearLayout linearlayout;
        ImageView editBtn,DeleteBtn;

        public MyViewHolder(View view) {
            super(view);
            phone = (TextView) view.findViewById(R.id.txtphone);
            userName = (TextView) view.findViewById(R.id.txtName);
            taddrss = (TextView) view.findViewById(R.id.taddrss);
            linearlayout =  view.findViewById(R.id.linearlayout);
            radioButton = (RadioButton) view.findViewById(R.id.radio);
            editBtn=view.findViewById(R.id.editBtn);
            DeleteBtn=view.findViewById(R.id.DeleteBtn);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    RadioButton cb = (RadioButton) view;
                    int clickedPos = getAdapterPosition();

                    getaddress = moviesList.get(clickedPos).getAddress();

                    getname = moviesList.get(clickedPos).getUser_name();
                    getphone = moviesList.get(clickedPos).getUser_phone();
                    getareaname = moviesList.get(clickedPos).getArea_name();

                    getcityname = moviesList.get(clickedPos).getCity_name();

                    addressid = moviesList.get(clickedPos).getAddress_id();

                    if (moviesList.size() > 1) {
                        if (cb.isChecked()) {
                            if (lastChecked != null) {
                                lastChecked.setChecked(false);
                                moviesList.get(lastCheckedPos).setIscheckd(false);
                            }

                            lastChecked = cb;
                            lastCheckedPos = clickedPos;
                        } else {
                            lastChecked = null;
                        }
                    }
                    moviesList.get(clickedPos).setIscheckd(cb.isChecked());

                    if (cb.isChecked()) {
                        ischecked = true;


                    } else {
                        ischecked = false;

                    }

                }
            });
        }
    }

    public ShowAddressAdapter(Context context,List<SelectAdressModelClss> moviesList) {
        this.context=context;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_select_addres, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SelectAdressModelClss movie = moviesList.get(position);
        holder.phone.setText(movie.getUser_phone());
        holder.userName.setText(movie.getUser_name());
        holder.taddrss.setText(movie.getAddress());
        holder.radioButton.setTag(new Integer(position));

        //for default check in first item
        if (position == 0 /*&& mList.getIscheckd() && holder.rb_select.isChecked()*/) {
            holder.radioButton.setChecked(true);
            moviesList.get(position).setIscheckd(true);

            lastChecked = holder.radioButton;
            lastCheckedPos = 0;

            addressid=moviesList.get(0).getAddress_id();

            getaddress=moviesList.get(0).getAddress();
            getname = moviesList.get(0).getUser_name();

            ischecked = true;



        }

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, EditAddressActivity.class);
                intent.putExtra("addrssId",moviesList.get(position).getAddress_id());
                context.startActivity(intent);
            }


        });
        holder.DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAdress(moviesList.get(position).getAddress_id());
            }
            private void deleteAdress(String address_id) {
                String tag_json_obj = "json_add_address_req";
                Map<String, String> params = new HashMap<String, String>();
                params.put("address_id", address_id);

                CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                        DeleteAddress, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        try {
                            String status = response.getString("status");
                            String msg = response.getString("message");

                            if (status.contains("1")) {
                                Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
                                moviesList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, moviesList.size());
                            }
                            else {
                                Toast.makeText(context, msg+"", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.getCache().clear();
                requestQueue.add(jsonObjReq);
            }

        });

        holder.radioButton.setChecked(lastSelectedPosition == position);
        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.radioButton.setChecked(true);
                moviesList.get(position).getAddress_id();
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public String getlocation_id() {
        return addressid;
    }

    public String getaddress() {
        String address =  getname + "\n" + getaddress;
        return address;
    }

    public boolean ischeckd() {
        return ischecked;
    }

}
