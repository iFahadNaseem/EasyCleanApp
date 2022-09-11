package com.tigan_lab.customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.tigan_lab.customer.ModelClass.HistoryBookinModelclass;
import com.tigan_lab.easy_clean.R;


public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.MyViewHolder> {

    Context context;

    boolean showingfirst = true;
    int myPos = 0;

    private List<HistoryBookinModelclass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,duratiojn,status,otp;
        View vieww;
        LinearLayout linearLayout;
        public MyViewHolder(View view) {
            super(view);
            otp = (TextView) view.findViewById(R.id.otp);
            title = (TextView) view.findViewById(R.id.title);
            status = (TextView) view.findViewById(R.id.txtStatus);
            duratiojn = (TextView) view.findViewById(R.id.duration);
            vieww=  view.findViewById(R.id.view);
            linearLayout =  view.findViewById(R.id.llinear);
        }
    }


    public BookingHistoryAdapter(Context context, List<HistoryBookinModelclass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public BookingHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_list, parent, false);
        return new BookingHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final HistoryBookinModelclass lists = OfferList.get(position);

        holder.title.setText(lists.getServices());
       holder.status.setText("Booking Completed");
        holder.duratiojn.setText(lists.getConfirmed_on()+", "+lists.getTime_slot());

        holder.vieww.setVisibility(View.GONE);
        holder.otp.setVisibility(View.GONE);
        holder.linearLayout.setVisibility(View.GONE);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPos = position;

/*                Intent intent = new Intent(context, ReviewRatingActivity.class);

                intent.putExtra("categoryid",lists.);



                context.startActivity(intent);*/






                notifyDataSetChanged();

            }
        });


    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


