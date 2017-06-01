package com.rentit.priyath.rentitlayout;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.rentit.priyath.rentitlayout.models.RequestBody;
import com.rentit.priyath.rentitlayout.models.ResponseBody;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationIntentService extends IntentService{

    globalData globaldata;
    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String deviceId = intent.getStringExtra("DEVICE_ID");
        String deviceName = intent.getStringExtra("DEVICE_NAME");
        globaldata = (globalData)getApplicationContext();
        String userId = globaldata.getUserId();

        Log.i("device name",deviceName);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String registrationId = instanceID.getToken("762704509331", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            registerDeviceProcess(deviceName,deviceId,registrationId,userId);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void registerDeviceProcess(String deviceName, String deviceId, String registrationId,String userId){

        RequestBody requestBody = new RequestBody();
        requestBody.setDeviceId(deviceId);
        requestBody.setDeviceName(deviceName);
        requestBody.setRegistrationId(registrationId);
        requestBody.setUserId(userId);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://rentitapi.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<ResponseBody> call = request.registerDevice(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
//                Log.i("result",responseBody.getResult());
                //Intent intent = new Intent(MainActivity.REGISTRATION_PROCESS);
                //intent.putExtra("result", responseBody.getResult());
               // intent.putExtra("message",responseBody.getMessage());
                //LocalBroadcastManager.getInstance(RegistrationIntentService.this).sendBroadcast(intent);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
