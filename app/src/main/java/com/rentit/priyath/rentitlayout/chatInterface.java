package com.rentit.priyath.rentitlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class chatInterface extends AppCompatActivity {
    ArrayList<chatData> chatDatas;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    chatadapter adapter;
    EditText toMessage;
    Button sentButton;
    MyReceiver myReceiver;
    String fromId;
    String toId;
    chatData d;
    String response;
    Request r;
    globalData globaldata;
    String productOwner;
    TextView productowner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_interface);

        Intent getintent;
        getintent =  getIntent();
        fromId = getintent.getStringExtra("fromId");
        productOwner = getintent.getStringExtra("ownername");
        productowner = (TextView)findViewById(R.id.productOwner);
        productowner.setText(productOwner);
        globaldata = (globalData)getApplicationContext();
        toId = globaldata.getUserId();
        recyclerView = (RecyclerView) findViewById(R.id.ChatRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatDatas = new ArrayList<>();
        chatDatas = getChatData();
        adapter = new chatadapter(chatDatas,this);
        recyclerView.setAdapter(adapter);
        if(chatDatas.size()>=1)
        recyclerView.smoothScrollToPosition(chatDatas.size()-1);

        toMessage = (EditText)findViewById(R.id.toMessage);
        sentButton = (Button)findViewById(R.id.sentButton);
        sentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = new chatData();
                d.message = String.valueOf(toMessage.getText());
                d.name = globaldata.getUsername();
                d.type = 1;
                d.toId = fromId;//product owner
                d.fromId = globaldata.getUserId();
                d.productName = "something new";
                chatDatas.add(d);
                adapter.notifyDataSetChanged();
                toMessage.setText("");
                recyclerView.smoothScrollToPosition(chatDatas.size()-1);
                try {
                    saveChatData(chatDatas);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Request().execute(1);

            }
        });
        registerReceiver();

        r = new Request();
        r.execute(0);


       // myReceiver = new MyReceiver();
        //IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addAction(getChatService.NEW_MESSAGE);
        //registerReceiver(myReceiver, intentFilter);
    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(chatActivity.MESSAGE_RECEIVED);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(chatActivity.MESSAGE_RECEIVED)) {
                r.cancel(true);
                r = new Request();
                r.execute(0);

            }
        }
    };


    void saveChatData(ArrayList<chatData> data) throws IOException {
        FileOutputStream out = this.openFileOutput(fromId,MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(data);
        oos.close();
        out.close();
        Toast.makeText(this,"saving Data",Toast.LENGTH_LONG).show();
    }

    ArrayList<chatData> getChatData(){
        ArrayList<chatData> chdata = new ArrayList<>();
        try {

            FileInputStream in = openFileInput(fromId);
            ObjectInputStream ois = new ObjectInputStream(in);

            chdata = (ArrayList<chatData>) ois.readObject();
            ois.close();
            in.close();
            //Toast.makeText(this,"getting Data"+chatDatas.size(),Toast.LENGTH_LONG).show();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return chdata;
//;
    }
    public void onBackPressed(){
        r.cancel(true);
        Intent intent = new Intent(this,chatActivity.class);
        startActivity(intent);
    }

    public class Request extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... params) {
            if(params[0] == 1){
                JSONObject postparams = new JSONObject();
                try {
                    postparams.put("fromId",d.fromId);
                    postparams.put("toId",d.toId);
                    postparams.put("message",d.message);
                    postparams.put("name",d.name);
                    postparams.put("productName",d.productName);
                    HttpPost httpPost = new HttpPost();
                    String response = httpPost.postData(postparams,"http://rentitapi.herokuapp.com/chat_to");
//                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    return response;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (params[0]==0){
                HttpGet httpGet = new HttpGet();
               // String link = "https://rentitapi.herokuapp.com/chat_from?toId="+toId+"&fromId"+fromId;
                String link = "http://rentitapi.herokuapp.com/chat_from?fromId="+fromId+"&toId="+toId;
                try {
                    response=httpGet.getData(link);
                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        protected  void onPreExecute(){
            //Intent intent = new Intent(getApplicationContext(),getChatService.class);
            //stopService(intent);
        }

        protected void onCancelled(){
            Log.i("canceled","true");
        }

        @Override
        protected void onPostExecute(String result) {
            if(result !=null) {
                if (result.charAt(0) == '[') {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject js = jsonArray.getJSONObject(i);
                            chatData chdata = new chatData();
                            chdata.message = js.getString("message");
                            chdata.toId = js.getString("toId");
                            chdata.fromId = js.getString("fromId");
                            chdata.name = js.getString("name");
                            chatDatas.add(chdata);
                        }
                        if(chatDatas.size()!=0) {
                            productowner.setText(chatDatas.get(0).name);
                            saveChatData(chatDatas);
                            adapter.notifyDataSetChanged();

                            recyclerView.scrollToPosition(chatDatas.size() - 1);
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //Intent intent = new Intent(getApplicationContext(),getChatService.class);
            //stopService(intent);
        }
    }


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            int datapassed = arg1.getIntExtra("DATAPASSED", 0);
            new Request().execute(0);



        }
    }



}
