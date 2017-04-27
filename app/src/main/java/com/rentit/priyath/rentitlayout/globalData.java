package com.rentit.priyath.rentitlayout;

import android.app.Application;
import android.app.LoaderManager;

import java.util.ArrayList;

/**
 * Created by priyath on 23-04-2017.
 */

public class globalData extends Application {
    private ArrayList<String> messages;
    private String Location;
    private Double latitude;
    private Double longitude;
    private String Username;
    private String UserId;
    private String phonenumber;
    private String emailId;

    public globalData(){
        messages = new ArrayList<>();
    }


    public void addMessage(String message){
        messages.add(message);
    }
    public void addEmalid(String email){emailId = email;}
    public void addLocation(String Location){this.Location = Location;}
    public void addLatitude(Double latitude){this.latitude = latitude;}
    public void addlogitude(Double longitude){this.longitude = longitude;}
    public void addUsername(String Username){this.Username = Username;}
    public void addUserId(String UserId){this.UserId = UserId;}
    public void addPhoneNumeber(String phonenumber){this.phonenumber = phonenumber;}

    public String getEmailId(){return emailId;};
    public String getLocation(){return Location;}
    public Double getLatitude(){return latitude;}
    public Double getLongitude(){return longitude;}
    public String getUsername(){return Username;}
    public String getUserId(){return UserId;}
    public String getphonenumber(){return phonenumber;}
    public String getMessage(int i){
        return messages.get(i);
    }
    public void clear(){
        messages.clear();
    }
    public int size(){
        return messages.size();
    }

}
