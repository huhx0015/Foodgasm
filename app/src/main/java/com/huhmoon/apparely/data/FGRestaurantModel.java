package com.huhmoon.apparely.data;

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

    public String getImage(){
        return url;
    }
    public String getName(){
        return name;
    }

    public FGRestaurantModel(String name, String url){
        this.name = name;
        this.url = url;
    }


    public static List<FGRestaurantModel> parseRestaurants(JSONArray businesses) throws JSONException {
        List<FGRestaurantModel> restaurantModelList = new ArrayList<FGRestaurantModel>();
        String name;
        String url; //image_url
        JSONObject business;
        for (int i=0;i<businesses.size();i++){
            business = (JSONObject) businesses.get(i);
            name = business.get("name").toString();
            url = business.get("image_url").toString();
            restaurantModelList.add(new FGRestaurantModel(name, url));
            Log.e("TESTTESTname", name);
            Log.e("TESTTESTurl", url);
        }
        return restaurantModelList;
    }

}
