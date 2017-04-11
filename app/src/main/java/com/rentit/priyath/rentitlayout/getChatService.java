package com.rentit.priyath.rentitlayout;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by priyath on 02-04-2017.
 */

public class getChatService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler;
    HttpURLConnection conn;
    String postDataParams;
    chatUser chatdata;
    ArrayList<chatUser> chatUserDatas;
    HashMap<String,Integer> chatuserstatus;
    int length;
    final static String MY_ACTION = "MY_ACTION";
    final static String NEW_MESSAGE = "NEW_MESSAGE";

    boolean loading = false;
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service startied",Toast.LENGTH_LONG).show();
        handler = new Handler();
        chatuserstatus = new HashMap<>();
        chatUserDatas = new ArrayList<>();
        runnable.run();

        return START_STICKY;
    }



    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            chatStatus.newDataAdded=false;
            if(!loading)
            new MyAsyncTask().execute(0);
            handler.postDelayed(runnable,5000);
        }

    };

    public void onDestroy(){
        Toast.makeText(this,"service stoped",Toast.LENGTH_LONG).show();
    }

    public class MyAsyncTask extends AsyncTask<Integer,Integer,Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {

            try {
                postData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  0;
        }
        void postData() throws IOException {
            String link = "https://rentitapi.herokuapp.com/get_chatusers?toId=priyathsaji";
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
                //JSONObject jsonObject = new JSONObject(response.toString());

                JSONArray jsonArray = new JSONArray(response.toString());
                JSONObject js;
                if(jsonArray.length()>0) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        chatdata = new chatUser();
                        js = jsonArray.getJSONObject(i);

                        chatdata.ownerName = js.getString("name");
                        chatdata.productName = js.getString("productName");
                        chatdata.number = js.getInt("number");
                        chatdata.toId = js.getString("toId");
                        chatdata.fromId = js.getString("fromId");
                        Log.i("name", chatdata.productName);
                        if(chatuserstatus.get(chatdata.fromId)==null) {
                            chatUserDatas.add(chatdata);
                            chatuserstatus.put(chatdata.fromId,chatUserDatas.size()-1);
                            Log.i("size",""+chatUserDatas.size());
                        }else{
                            chatUser temp = chatUserDatas.get(chatuserstatus.get(chatdata.fromId));
                            temp.number += chatdata.number;
                            Log.i("temp",""+temp.number);
                            chatUserDatas.set(chatuserstatus.get(chatdata.fromId),temp);

                        }
                    }
                }
                length = jsonArray.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void onPreExecute(){
            loading = true;
            getChatUserStatus();
            try {
                getChatUserData();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        protected void onPostExecute(Integer page){
            loading = false;
            if(length!=0) {

                try {
                    saveChatUserStatus(chatuserstatus);
                    saveChatUserData(chatUserDatas);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void getChatUserData() throws IOException, ClassNotFoundException {
        FileInputStream in = this.openFileInput("chatusers");
        ObjectInputStream inputStream = new ObjectInputStream(in);
        chatUserDatas = (ArrayList<chatUser>) inputStream.readObject();
        in.close();
        inputStream.close();
    }

    void getChatUserStatus(){
        try {
            File file = new File(getDir("datamap", MODE_PRIVATE), "map");
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            chatuserstatus = (HashMap<String, Integer>) inputStream.readObject();
            inputStream.close();
            Log.i("the recieved is : ",""+chatuserstatus.get("testing"));
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    void saveChatUserStatus(HashMap<String,Integer> data) throws IOException {
        File file = new File(getDir("datamap", MODE_PRIVATE), "map");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(data);
        outputStream.flush();
        outputStream.close();
    }

    void saveChatUserData(ArrayList<chatUser> data) throws IOException {
        FileOutputStream out = this.openFileOutput("chatusers",MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(data);
        oos.close();
        out.close();
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("DATAPASSED",length);
        sendBroadcast(intent);

        intent.setAction(NEW_MESSAGE);
        intent.putExtra("DATAPASSED",length);
        sendBroadcast(intent);

        Toast.makeText(this,"saving Data",Toast.LENGTH_LONG).show();
    }

}
