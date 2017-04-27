package com.rentit.priyath.rentitlayout;

import android.*;
import android.Manifest;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class history extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;
    ArrayList<proposalAndHistoryData> proposalAndHistoryDatas;
    String url;
    EditText review;
    RatingBar ratingBar;

    TextView location;
    globalData globaldata;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.History);


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
        View header = navigationView.getHeaderView(0);
        username = (TextView)header.findViewById(R.id.username);
        username.setText(globaldata.getUsername());

        context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerview);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar = (ProgressBar) findViewById(R.id.historyprogressbar);

        proposalAndHistoryDatas = new ArrayList<>();

        url = "http://192.168.43.87:5000/get_history?Id="+globaldata.getUserId();
        myAsyncTask task = new myAsyncTask();
        task.execute(0);
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
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.History) {
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.MyAds) {
            Intent intent = new Intent(this, myAds.class);
            startActivity(intent);

        } else if (id == R.id.postAds) {
            Intent intent = new Intent(this, PostAdActivity.class);
            startActivity(intent);

        } else if (id == R.id.location) {
            Intent intent = new Intent(this, location.class);
            intent.putExtra("flag", 2);
            startActivity(intent);

        } else if (id == R.id.History) {
            Intent intent = new Intent(this,history.class);
            startActivity(intent);


        } else if (id == R.id.chat) {
            Intent intent = new Intent(this, chatActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class myAsyncTask extends AsyncTask<Integer, Void, String> {


        @Override
        protected String doInBackground(Integer... params) {
            HttpGet httpGet = new HttpGet();
            try {
                String Response = httpGet.getData(url);
                return Response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            if (result.charAt(0) == '[') {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = null;
                        js = jsonArray.getJSONObject(i);
                        proposalAndHistoryData data = new proposalAndHistoryData();
                        data.name = "";
                        data.productname = js.getString("productName");
                        data.phoneNumber = "";
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
                proposalAdapter adapter = new proposalAdapter(proposalAndHistoryDatas, context) {
                    @Override
                    public void button1function(proposalAndHistoryData data) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:9526849323"));
                        ActivityCompat.requestPermissions(history.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                        //startActivity(callIntent);


                    }

                    @Override
                    public void button2function(proposalAndHistoryData data) {
                        showDialog(data);
                    }

                    @Override
                    public void button3function(proposalAndHistoryData data) {
                        url = "http://192.168.43.87:5000/terminate_usage?type="+data.type+"&productId="+data.productId;
                        myAsyncTask task = new myAsyncTask();
                        task.execute(0);

                    }
                };
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);


            }else{
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            }
        }
    }
    void showDialog(proposalAndHistoryData data){
        //url = "http://192.168.43.87:5000/new_rating?type="+data.type+"&name="+"priyath";
        url = "http://192.168.43.87:5000/new_rating?type="+data.type+"&id="+data.productId+"&name="+globaldata.getUsername();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater =  this.getLayoutInflater();
        final View dialogview = inflater.inflate(R.layout.dialogbox,null);
        review = (EditText)dialogview.findViewById(R.id.review);
        ratingBar = (RatingBar)dialogview.findViewById(R.id.ratingbar) ;
        dialogBuilder.setView(dialogview);

        dialogBuilder.setTitle("Review");
        dialogBuilder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              url = url + "&rating="+ratingBar.getRating()+"&review="+review.getText().toString();
                myAsyncTask task = new myAsyncTask();
                task.execute(1);

            }
        });

        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:9526849323"));
                    startActivity(callIntent);
                }
                break;
        }
    }
}
