package com.rentit.priyath.rentitlayout;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


public class GCMInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
