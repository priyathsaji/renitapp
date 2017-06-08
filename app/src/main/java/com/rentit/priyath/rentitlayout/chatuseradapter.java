package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by priyath on 08-04-2017.
 */

public class chatuseradapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<chatUser> chatUsers;
    Context context;

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView productName;
        TextView ownerName;
        TextView number;
        CardView userCard;
        public viewHolder(View itemView) {
            super(itemView);
            productName = (TextView)itemView.findViewById(R.id.productName);
            ownerName = (TextView)itemView.findViewById(R.id.ownerName);
            number = (TextView)itemView.findViewById(R.id.Number);
            userCard = (CardView)itemView.findViewById(R.id.userCard);

        }
    }

    public chatuseradapter(ArrayList<chatUser> chatUsers, Context context){
        this.chatUsers = chatUsers;
        this.context = context;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userscard,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        viewHolder viewHolder = (viewHolder)holder;
        viewHolder.productName.setText(chatUsers.get(position).productName);
        viewHolder.ownerName.setText(chatUsers.get(position).ownerName);
        viewHolder.number.setText(""+chatUsers.get(position).number);
        viewHolder.userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"clicked on position :"+position,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context,chatInterface.class);
                intent.putExtra("toId",chatUsers.get(position).toId);
                intent.putExtra("fromId",chatUsers.get(position).fromId);
                intent.putExtra("ownername",chatUsers.get(position).ownerName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatUsers.size();
    }
}
