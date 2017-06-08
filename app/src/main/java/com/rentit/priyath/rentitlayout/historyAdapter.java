package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by priyath on 01-06-2017.
 */

public abstract class historyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<proposalAndHistoryData> proposalAndHistoryDatas;
    Context context;
    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title,adrent,adstatus;
        Button ownerDetails,remove;

        public viewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.HisImage);
            title = (TextView)itemView.findViewById(R.id.HisTitle);
            adrent = (TextView)itemView.findViewById(R.id.HisRent);
            adstatus = (TextView)itemView.findViewById(R.id.HisStatus);
            ownerDetails = (Button)itemView.findViewById(R.id.ownerDetails);
            remove = (Button)itemView.findViewById(R.id.HisRemove);
        }
    }

    public historyAdapter(ArrayList<proposalAndHistoryData> proposalAndHistoryDatas,Context context){
        this.proposalAndHistoryDatas = proposalAndHistoryDatas;
        this.context = context;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historycard,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(holder instanceof viewHolder){
            final int pos = position;
            viewHolder viewHolder = (viewHolder)holder;
            viewHolder.title.setText(proposalAndHistoryDatas.get(position).productname);
            viewHolder.adstatus.setText(proposalAndHistoryDatas.get(position).Status);
            viewHolder.adrent.setText(""+proposalAndHistoryDatas.get(position).Rent);
            Picasso.with(context).load("https://s3.ap-south-1.amazonaws.com/rentit-profile-pics/"+proposalAndHistoryDatas.get(position).image).fit().centerCrop().into(viewHolder.image);
            viewHolder.ownerDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOwnerDetails(proposalAndHistoryDatas.get(pos).OwnerId);
                }
            });
            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    terminateusage(proposalAndHistoryDatas.get(pos));
                }
            });
        }

    }
    abstract void getOwnerDetails(String id);
    abstract void terminateusage(proposalAndHistoryData data);

    @Override
    public int getItemCount() {
        return proposalAndHistoryDatas.size();
    }
}
