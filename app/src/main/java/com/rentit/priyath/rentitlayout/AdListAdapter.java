package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
    int TitleImageArray[]={R.drawable.housegeneral,R.drawable.cargeneral,R.drawable.bikegeneral,R.drawable.bikegeneral};
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static int leftPin =0;
    private static int rightPin = 6;

    public class viewHolder extends RecyclerView.ViewHolder{
        public ImageView AdImage;
        public TextView AdTitle;
        public TextView AdLocation;
        public Button chatButton;
        public TextView rentCost;
        public RatingBar AdRating;
        public Button rentButton;
        public viewHolder(View itemView) {
            super(itemView);
            AdImage = (ImageView)itemView.findViewById(R.id.AdImage);
            AdTitle = (TextView)itemView.findViewById(R.id.AdTitle);
            AdLocation = (TextView)itemView.findViewById(R.id.location);
            chatButton = (Button)itemView.findViewById(R.id.chatButton);
            rentCost = (TextView)itemView.findViewById(R.id.Rentcost);
            AdRating = (RatingBar)itemView.findViewById(R.id.rating);
            rentButton =(Button)itemView.findViewById(R.id.rentButton);

        }
    }

    public class viewHolder2 extends RecyclerView.ViewHolder{
        TextView BudgetView;
        RangeBar rangeBar;
        TextView BudgetRange;
        ImageView generalImage;
        public viewHolder2(View itemView) {
            super(itemView);
            BudgetView = (TextView)itemView.findViewById(R.id.budgettview);
            BudgetRange = (TextView)itemView.findViewById(R.id.budgetrange);
            rangeBar = (RangeBar)itemView.findViewById(R.id.rangebar);
            generalImage = (ImageView)itemView.findViewById(R.id.listGeneralImage);
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

        if(holder instanceof viewHolder) {
            viewHolder viewHolder = (viewHolder)holder;
            generalAdDetails gad;
            gad = AdData.get(position - 1);
            String s = String.valueOf(gad.Adcost) + " Rs";
            viewHolder.AdTitle.setText(gad.AdTitle);
            viewHolder.AdLocation.setText(gad.Location);
            viewHolder.rentCost.setText(s);
            viewHolder.AdRating.setRating(gad.adAvgRating);
            Picasso.with(context).load("http://rentitapi.herokuapp.com/"+image[imagenumber]).fit().centerCrop().into(viewHolder.AdImage);
        }else if(holder instanceof viewHolder2){
            if(position == 0){
                imagenumber = geturlnumber();
            }
            viewHolder2 viewHolder2 = (viewHolder2)holder;
            viewHolder2.generalImage.setImageResource(TitleImageArray[imagenumber]);
            viewHolder2.rangeBar.setTickStart(0);
            viewHolder2.rangeBar.setTickEnd(30000);
            viewHolder2.rangeBar.setRangePinsByIndices(leftPin,rightPin);
            String s = ""+leftPin*5000+" Rs to "+rightPin*5000+" Rs+";
            viewHolder2.BudgetRange.setText(s);
            viewHolder2.rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                    leftPin = leftPinIndex;
                    rightPin = rightPinIndex;
                    refreshdata(leftPinIndex,rightPinIndex);

                }
            });

        }

    }

    public abstract void refreshdata(int left,int right);
    public abstract int geturlnumber();

    @Override
    public int getItemCount() {
        return AdData.size()+1;
    }
}
