package com.huhmoon.apparely.retrofitapisubmodule.models;

import com.google.gson.annotations.Expose;

import java.net.URL;

/**
 * Created by ssureymoon on 8/8/15.
 */
public class FoodModel {
    @Expose
    public String title;
    @Expose
    public String preview;

    public FoodModel(String title, String preview){
        this.title = title;
        this.preview = preview;
    }
}
