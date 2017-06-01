package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by priyath on 11-04-2017.
 */

public abstract class myAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<generalAdDetails> adDetails;
    Context context;
    int pos;

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView adImage;
        TextView adTitle;
        TextView adLocation;
        TextView adRent;
        TextView adStatus;
        RatingBar adRating;
        Button removeAd;


        public viewHolder(View itemView) {
            super(itemView);
            adImage = (ImageView)itemView.findViewById(R.id.AdImage);
            adTitle = (TextView)itemView.findViewById(R.id.AdTitle);
            adLocation =(TextView)itemView.findViewById(R.id.location);
            adRent = (TextView)itemView.findViewById(R.id.AdRent);
            adStatus = (TextView)itemView.findViewById(R.id.status);
            adRating = (RatingBar)itemView.findViewById(R.id.rating);
            removeAd = (Button)itemView.findViewById(R.id.editAd);

        }
    }

    public myAdAdapter(ArrayList<generalAdDetails> adDetails,Context context){
        this.context = context;
        this.adDetails = adDetails;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myadcard,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        pos = position;
        viewHolder viewHolder = (myAdAdapter.viewHolder)holder;
        generalAdDetails gad;
        gad = adDetails.get(position);
        String s = String.valueOf(gad.Adcost) + " Rs";
        viewHolder.adTitle.setText(gad.AdTitle);
        viewHolder.adLocation.setText(gad.Location);
        viewHolder.adRent.setText(s);
        viewHolder.adRating.setRating(gad.adAvgRating);
        Picasso.with(context).load("https://s3.ap-south-1.amazonaws.com/rentit-profile-pics/"+gad.primaryImageName).fit().centerCrop().into(viewHolder.adImage);
        viewHolder.adStatus.setText(gad.status);
        viewHolder.removeAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAd(adDetails.get(pos).productId,adDetails.get(pos).type);

            }
        });

    }

    abstract void removeAd(String productId, int type);
    @Override
    public int getItemCount() {
        return adDetails.size();
    }
}