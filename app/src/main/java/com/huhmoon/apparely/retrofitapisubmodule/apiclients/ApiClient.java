package com.huhmoon.apparely.retrofitapisubmodule.apiclients;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huhmoon.apparely.retrofitapisubmodule.models.FoodDeserializer;
import com.huhmoon.apparely.retrofitapisubmodule.models.FoodModel;
import com.huhmoon.apparely.retrofitapisubmodule.models.FoodResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by ssureymoon on 8/8/15.
 */
public class ApiClient {
    private static FoodPornService sFoodPornService;

    public static FoodPornService get() {
        if (sFoodPornService == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(FoodResponse.class, new FoodDeserializer())
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://www.reddit.com")
                    .setConverter(new GsonConverter(gson))
                    .build();

            sFoodPornService = restAdapter.create(FoodPornService.class);
        }

        return sFoodPornService;
    }

    public interface FoodPornService {
        @GET("/r/FoodPorn/.json")
        void listFood(
                @Query("sort") String sort,
                @Query("t") String t,
                @Query("limit") String limit,
                Callback<FoodResponse> callback);
    }
}
