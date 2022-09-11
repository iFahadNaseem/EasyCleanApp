package com.tigan_lab.customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.FaqMiddleActivity;
import com.tigan_lab.customer.ModelClass.FAQmodelClass;
import com.tigan_lab.easy_clean.R;

import java.util.List;

public class FAQmainAdapter extends RecyclerView.Adapter<FAQmainAdapter.MyViewHolder> {

    Context context;
    private List<FAQmodelClass> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        LinearLayout linearLayout;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            linearLayout =  view.findViewById(R.id.ll);
        }
    }


    public FAQmainAdapter(Context  context,List<FAQmodelClass> moviesList) {
        this.context=context;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_faq_list1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FAQmodelClass movie = moviesList.get(position);
        holder.title.setText(movie.getFaq_category());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getId(moviesList.get(position).getFaq_cat_id());
            }
        });

    }

    private void getId(String faq_cat_id) {
        Intent intent=new Intent(context, FaqMiddleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("faqId",faq_cat_id);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
