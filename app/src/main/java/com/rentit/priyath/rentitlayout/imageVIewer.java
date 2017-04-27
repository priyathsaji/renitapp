package com.rentit.priyath.rentitlayout;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class imageVIewer extends AppCompatActivity {
    ArrayList<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);


        images = new ArrayList<>();
        Intent intent = getIntent();
        images.add(intent.getStringExtra("image1"));
        images.add(intent.getStringExtra("image2"));
        images.add(intent.getStringExtra("image3"));
        images.add(intent.getStringExtra("image4"));
        images.add(intent.getStringExtra("image5"));
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPageAndroid);
        imageViewAdapter adapterView = new imageViewAdapter(this,images);
        mViewPager.setAdapter(adapterView);
    }
}
