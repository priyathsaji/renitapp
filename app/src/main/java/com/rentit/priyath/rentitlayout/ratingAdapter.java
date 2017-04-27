package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by priyath on 26-04-2017.
 */

public class ratingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ratingData> ratingDatas;

    public ratingAdapter(Context context, ArrayList<ratingData> ratingDatas){
        this.context = context;
        this.ratingDatas = ratingDatas;

    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView comment;
        RatingBar ratingBar;
        public viewHolder(View itemView) {
            super(itemView);
            username = (TextView)itemView.findViewById(R.id.username);
            comment = (TextView)itemView.findViewById(R.id.comment);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rating);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ratingcard,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof viewHolder){
            viewHolder viewholder = (viewHolder)holder;
            viewholder.username.setText(ratingDatas.get(position).username);
            viewholder.comment.setText(ratingDatas.get(position).comment);
            viewholder.ratingBar.setRating(ratingDatas.get(position).rating);
        }

    }

    @Override
    public int getItemCount() {
        return ratingDatas.size();
    }
}
