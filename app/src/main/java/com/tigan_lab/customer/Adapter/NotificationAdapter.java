package com.tigan_lab.customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.ModelClass.NoticeModelClss;
import com.tigan_lab.easy_clean.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NoticeModelClss> moviesList;
    Context context;

    public NotificationAdapter(Context context, List<NoticeModelClss> noticeList) {
        this.context=context;
        this.moviesList=noticeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, txxtDate,txtDesc,txtNew,txttime;
        ImageView imageView;
        View viewww;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtName);
            txxtDate = (TextView) view.findViewById(R.id.txtDAte);
            txtDesc = (TextView) view.findViewById(R.id.txtDesc);
            txtNew = (TextView) view.findViewById(R.id.txtNew);
            txttime = (TextView) view.findViewById(R.id.txtTime);
            imageView=view.findViewById(R.id.imgeView);
            viewww=view.findViewById(R.id.view);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NoticeModelClss movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.txtDesc.setText(movie.getDescription());
        holder.txttime.setText(movie.getCreated_at());
        Picasso.with(context).load(movie.getImage()).error(R.drawable.ic_about).into(holder.imageView);



        holder.viewww.setVisibility(View.GONE);
        holder.txtNew.setVisibility(View.GONE);
        holder.txxtDate.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
