package com.tigan_lab.customer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.customer.ModelClass.FaqLastModelClass;
import com.tigan_lab.easy_clean.R;

import java.util.List;

public class FAQlastAdapter extends RecyclerView.Adapter<FAQlastAdapter.MyViewHolder> {

    Context context;
    private List<FaqLastModelClass> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        LinearLayout linearLayout;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            linearLayout =  view.findViewById(R.id.ll);
        }
    }

    public FAQlastAdapter(Context  context, List<FaqLastModelClass> moviesList) {
        this.context=context;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_faq_anslist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FaqLastModelClass movie = moviesList.get(position);
        holder.title.setText(movie.getFaq_ans());

    }


    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
