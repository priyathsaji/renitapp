package com.rentit.priyath.rentitlayout;

import android.*;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {


    Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;

    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShowLocation = (Button) findViewById(R.id.button3);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(SplashActivity.this);
                double latitude = 0; 
                double longitude = 0;
                String location = null;

                // check if GPS enabled
                if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    placeFinder placefinder = new placeFinder();
                    try {
                        location = placefinder.getLocation(latitude,longitude,SplashActivity.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is " +location, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("Latitude",latitude);
                intent.putExtra("Longitude",longitude);
                intent.putExtra("Location",location);
                startActivity(intent);

            }
        });
    }
}
