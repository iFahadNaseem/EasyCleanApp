package com.tigan_lab.customer.Adapter;

import static com.tigan_lab.customer.Extra.Config.DeleteAddress;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.tigan_lab.customer.EditAddressActivity;
import com.tigan_lab.customer.Extra.ConnectivityReceiver;
import com.tigan_lab.customer.Extra.CustomVolleyJsonRequest;
import com.tigan_lab.customer.ModelClass.Delivery_address_model;
import com.tigan_lab.easy_clean.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Manage_address_adapter extends RecyclerSwipeAdapter<Manage_address_adapter.MyViewHolder> {

    private static String TAG = Manage_address_adapter.class.getSimpleName();

    private List<Delivery_address_model> modelList;

    private Context context;

    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;
    private boolean ischecked = false;
    private String getphone, getname, getaddress,getcityname,getareaname,addressid;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_address, tv_name, tv_phone;
        public RadioButton rb_select;

        SwipeLayout swipeLayout;
        ImageView buttonDelete, btn_edit;

        public MyViewHolder(View view) {
            super(view);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            buttonDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            btn_edit = (ImageView) itemView.findViewById(R.id.iv_edit);

            tv_address = (TextView) view.findViewById(R.id.address);
            tv_name = (TextView) view.findViewById(R.id.name);

            rb_select = (RadioButton) view.findViewById(R.id.rb_add);

            rb_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RadioButton cb = (RadioButton) view;
                    int clickedPos = getAdapterPosition();

                    getaddress = modelList.get(clickedPos).getAddress();

                    getname = modelList.get(clickedPos).getUser_name();
                    getphone = modelList.get(clickedPos).getUser_phone();
                    getareaname= modelList.get(clickedPos).getArea_name();

                    getcityname=modelList.get(clickedPos).getCity_name();

                    addressid=modelList.get(clickedPos).getAddress_id();

                    if (modelList.size() > 1) {
                        if (cb.isChecked()) {
                            if (lastChecked != null) {
                                lastChecked.setChecked(false);
                                modelList.get(lastCheckedPos).setIscheckd(false);
                            }

                            lastChecked = cb;
                            lastCheckedPos = clickedPos;
                        } else {
                            lastChecked = null;
                        }
                    }
                    modelList.get(clickedPos).setIscheckd(cb.isChecked());

                    if (cb.isChecked()) {
                        ischecked = true;


                    } else {
                        ischecked = false;

                    }

                }
            });
        }
    }

    public Manage_address_adapter(List<Delivery_address_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Manage_address_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_delivery_time_rv_test, parent, false);

        context = parent.getContext();

        return new Manage_address_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Manage_address_adapter.MyViewHolder holder, final int position) {
        final Delivery_address_model mList = modelList.get(position);

        holder.tv_address.setText(mList.getAddress());

        holder.tv_name.setText(mList.getUser_name());

        holder.rb_select.setChecked(mList.getIscheckd());

        holder.rb_select.setTag(new Integer(position));

        if (position == 0 /*&& mList.getIscheckd() && holder.rb_select.isChecked()*/) {
            holder.rb_select.setChecked(true);
            modelList.get(position).setIscheckd(true);

            lastChecked = holder.rb_select;
            lastCheckedPos = 0;

            addressid=modelList.get(0).getAddress_id();

            getaddress=modelList.get(0).getAddress();
            getname = modelList.get(0).getUser_name();

            ischecked = true;



        }

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        holder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(holder.swipeLayout);

                if(ConnectivityReceiver.isConnected()){
                    makeDeleteAddressRequest(mList.getAddress_id(),position);
                }

            }
        });


        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, EditAddressActivity.class);
                intent.putExtra("addrssId",mList.getAddress_id());
                intent.putExtra("value","1");
                intent.putExtra("active","0");
                context.startActivity(intent);

            }
        });

        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
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

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    private void makeDeleteAddressRequest(String location_id, final int position) {

        String tag_json_obj = "json_delete_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("address_id", location_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                DeleteAddress, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    if (status.contains("1")) {

                        String msg = response.getString("message");

                        Toast.makeText(context, ""+msg, Toast.LENGTH_SHORT).show();

                        modelList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, modelList.size());
                        mItemManger.closeAllItems();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);}
    }




