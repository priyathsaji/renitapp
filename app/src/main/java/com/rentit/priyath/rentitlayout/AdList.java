package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AdList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressBar progressBar;
    ArrayList<generalAdDetails> AdDetails = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    generalAdDetails gad;
    AdListAdapter adlistadapter;
    EndlessRecyclerViewScrollListener scrollListener;
    int TotalPages,Page,TotalObjects;
    RangeBar rangeBar;
    TextView budgetrange;
    int HouseBudgetmultipler = 5000;
    int max,min;
    int urlnumber;
    Context context;
    String urlarray[] = {"http://rentitapi.herokuapp.com/get_houses","http://rentitapi.herokuapp.com/get_cars","http://rentitapi.herokuapp.com/get_bikes",};
    String url = urlarray[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        urlnumber = intent.getIntExtra("url number",0);
        if(urlnumber<3)
            url = urlarray[urlnumber];
        else {
            url = urlarray[0];
            urlnumber = 0;
        }

        recyclerView = (RecyclerView) findViewById(R.id.AdRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(TotalPages >= page ) {
                    //for (int i = 0; i < 5; i++)
                    //  AdDetails.add(AdDetails.get(i));
                    //adlistadapter.notifyItemRangeInserted(page * 5, AdDetails.size());
                    //adlistadapter = new AdListAdapter(AdDetails);
                    //recyclerView.setAdapter(adlistadapter);
                    //Page = page+1;
                   if (page != 1) {
                        new MyAsyncTask().execute(page);
                        progressBar.setVisibility(View.VISIBLE);
                   }
                }
            }
        };
        budgetrange = (TextView)findViewById(R.id.budgetrange);
        recyclerView.addOnScrollListener(scrollListener);

        //rangeBar = (RangeBar)findViewById(R.id.rangebar);
        //rangeBar.setTickStart(0);
        //rangeBar.setTickEnd(30000);
        //rangeBar.setRangePinsByIndices(0,6);
        max = 6*HouseBudgetmultipler;
        min = 0*HouseBudgetmultipler;

        context = getApplicationContext();

        adlistadapter = new AdListAdapter(AdDetails, context) {
            @Override
            public void refreshdata(int left, int right) {
                int temp1 = right*HouseBudgetmultipler;
                int temp2 = left*HouseBudgetmultipler;
                if(temp1==max && temp2 == min) {

                }else{
                    AdDetails.clear();
                    Toast.makeText(context, "size :" + AdDetails.size() + left + right, Toast.LENGTH_LONG).show();
                    adlistadapter.notifyDataSetChanged();
                    max = right * HouseBudgetmultipler;
                    min = left * HouseBudgetmultipler;
                    new MyAsyncTask().execute(1);
                    progressBar.setProgress(View.VISIBLE);
                }
            }

            @Override
            public int geturlnumber() {
                return urlnumber;
            }
        };
        recyclerView.setAdapter(adlistadapter);


        new MyAsyncTask().execute(1);

        /*rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                String budget = ""+(leftPinIndex*5000)+" Rs to "+(rightPinIndex*5000)+" Rs+";

                max = rightPinIndex*HouseBudgetmultipler;
                min = leftPinIndex*HouseBudgetmultipler;
                budgetrange.setText(budget);
                AdDetails.clear();
                new MyAsyncTask().execute(1);
            }
        });
*/

    }

    void startIntent(){
        Intent intent = new Intent(this,Details1.class);
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
        getMenuInflater().inflate(R.menu.main2, menu);
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

        } else if (id == R.id.Wishlist) {
            Intent intent = new Intent(this,wishlist.class);
            startActivity(intent);

        } else if (id == R.id.location) {
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

    public class MyAsyncTask extends AsyncTask<Integer,Integer,Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {

            if(params[0]>0){
                try {
                    postData(params[0],1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return params[0];
            }else{
                try {
                    postData(1,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        }

        void postData(int page,int urlnumber) throws IOException {
            String link = url+"?page="+page+"&max="+max+"&min="+min;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine())!=null){
                response.append(line);

            }
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                TotalPages = jsonObject.getInt("pages");
                TotalObjects = jsonObject.getInt("total");
                JSONArray jsonArray = jsonObject.getJSONArray("docs");
                JSONObject js;
                //Toast.makeText(getApplicationContext(),js.getString("Status"),Toast.LENGTH_LONG).show();

                for(int i=0;i<jsonArray.length();i++){
                    gad = new generalAdDetails();
                    js = jsonArray.getJSONObject(i);
                    gad.Adcost = js.getInt("Price");
                    gad.adAvgRating = js.getInt("AverageRating");
                    gad.AdTitle = js.getString("Title");
                    gad.primaryImageName = js.getString("image_1");
                    gad.Location = js.getString("Location");
                    AdDetails.add(gad);
                    Log.i("status  :",AdDetails.get(i).Location);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void onProgressUpdate(Integer... progress){
            progressBar.setProgress(0);
        }

        protected void onPostExecute(Integer page){
            progressBar.setVisibility(View.GONE);
            if(page == 0){
                    //recyclerView.setAdapter(adlistadapter);
            }else if(page<=TotalPages){
                if(TotalPages == page && page !=1) {
                    adlistadapter.notifyItemRangeInserted((page * 10)+1,(TotalObjects/(page-1)));
                }else if(page == 1){
                    recyclerView.setAdapter(adlistadapter);
                }else{
                    adlistadapter.notifyItemRangeInserted((page * 10)+1,10);
                }
            }

            Toast.makeText(context,"page :"+page,Toast.LENGTH_LONG).show();
        }
    }


}
