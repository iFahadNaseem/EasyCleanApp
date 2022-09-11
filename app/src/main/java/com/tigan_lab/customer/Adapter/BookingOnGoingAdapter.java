package com.tigan_lab.customer.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.tigan_lab.customer.ModelClass.OnGoingBookingModelClass;

import com.tigan_lab.customer.BookingCancellationActivity;
import com.tigan_lab.customer.Reschdule_activity;
import com.tigan_lab.easy_clean.R;


public class BookingOnGoingAdapter extends RecyclerView.Adapter<BookingOnGoingAdapter.MyViewHolder> {

    Context context;

    boolean showingfirst = true;
    int myPos = 0;

    private List<OnGoingBookingModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,duratiojn,status,cancelBtn,uniqueCode,reschedleBtn;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            status = (TextView) view.findViewById(R.id.txtStatus);
            duratiojn = (TextView) view.findViewById(R.id.duration);
            cancelBtn = (TextView) view.findViewById(R.id.cancelBooking);
            reschedleBtn = (TextView) view.findViewById(R.id.rescduleBooking);
            uniqueCode = (TextView) view.findViewById(R.id.uniqueCode);
        }
    }


    public BookingOnGoingAdapter(Context context, List<OnGoingBookingModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public BookingOnGoingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_list, parent, false);
        return new BookingOnGoingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final OnGoingBookingModelClass lists = OfferList.get(position);

        holder.title.setText(lists.getServices());
        holder.status.setText("Booking Accepted");
        holder.duratiojn.setText(lists.getConfirmed_on()+", "+lists.getTime_slot());
        holder.uniqueCode.setText(lists.getUnique_code());

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getId(OfferList.get(position).getBooking_id());
            }
        });

        holder.reschedleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Reschdule_activity.class);
                intent.putExtra("booking_id",String.valueOf(lists.getBooking_id()));
                context.startActivity(intent);
            }
        });


    }
    private void getId(String booking_id) {
        Intent intent=new Intent(context, BookingCancellationActivity.class);
        intent.putExtra("bId",booking_id);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


