package com.rentit.priyath.rentitlayout;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by priyath on 06-04-2017.
 */

public class placeFinder {
    double lat;
    double lng;

    String getLocality(LatLng latlng, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latlng.latitude,latlng.longitude,1);
        return addresses.get(0).getLocality();
    }

    String getFeatureName(LatLng latLng, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        return addresses.get(0).getFeatureName();
    }

    String getLocation(Double latitude,Double longitude, Context context) throws IOException{
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
        return addresses.get(0).getLocality();
    }
}
