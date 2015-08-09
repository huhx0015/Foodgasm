package com.huhmoon.foodgasm.data;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public class FGRestaurantModel {
    String name;
    String url;
    String address;

    public String getImage(){ return url; }
    public String getName(){ return name; }
    public String getAddress(){ return address; }

    public FGRestaurantModel(String name, String url, String address){
        this.name = name;
        this.url = url;
        this.address = address;
    }

    public static List<FGRestaurantModel> parseRestaurants(JSONArray businesses) throws JSONException {
        List<FGRestaurantModel> restaurantModelList = new ArrayList<FGRestaurantModel>();
        String name;
        String url; //image_url
        JSONObject business;
        JSONObject location;
        JSONArray diplayAddresses;
        String diplayAddress;

        for (int i=0;i<businesses.size();i++){
            business = (JSONObject) businesses.get(i);
            name = business.get("name").toString();
            url = business.get("image_url").toString();
            location = (JSONObject) business.get("location");
            diplayAddresses = (JSONArray) location.get("display_address");
            diplayAddress = "";
            for (int j=0;j<diplayAddresses.size();j++) {
                diplayAddress += diplayAddresses.get(j);
            }
            restaurantModelList.add(new FGRestaurantModel(name, url, diplayAddress));
            Log.e("TESTTESTname", name);
            Log.e("TESTTESTurl", url);
            Log.e("TESTTESaddress", diplayAddress);
        }
        return restaurantModelList;
    }

}
