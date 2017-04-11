package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by priyath on 02-04-2017.
 */
class chatadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<chatData> chatdata;
    Context context;

    private final int TYPE_FROM = 0;
    private final int TYPE_TO = 1;

    private class viewHolder extends RecyclerView.ViewHolder{
        TextView toMessage;
        TextView toname;
        viewHolder(View itemView) {
            super(itemView);
            toMessage = (TextView)itemView.findViewById(R.id.toMessage);
            toname = (TextView)itemView.findViewById(R.id.toname);

        }
    }

    private class viewHolder2 extends RecyclerView.ViewHolder{
       TextView fromMessage;
        TextView fromname;
        viewHolder2(View itemView) {
            super(itemView);
           fromMessage = (TextView)itemView.findViewById(R.id.fromMessage);
            fromname = (TextView)itemView.findViewById(R.id.fromname);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_TO){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chattocard,parent,false);
            return new viewHolder(v);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatfromcard, parent, false);
            return new viewHolder2(v);
        }
    }
    @Override
    public int getItemViewType(int position){
        if(chatdata.get(position).type == 1)
            return TYPE_TO;
        return TYPE_FROM;
    }


    public chatadapter(ArrayList<chatData> chatDatas, Context context){
        chatdata = chatDatas;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(chatdata.get(position).type==1) {
            viewHolder viewHolder = (viewHolder)holder;
            viewHolder.toMessage.setText(chatdata.get(position).message);
            viewHolder.toname.setText(chatdata.get(position).name);

        }else if(chatdata.get(position).type==0){
            viewHolder2 viewHolder2 = (viewHolder2)holder;
            viewHolder2.fromMessage.setText(chatdata.get(position).message);
            viewHolder2.fromname.setText(chatdata.get(position).name);


        }

    }


    @Override
    public int getItemCount() {
        return chatdata.size();
    }
}
