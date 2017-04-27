package com.rentit.priyath.rentitlayout;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SplashActivity extends AppCompatActivity {


    Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button btn_register;

    public static final String REGISTRATION_PROCESS = "registration";
    public static final String MESSAGE_RECEIVED = "message_received";

    globalData globaldata;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        globaldata = (globalData)getApplicationContext();

        FileInputStream in = null;
        try {
            in = openFileInput("userdetails");
            ObjectInputStream ois = new ObjectInputStream(in);
            userDetails details = (userDetails) ois.readObject();
            globaldata.addEmalid(details.emailId);
            globaldata.addUserId(details.id);
            globaldata.addUsername(details.username);
            globaldata.addPhoneNumeber(details.phonenumber);
            if(details.username == null){
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
            }
            ois.close();
            in.close();
            Log.i("username",details.username);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        if(in == null){
            Log.i("status","invalid file");
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

/*
                globaldata.addUserId("nitinsaji");
                globaldata.addUsername("Nitin Saji");
                globaldata.addEmalid("nitinsaji007@gmail.com");
                globaldata.addPhoneNumeber("9526849323");

        globaldata.addUserId("priyathsaji");
        globaldata.addUsername("Priyath Saji");
        globaldata.addEmalid("priyathsaji@gmail.com");
        globaldata.addPhoneNumeber("9526849323");

*/

        if (checkPlayServices()) {
            startRegisterProcess();
        }else{
            requestPermission();
        }



    }


    private void startRegisterProcess(){

        if(checkPermission()){

            startRegisterService();
            //Toast.makeText(getApplicationContext(), "registration succesfull", Toast.LENGTH_LONG).show();
        } else {

            requestPermission();
        }

    }

    private void startRegisterService(){

        Intent intent = new Intent(SplashActivity.this, RegistrationIntentService.class);
        intent.putExtra("DEVICE_ID", getDeviceId());
        intent.putExtra("DEVICE_NAME",getDeviceName());
        startService(intent);
        gps = new GPSTracker(SplashActivity.this);
        double latitude = 0;
        double longitude = 0;
        String location = null;
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            globaldata.addLatitude(latitude);
            globaldata.addlogitude(longitude);

            placeFinder placefinder = new placeFinder();
            try {
                location = placefinder.getLocation(latitude,longitude,SplashActivity.this);
                globaldata.addLocation(location);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // \n is for new line

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private String getDeviceId(){

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    private String getDeviceName(){
        String deviceName = Build.MODEL;
        String deviceMan = Build.MANUFACTURER;
        return  deviceMan + " " +deviceName;
    }


    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE,mPermission},PERMISSION_REQUEST_CODE);
        //ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startRegisterService();

                }
                break;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
