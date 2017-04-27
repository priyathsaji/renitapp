package com.rentit.priyath.rentitlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ratingData> ratingDatas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ratingDatas = new ArrayList<>();
        Intent intent = getIntent();
        String response = intent.getStringExtra("rating");
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject js = jsonArray.getJSONObject(i);
                ratingData r = new ratingData();
                r.username = js.getString("userName");
                r.comment = js.getString("review");
                r.rating = js.getInt("rating");
                ratingDatas.add(r);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView)findViewById(R.id.ratingRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        ratingAdapter adapter = new ratingAdapter(this,ratingDatas);
        recyclerView.setAdapter(adapter);
    }

}
