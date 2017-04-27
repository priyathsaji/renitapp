package com.rentit.priyath.rentitlayout;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;


public class GCMService extends GcmListenerService {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.i("from",data.getString("fromname"));
        Intent intent = new Intent(chatActivity.MESSAGE_RECEIVED);
        intent.putExtra("message",message);
        LocalBroadcastManager.getInstance(GCMService.this).sendBroadcast(intent);
        sendNotification(message,data.getString("type"));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String message, String type) {

        globalData globaldata = (globalData)getApplicationContext();


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(type.contentEquals("proposal")){
            Intent intent = new Intent(this, myAds.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("flag",1);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentTitle("Proposal")
                    .setContentIntent(pendingIntent)
                    .setContentText(message);
            notificationManager.notify(1,notificationBuilder.build());
        }else{

            globaldata.addMessage(message);


            Intent intent = new Intent(this, chatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("message",message);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            for(int i=0;i<globaldata.size();i++){
                inboxStyle.addLine(globaldata.getMessage(i));
            }
            notificationBuilder.setContentTitle(""+globaldata.size()+" new messages")
                    .setStyle(inboxStyle)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(0, notificationBuilder.build());
        }


    }
}
