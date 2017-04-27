package com.rentit.priyath.rentitlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by priyath on 11-04-2017.
 */

public class NetworkReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager manager1 = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        //Intent intent1 = new Intent(context,getChatService.class);

        if(info!=null && info.getType() == ConnectivityManager.TYPE_MOBILE){
            //context.stopService(intent1);
            //context.startService(intent1);

        }else if(manager1.isWifiEnabled()){
            //context.stopService(intent1);
            //context.startService(intent1);

        }else{
            //context.stopService(intent1);
        }
    }
}
