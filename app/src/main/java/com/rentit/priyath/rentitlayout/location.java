package com.rentit.priyath.rentitlayout;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class location extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, PlaceSelectionListener
        {

            private GoogleMap mMap;
            GoogleApiClient mGoogleApiClient;
            Location mLastLocation;
            Marker mCurrLocationMarker;
            LocationRequest mLocationRequest;
            LatLng latlng;
            Button button;
            int flag;
            JSONObject postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.location);

        postData = new JSONObject();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Search a Location");
        button = (Button)findViewById(R.id.LoctionSelectButton);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag",2);
        if(flag == 1){
            button.setText("Post Ad");
            try {
                postData.put("Title",intent.getStringExtra("Title"));
                postData.put("Rent",intent.getIntExtra("Rent",0));
                postData.put("Description",intent.getStringExtra("Description"));
                postData.put("subitem1",intent.getStringExtra("subitem1"));
                postData.put("subitem2",intent.getStringExtra("subitem2"));
                postData.put("subitem3",intent.getStringExtra("subitem3"));
                postData.put("type",intent.getIntExtra("Type",0));
                postData.put("image_1",intent.getStringExtra("image_1"));
                postData.put("image_2",intent.getStringExtra("image_2"));
                postData.put("image_3",intent.getStringExtra("image_3"));
                postData.put("image_4",intent.getStringExtra("image_4"));
                postData.put("image_5",intent.getStringExtra("image_5"));
                postData.put("AverageRating",intent.getIntExtra("AverageRating",0));
                postData.put("ownerDetails",intent.getStringExtra("OwnerDetails"));
                postData.put("Status","available");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Toast.makeText(this,"from postad Activity" + intent.getStringExtra("Title"),Toast.LENGTH_LONG).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeFinder finder = new placeFinder();
                String location = null;
                try {
                    location = finder.getLocality(latlng,getApplicationContext());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(flag == 1){
                    try {
                        location+=" , ";
                        location+=finder.getFeatureName(latlng,getApplicationContext());
                        postData.put("Location",location);
                        postData.put("longitude",latlng.longitude);
                        postData.put("latitude",latlng.latitude);
                        new postAsyncTask().execute(0);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }if(flag == 2){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("Latitude",latlng.latitude);
                    intent.putExtra("Longitude",latlng.longitude);
                    intent.putExtra("Location",location);
                    startActivity(intent);

                }
                Toast.makeText(getApplicationContext(),"the latlng is :"+ location,Toast.LENGTH_LONG).show();

            }
        });


    }

    private class postAsyncTask extends AsyncTask<Integer,Void,String>{
        @Override
        protected String doInBackground(Integer... params) {
            HttpPost httpPost = new HttpPost();
            String response = httpPost.postData(postData,"https://rentitapi.herokuapp.com/new_product");
            return response;
        }
        protected void onPostExecute(String response){
            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(location.this,myAds.class);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.MyAds) {
            Intent intent = new Intent(this,myAds.class);
            startActivity(intent);

        } else if (id == R.id.postAds) {
            Intent intent = new Intent(this,PostAdActivity.class);
            startActivity(intent);

        } else if (id == R.id.Wishlist) {
            Intent intent = new Intent(this,wishlist.class);
            startActivity(intent);

        } else if (id == R.id.location) {


        } else if (id == R.id.History) {
            Intent intent =  new Intent(this,history.class);
            startActivity(intent);

        }else if (id == R.id.chat) {
            Intent intent =  new Intent(this,chatActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                //Initialize Google Play Services
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            }

            protected synchronized void buildGoogleApiClient() {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
            }

            @Override
            public void onConnected(Bundle bundle) {

                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(1000 * 60);
                mLocationRequest.setFastestInterval(1000 * 60);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }

            }

            @Override
            public void onConnectionSuspended(int i) {

            }

            @Override
            public void onLocationChanged(Location location) {

                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mCurrLocationMarker.remove();
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Current Position");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mCurrLocationMarker = mMap.addMarker(markerOptions);
                        latlng = latLng;
                    }
                });
                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                latlng = latLng;
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

                //stop location updates
                if (mGoogleApiClient != null) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                }

            }

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {

            }

            public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
            public boolean checkLocationPermission(){
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Asking user if explanation is needed
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);


                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public void onRequestPermissionsResult(int requestCode,
                                                   String permissions[], int[] grantResults) {
                switch (requestCode) {
                    case MY_PERMISSIONS_REQUEST_LOCATION: {
                        // If request is cancelled, the result arrays are empty.
                        if (grantResults.length > 0
                                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                            // permission was granted. Do the
                            // contacts-related task you need to do.
                            if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {

                                if (mGoogleApiClient == null) {
                                    buildGoogleApiClient();
                                }
                                mMap.setMyLocationEnabled(true);
                            }

                        } else {

                            // Permission denied, Disable the functionality that depends on this permission.
                            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                        }
                        return;
                    }

                    // other 'case' lines to check for other permissions this app might request.
                    // You can add here other case statements according to your requirement.
                }
            }

            @Override
            public void onPlaceSelected(Place place) {
                //Toast.makeText(getApplicationContext(),""+place.getLatLng(),Toast.LENGTH_LONG).show();
                //mCurrLocationMarker.remove();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(place.getLatLng());
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                latlng = place.getLatLng();
            }

            @Override
            public void onError(Status status) {

            }

}
