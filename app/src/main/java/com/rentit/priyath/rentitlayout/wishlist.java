package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class wishlist extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Context context;
    ArrayList<proposalAndHistoryData> proposalAndHistoryDatas;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setCheckedItem(R.id.Wishlist);
        context = getApplicationContext();
        recyclerView = (RecyclerView)findViewById(R.id.wishlistRecyclerView);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        proposalAndHistoryDatas = new ArrayList<>();
        url = "http://192.168.43.49:5000/get_wishlist?id=some";

        wishlistfetch task = new wishlistfetch();
        task.execute(1);
    }

   public class wishlistfetch extends AsyncTask<Integer,Void,String>{

       @Override
       protected String doInBackground(Integer... params) {
           HttpGet httpGet = new HttpGet();
           try {
               String response = httpGet.getData(url);
               return response;
           } catch (IOException e) {
               e.printStackTrace();
           }
           return null;
       }

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);
           if(s!=null) {
               Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
               try {
                   JSONArray jsonArray = new JSONArray(s);
                   for (int i = 0; i < jsonArray.length(); i++) {
                       proposalAndHistoryData data = new proposalAndHistoryData();
                       JSONObject js = jsonArray.getJSONObject(i);
                       data.name = js.getString("");
                       data.name = "";
                       data.productname = js.getString("productName");
                       data.phoneNumber = "";
                       data.image = js.getString("image");
                       data.Rent = js.getInt("rent");
                       data.CusomerId = js.getString("Id");
                       data.OwnerId = js.getString("ownerId");
                       data.Status = js.getString("status");
                       data.productId = js.getString("productId");
                       data.type = js.getInt("type");
                       data.isproposal = false;
                       proposalAndHistoryDatas.add(data);

                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }

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
        getMenuInflater().inflate(R.menu.wishlist, menu);
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

        }  else if (id == R.id.location) {
            Intent intent = new Intent(this,location.class);
            intent.putExtra("flag",2);
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
}
