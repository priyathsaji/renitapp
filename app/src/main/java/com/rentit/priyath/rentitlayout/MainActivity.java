package com.rentit.priyath.rentitlayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView grid;
    TextView location;
    String loc;
    Double latitude;
    Double longitude;
    String[] web = {
            "House",
            "Cars",
            "Buses",
            "Bikes and Scooters",
            "Commercial spaces",
            "Auditoriums and events",
            "Light and Sound Systems",
            "Tools and Equipments",
            "Miscelloneous items"

    } ;
    int[] imageId = {
            R.drawable.house,
            R.drawable.car,
            R.drawable.bus,
            R.drawable.bike,
            R.drawable.shop,
            R.drawable.hall,
            R.drawable.speaker,
            R.drawable.tools,
            R.drawable.misc,


    };

    globalData globaldata;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        loc = intent.getStringExtra("Location");
        latitude = intent.getDoubleExtra("Latitude",0);
        longitude = intent.getDoubleExtra("Longitude",0);
        globaldata = (globalData)getApplicationContext();
        location = (TextView)findViewById(R.id.loca);
        location.setText(globaldata.getLocation());

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),location.class);
                intent.putExtra("flag",2);
                startActivity(intent);
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        username = (TextView)header.findViewById(R.id.username);
        username.setText(globaldata.getUsername());
        navigationView.setCheckedItem(R.id.home);




            CustomGrid adapter = new CustomGrid(MainActivity.this, web, imageId);
            grid=(GridView)findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(MainActivity.this, "You Clicked at " +web[position], Toast.LENGTH_SHORT).show();
                    startIntent(position);

                }
            });

        }
    public void startIntent(int position){
        Intent intent = new Intent(getApplicationContext(), AdList.class);
        intent.putExtra("Latitude",latitude);
        intent.putExtra("Longitude",longitude);
        intent.putExtra("Location",loc);
        intent.putExtra("type",position);
        startActivity(intent);

        startActivity(intent);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
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

        } else if (id == R.id.MyAds) {
            Intent intent = new Intent(this,myAds.class);
            intent.putExtra("Latitude",latitude);
            intent.putExtra("Longitude",longitude);
            intent.putExtra("Location",loc);
            intent.putExtra("flag",0);
            startActivity(intent);

            startActivity(intent);

        } else if (id == R.id.postAds) {
            Intent intent = new Intent(this,PostAdActivity.class);
            intent.putExtra("Latitude",latitude);
            intent.putExtra("Longitude",longitude);
            intent.putExtra("Location",loc);
            startActivity(intent);

        } else if (id == R.id.location) {
            Intent intent = new Intent(this,location.class);
            intent.putExtra("Latitude",latitude);
            intent.putExtra("Longitude",longitude);
            intent.putExtra("Location",loc);
            intent.putExtra("flag",2);
            startActivity(intent);

        } else if (id == R.id.History) {
            Intent intent =  new Intent(this,history.class);
            intent.putExtra("Latitude",latitude);
            intent.putExtra("Longitude",longitude);
            intent.putExtra("Location",loc);
            startActivity(intent);

        }else if (id == R.id.chat) {
            Intent intent =  new Intent(this,chatActivity.class);
            intent.putExtra("Latitude",latitude);
            intent.putExtra("Longitude",longitude);
            intent.putExtra("Location",loc);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
