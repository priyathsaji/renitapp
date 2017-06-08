package com.rentit.priyath.rentitlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class chatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    chatUser chatUser;
    ArrayList<chatUser> chatUsers;
    chatuseradapter adapter;
    HashMap<String,Integer> chat;

    TextView location;
    globalData globaldata;
    TextView username;

    public static final String MESSAGE_RECEIVED = "message_received";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.chat);


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

        recyclerView = (RecyclerView)findViewById(R.id.Chatusers);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatUsers = new ArrayList<>();

        globalData globalData = (com.rentit.priyath.rentitlayout.globalData)getApplicationContext();
        globalData.clear(); // clearing the notification messages from global data
        registerReceiver();

    }

    public void onResume(){
        super.onResume();
        chatUsers.clear();
        new Request().execute("priyathsaji");
    }

    public class UserComparator implements Comparator<chatUser> {
        @Override
        public int compare(chatUser u1, chatUser u2) {
            if(u1.number > u2.number){
                return -1;
            }else if(u1.number < u2.number){
                return 1;
            }else{
                return 0;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
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
            Toast.makeText(this,"loged out",Toast.LENGTH_LONG).show();
            File file =  new File("userdetails");
            file.delete();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
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
            Intent intent = new Intent(this, PostAdActivity.class);
            startActivity(intent);

        } else if (id == R.id.location) {
            Intent intent = new Intent(this,location.class);
            intent.putExtra("flag",2);
            startActivity(intent);

        } else if (id == R.id.History) {

        }else if (id == R.id.chat) {
            Intent intent =  new Intent(this,chatActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_RECEIVED);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MESSAGE_RECEIVED)) {
                chatUsers.clear();
                new Request().execute("priyathsaji");
            }
        }
    };


    public class Request extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            HttpGet httpGet = new HttpGet();
            String response = null;
            // String link = "https://rentitapi.herokuapp.com/chat_from?toId="+toId+"&fromId"+fromId;
            String link = "http://rentitapi.herokuapp.com/get_chatusers?toId="+globaldata.getUserId();
            try {
                response=httpGet.getData(link);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.charAt(0)=='['){
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        chatUser chatuser  = new chatUser();
                        chatuser.productName = js.getString("productName");
                        chatuser.toId = js.getString("toId");
                        chatuser.fromId = js.getString("fromId");
                        chatuser.number =js.getInt("number");
                        chatuser.ownerName = js.getString("name");
                        chatUsers.add(chatuser);
                    }
                    Collections.sort(chatUsers, new UserComparator());
                    adapter = new chatuseradapter(chatUsers,getApplicationContext());
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



/*
    void getChatUserData() throws IOException, ClassNotFoundException {
        FileInputStream in = this.openFileInput("chatusers");
        ObjectInputStream inputStream = new ObjectInputStream(in);
        ArrayList<chatUser> chUsers = new ArrayList<>();

        chUsers = (ArrayList<chatUser>) inputStream.readObject();
        chatUsers.clear();
        for(int i=0;i<chUsers.size();i++)
            chatUsers.add(chUsers.get(i));
        Collections.sort(chatUsers, new UserComparator());
        in.close();
        inputStream.close();

    }
    void saveChatData(HashMap<String,Integer> data) throws IOException {
        File file = new File(getDir("datamap", MODE_PRIVATE), "map");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(data);
        outputStream.flush();
        outputStream.close();
        Log.i("testing",""+data.get("testing"));
        //Toast.makeText(this,"the testing is :"+data.get("testing"),Toast.LENGTH_LONG).show();
    }
    void saveChatUserData(ArrayList<chatUser> data) throws IOException {
        FileOutputStream out = this.openFileOutput("chatusers",MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(data);
        oos.close();
        out.close();

        Toast.makeText(this,"saving Data",Toast.LENGTH_LONG).show();
    }

    */

}
