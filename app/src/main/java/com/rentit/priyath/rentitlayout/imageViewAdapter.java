package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by priyath on 25-04-2017.
 */

public class imageViewAdapter extends PagerAdapter {
    ArrayList<String> imageNames;
    Context context;

    public imageViewAdapter(Context context, ArrayList<String> imageNames){
        this.context = context;
        this.imageNames = imageNames;

    }
    @Override
    public int getCount() {
        return imageNames.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        ImageView mImageView = new ImageView(context);
        Picasso.with(context).load("https://s3.ap-south-1.amazonaws.com/rentit-profile-pics/"+imageNames.get(i)).into(mImageView);
        //mImageView.setImageResource(R.drawable.default_image);
        container.addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((ImageView) obj);
    }
}
