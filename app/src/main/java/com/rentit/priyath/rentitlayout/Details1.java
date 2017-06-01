package com.rentit.priyath.rentitlayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Details1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Integer> horizontalList;
    FloatingActionButton fab;
    int type;
    String id;
    TextView title,loc,rent,sub1,sub2,sub3,sub4;
    RatingBar averageRating;
    ImageView primaryImage;
    String image1,image2,image3,image4,image5;

    Button viewImages;
    globalData globaldata;
    TextView location;
    String comment;
    Button button;
    String name,email,phone;
    String ownerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        button = (Button)findViewById(R.id.Reviews);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                intent.putExtra("rating",comment);
                startActivity(intent);
            }
        });

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

        title = (TextView)findViewById(R.id.title);
        loc = (TextView)findViewById(R.id.location);
        rent = (TextView)findViewById(R.id.Rent);
        sub1 = (TextView)findViewById(R.id.sub1);
        sub2 = (TextView)findViewById(R.id.sub2);
        sub3 = (TextView)findViewById(R.id.sub3);
        sub4 = (TextView)findViewById(R.id.sub4);
        averageRating = (RatingBar)findViewById(R.id.aveargeRating);
        primaryImage = (ImageView)findViewById(R.id.primaryImage);
        viewImages = (Button)findViewById(R.id.images);
        viewImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            userDetailsAsyncTask task = new userDetailsAsyncTask();
                task.execute(ownerId);
            }
        });

        primaryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),imageVIewer.class);
                intent.putExtra("image1",image1);
                intent.putExtra("image2",image2);
                intent.putExtra("image3",image3);
                intent.putExtra("image4",image4);
                intent.putExtra("image5",image5);
                getApplicationContext().startActivity(intent);
            }
        });

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        type = intent.getIntExtra("type",0);

        myAsyncTask t = new myAsyncTask();
        t.execute(0);


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
        getMenuInflater().inflate(R.menu.details1, menu);
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

        }  else if (id == R.id.location) {
            Intent intent = new Intent(this,location.class);
            startActivity(intent);

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

    public class myAsyncTask extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... params) {
            HttpGet httpGet = new HttpGet();
            String response = null;
            try {
                response= httpGet.getData("http://rentitapi.herokuapp.com/purticular_product?type="+type+"&id="+id);
                Log.i("response",response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  response;
        }

        protected void onPostExecute(String response){
            try {
                JSONObject js = new JSONObject(response);
                Picasso.with(getApplicationContext()).load("https://s3.ap-south-1.amazonaws.com/rentit-profile-pics/"+js.getString("image_1")).fit().centerCrop().into(primaryImage);
                title.setText(js.getString("Title"));
                loc.setText(js.getString("Location"));
                rent.setText("Rent :  "+js.getString("Rent"));
                image1 = js.getString("image_1");
                image2 = js.getString("image_2");
                image3 = js.getString("image_3");
                image4 = js.getString("image_4");
                image5 = js.getString("image_5");
                ownerId = js.getString("ownerDetails");
                averageRating.setRating((float) js.getDouble("AverageRating"));
                String temp;
                if(type == 0){
                    temp = "Area :  " + js.getString("Area");
                    sub1.setText(temp);
                    temp = "Bedrooms :  " + js.getString("Bedrooms");
                    sub2.setText(temp);
                    temp = "Bathrooms :  "+ js.getString("Bathrooms");
                    sub3.setText(temp);
                    temp = "Description :  "+ js.getString("Description");
                    sub4.setText(temp);
                }else if(type == 1){
                    temp = "Brand :  "+ js.getString("Brand");
                    sub1.setText(temp);
                    temp ="Model :  :"+ js.getString("Model");
                    sub2.setText(temp);
                    temp ="Year :  "+js.getString("Year");
                    sub3.setText(temp);
                    temp = "Description :  "+ js.getString("Description");
                    sub4.setText(temp);
                }else if(type == 2){
                    temp = "Number of Wheels :  "+js.getString("NumberOfWheels");
                    sub1.setText(temp);
                    temp = "Number of Simeats :  "+js.getString("NumberOfSeats");
                    sub2.setText(temp);
                    temp = "Year :  "+js.getString("Year");
                    sub3.setText(temp);
                    temp = "Description :  "+ js.getString("Description");
                    sub4.setText(temp);
                }else if(type == 3){
                    temp = "Brand :  "+ js.getString("Brand");
                    sub1.setText(temp);
                    temp = "Model :  :"+ js.getString("Model");
                    sub2.setText(temp);
                    temp = "Year :  "+js.getString("Year");
                    sub3.setText(temp);
                    temp = "Description :  "+ js.getString("Description");
                    sub4.setText(temp);
                }else if(type == 4){
                    temp = "Area :  "+js.getString("Area");
                    sub1.setText(temp);
                    temp = "Parking Capacity  :" +js.getString("ParkingCapacity");
                    sub2.setText(temp);
                    temp = "Year :  "+js.getString("Year");
                    sub3.setText(temp);
                    temp = "Description :  "+ js.getString("Description");
                    sub4.setText(temp);
                }else if(type == 5){
                    temp = "Seating Capacity :  "+js.getString("SeatingCapacity");
                    sub1.setText(temp);
                    temp = "Parking Capacity :  "+js.getString("ParkingCapacity");
                    sub2.setText(temp);
                    temp = "Area :  "+js.getString("Area");
                    sub3.setText(temp);
                    temp = "Description :  "+ js.getString("Description");
                    sub4.setText(temp);
                }else if(type == 6){
                    temp ="Power :  "+js.getString("powerSpecification");
                    sub1.setText(temp);
                    temp = "Description :  "+ js.getString("Description");
                    sub2.setText(temp);
                    sub3.setVisibility(View.GONE);
                    sub4.setVisibility(View.GONE);
                }else if(type == 7){
                    temp = "Tool Type :  "+js.getString("toolType");
                    sub1.setText(temp);
                    temp = "Power :  "+js.getString("powerSpecification");
                    sub2.setText(temp);
                    temp = "Description :  "+ js.getString("Description");
                    sub3.setText(temp);
                    sub4.setVisibility(View.GONE);
                }else if(type == 8){
                    temp = "Description :  "+ js.getString("Description");
                    sub1.setText(temp);
                    sub2.setVisibility(View.GONE);
                }
                JSONArray comments = js.getJSONArray("comments");
                comment = String.valueOf(comments);
                Log.i("comments :", String.valueOf(comments));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    TextView owneremail,ownername,ownerphone;
    private void initiatePopupWindow() {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogview = inflater.inflate(R.layout.userdetailspopup, null);

            owneremail = (TextView)dialogview.findViewById(R.id.owneremail);
            ownername = (TextView)dialogview.findViewById(R.id.ownername);
            ownerphone = (TextView)dialogview.findViewById(R.id.ownerphone);

            owneremail.setText(email);
            ownername.setText(name);
            ownerphone.setText(phone);
            dialogBuilder.setView(dialogview);
            dialogBuilder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phone));
                    startActivity(callIntent);

                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();

            dialogBuilder.setTitle("Review");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class userDetailsAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            HttpGet httpGet = new HttpGet();
            try {
                String response = httpGet.getData("http://rentitapi.herokuapp.com/get_user?id="+params[0]);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String response){
            if(response!=null) {
                try {
                    JSONObject js = new JSONObject(response);
                    name = js.getString("name");
                    phone = js.getString("phoneNumber");
                    email = js.getString("emailId");
                    Log.i("email",email);
                    initiatePopupWindow();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

