package com.huhmoon.apparely.retrofitapisubmodule.models;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by ssureymoon on 8/8/15.
 */
public class FoodDeserializer implements JsonDeserializer<FoodResponse> {
    @Override
    public FoodResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        return new Gson().fromJson(je, FoodResponse.class);
    }
}
