package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.rentit.priyath.rentitlayout.R.id.rangebar;

/**
 * Created by priyath on 25-03-2017.
 */

public abstract class AdListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<generalAdDetails> AdData;
    Boolean loading = false;
    int prevoffset=0;
    Context context;
    String image[]={"logo.jpg","car.jpg","bike.jpg","bike.jpg"};
    int imagenumber = 0;
    int TitleImageArray[]={R.drawable.housegeneral,R.drawable.cargeneral,R.drawable.bikegeneral,R.drawable.bikegeneral,R.drawable.bikegeneral,R.drawable.bikegeneral,R.drawable.bikegeneral,R.drawable.bikegeneral,R.drawable.bikegeneral};
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static int leftPin =0;
    private static int rightPin = 6;
    generalAdDetails gad;

    private class viewHolder extends RecyclerView.ViewHolder{
        ImageView AdImage;
        TextView AdTitle;
        TextView AdLocation;
        Button chatButton;
        TextView rentCost;
        RatingBar AdRating;
        CardView cardView;
        public Button rentButton;
        public viewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card1);
            AdImage = (ImageView)itemView.findViewById(R.id.AdImage);
            AdTitle = (TextView)itemView.findViewById(R.id.AdTitle);
            AdLocation = (TextView)itemView.findViewById(R.id.location);
            chatButton = (Button)itemView.findViewById(R.id.chatButton);
            rentCost = (TextView)itemView.findViewById(R.id.Rentcost);
            AdRating = (RatingBar)itemView.findViewById(R.id.rating);
            rentButton =(Button)itemView.findViewById(R.id.rentButton);

        }
    }

    private class viewHolder2 extends RecyclerView.ViewHolder{
        TextView BudgetView;
        RangeBar rangeBar;
        TextView BudgetRange;
        ImageView generalImage;
        Button applyButton;
        public viewHolder2(View itemView) {
            super(itemView);
            BudgetView = (TextView)itemView.findViewById(R.id.budgettview);
            BudgetRange = (TextView)itemView.findViewById(R.id.budgetrange);
            rangeBar = (RangeBar)itemView.findViewById(R.id.rangebar);
            generalImage = (ImageView)itemView.findViewById(R.id.listGeneralImage);
            applyButton = (Button)itemView.findViewById(R.id.applyButton);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetcard,parent,false);
            return new viewHolder2(v);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adcard, parent, false);
            return new viewHolder(v);
        }
    }
    @Override
    public int getItemViewType(int position){
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }
    public AdListAdapter(ArrayList<generalAdDetails> generalAdData,Context context){
        AdData = generalAdData;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {
        final int  pos = position;
        if(holder instanceof viewHolder) {
            viewHolder viewHolder = (viewHolder)holder;

            gad = AdData.get(position - 1);
            String s = String.valueOf(gad.Adcost) + " Rs";
            viewHolder.AdTitle.setText(gad.AdTitle);
            viewHolder.AdLocation.setText(gad.Location);
            viewHolder.rentCost.setText(s);
            viewHolder.AdRating.setRating(gad.adAvgRating);
            //Picasso.with(context).load("http://192.168.43.87:5000/"+image[imagenumber]).fit().centerCrop().into(viewHolder.AdImage);
            Picasso.with(context).load("https://s3.ap-south-1.amazonaws.com/rentit-profile-pics/"+gad.primaryImageName).fit().centerCrop().into(viewHolder.AdImage);
           viewHolder.rentButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  proposal(AdData.get(pos-1));
               }
           });

            viewHolder.chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startChat(AdData.get(pos-1).ownerId,AdData.get(pos-1).AdTitle);
                    Log.i("toId",AdData.get(pos-1).ownerId);
                }
            });


            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,Details1.class);
                    intent.putExtra("type",AdData.get(pos-1).type);
                    intent.putExtra("id",AdData.get(pos-1).productId);
                    intent.putExtra("ownername",AdData.get(pos-1).ownerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }else if(holder instanceof viewHolder2){
            if(position == 0){
                imagenumber = geturlnumber();
            }
            final viewHolder2 viewHolder2 = (viewHolder2)holder;
            viewHolder2.generalImage.setImageResource(TitleImageArray[imagenumber]);
            viewHolder2.rangeBar.setTickStart(0);
            viewHolder2.rangeBar.setTickEnd(30000);
            viewHolder2.rangeBar.setRangePinsByIndices(leftPin,rightPin);
            String s = ""+leftPin*5000+" Rs to "+rightPin*5000+" Rs+";
            viewHolder2.BudgetRange.setText(s);
            viewHolder2.applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftPin = viewHolder2.rangeBar.getLeftIndex();
                    rightPin = viewHolder2.rangeBar.getRightIndex();
                    refreshdata(leftPin,rightPin);
                }
            });

        }

    }
    public abstract  void startChat(String toId,String title);
    public abstract void refreshdata(int left,int right);
    public abstract int geturlnumber();
    public abstract void proposal(generalAdDetails data);

    @Override
    public int getItemCount() {
        return AdData.size()+1;
    }
}
