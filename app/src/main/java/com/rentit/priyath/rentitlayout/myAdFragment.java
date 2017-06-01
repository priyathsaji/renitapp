package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import static android.content.Context.MODE_PRIVATE;

public class myAdFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<generalAdDetails> generalAdDetails;
    myAdAdapter adapter;
    Context context;
    ProgressBar progressbar;
    String userId;
    globalData globaldata;

    public myAdFragment() {
        // Required empty public constructor
    }

    public static myAdFragment newInstance(String param1, int param2) {
        myAdFragment fragment = new myAdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Toast.makeText(getContext(),"inside fragment",Toast.LENGTH_LONG).show();
    }

    public void getSavedData() throws IOException, ClassNotFoundException {
        FileInputStream in = context.openFileInput("myads");
        ObjectInputStream oin = new ObjectInputStream(in);
        generalAdDetails = (ArrayList<com.rentit.priyath.rentitlayout.generalAdDetails>) oin.readObject();
        oin.close();
        in.close();


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }

        globaldata = (globalData)getActivity().getApplicationContext();

        View view = inflater.inflate(R.layout.fragment_my_ad, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.MyAdsRecyclerview);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        context = getContext();
        progressbar = (ProgressBar)view.findViewById(R.id.myadprogressbar);
        generalAdDetails = new ArrayList<>();

        int flag = mParam2;
        /*
        if(flag == 0) {
            try {
                getSavedData();
                if (generalAdDetails.size() != 0) {
                    adapter = new myAdAdapter(generalAdDetails, context);
                    recyclerView.setAdapter(adapter);
                    progressbar.setVisibility(View.INVISIBLE);
                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if(generalAdDetails.size() == 0 ){
                progressbar.setProgress(0);
                progressbar.setVisibility(View.VISIBLE);

            }

        }else{
            progressbar.setProgress(0);
            progressbar.setVisibility(View.VISIBLE);
            new myAsyncTask().execute(0);

        }*/

        return view;
    }

    public void onResume(){
        super.onResume();
        new myAsyncTask().execute(0);

    }

    private class removeAdAsyncTask extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... params) {
            return null;
        }
    }

    private class myAsyncTask extends AsyncTask<Integer,Void,String> {

        String response;
        @Override
        protected String doInBackground(Integer... params) {
            HttpGet httpGet = new HttpGet();
            String link = "http://rentitapi.herokuapp.com/get_myAds?id="+globaldata.getUserId();
            try {
                response = httpGet.getData(link);
                Log.i("response",response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();

            }


            return null;
        }

        protected void onPreExecute(){
            generalAdDetails = new ArrayList<>();
        }

        protected void onPostExecute(String response){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject js = jsonArray.getJSONObject(i);
                    com.rentit.priyath.rentitlayout.generalAdDetails gad = new generalAdDetails();
                    gad.primaryImageName = js.getString("image");
                    gad.productId = js.getString("productid");
                    gad.adAvgRating = js.getInt("AverageRating");
                    gad.AdTitle = js.getString("Title");
                    gad.Adcost = js.getInt("Rent");
                    gad.type = js.getInt("type");
                    gad.status = js.getString("Status");
                    gad.Location = js.getString("Location");
                    generalAdDetails.add(gad);
                }

                FileOutputStream out = context.openFileOutput("myads",MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                objectOutputStream.writeObject(generalAdDetails);
                objectOutputStream.flush();
                objectOutputStream.close();
                out.close();
                adapter = new myAdAdapter(generalAdDetails, context) {
                    @Override
                    void removeAd(String productId, int type) {

                    }
                };
                recyclerView.setAdapter(adapter);
                progressbar.setVisibility(View.INVISIBLE);


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
