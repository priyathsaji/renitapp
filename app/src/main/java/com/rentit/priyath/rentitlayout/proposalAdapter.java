package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by priyath on 23-04-2017.
 */

public abstract class proposalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<proposalAndHistoryData> proposalAndHistoryDatas;
    Context context;
    int pos;

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView productname;
        TextView customername;
        TextView phonenumber;
        TextView status;
        TextView rent;
        Button button1;
        Button button2;
        Button button3;
        public viewHolder(View itemView) {
            super(itemView);
            productname = (TextView)itemView.findViewById(R.id.proposalproductname);
            customername = (TextView)itemView.findViewById(R.id.customername);
            phonenumber = (TextView)itemView.findViewById(R.id.customernumber);
            rent = (TextView)itemView.findViewById(R.id.proposalrent);
            button1 = (Button)itemView.findViewById(R.id.call);
            button2 = (Button)itemView.findViewById(R.id.disaprove);
            button3 = (Button)itemView.findViewById(R.id.approve);
            status = (TextView)itemView.findViewById(R.id.adStatus);

        }
    }

    public proposalAdapter(ArrayList<proposalAndHistoryData> datas, Context context){
        proposalAndHistoryDatas = datas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposalcard,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        pos = position;
        if(holder instanceof viewHolder){
            viewHolder viewholder = (viewHolder)holder;
            String temp =proposalAndHistoryDatas.get(position).productname;
            viewholder.productname.setText(temp);
            temp = "Customer Name : " + proposalAndHistoryDatas.get(position).name;
            viewholder.customername.setText(temp);
            temp = "Phone No : "+ proposalAndHistoryDatas.get(position).phoneNumber;
            viewholder.phonenumber.setText(temp);
            temp = "Rent : "+ proposalAndHistoryDatas.get(position).Rent;
            viewholder.rent.setText(temp);
            if(!proposalAndHistoryDatas.get(position).isproposal){
                viewholder.status.setVisibility(View.VISIBLE);
                temp = "Status : "+proposalAndHistoryDatas.get(position).Status;
                viewholder.status.setText(temp);
                viewholder.button1.setText("Call");
                viewholder.button2.setText("Add Review");
                viewholder.button3.setText("End Usage");
                viewholder.customername.setVisibility(View.GONE);
            }

            viewholder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button1function(proposalAndHistoryDatas.get(pos));
                }
            });
            viewholder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button2function(proposalAndHistoryDatas.get(pos));
                }
            });
            viewholder.button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button3function(proposalAndHistoryDatas.get(pos));

                }
            });
        }

    }

    public abstract void  button1function(proposalAndHistoryData data);
    public abstract void  button2function(proposalAndHistoryData data);
    public abstract void button3function(proposalAndHistoryData data);
    @Override
    public int getItemCount() {
        return proposalAndHistoryDatas.size();
    }
}
