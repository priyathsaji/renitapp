package com.rentit.priyath.rentitlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

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
    MyReceiver myReceiver;
    chatuseradapter adapter;
    HashMap<String,Integer> chat;

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

        recyclerView = (RecyclerView)findViewById(R.id.Chatusers);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatUsers = new ArrayList<>();
        try {
            getChatUserData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String,Integer> map = new HashMap<>();
        map.put("testing",1);
        try {
            saveChatUserData(chatUsers);
            saveChatData(map);
        } catch (IOException e) {
            e.printStackTrace();
        }


        adapter = new chatuseradapter(chatUsers,this);
        recyclerView.setAdapter(adapter);

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getChatService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        Intent intent = new Intent(this,getChatService.class);
        startService(intent);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



    }
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


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            Toast.makeText(arg0,"recieved something",Toast.LENGTH_LONG).show();
            int datapassed = arg1.getIntExtra("DATAPASSED", 0);
            try {
                getChatUserData();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();


        }
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
            super.onBackPressed();
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

}
