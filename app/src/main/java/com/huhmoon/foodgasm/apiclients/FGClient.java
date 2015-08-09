package com.huhmoon.foodgasm.apiclients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huhmoon.library.data.FGFoodModel;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ssureymoon on 8/8/15.
 */
public class FGClient {
    private static FoodPornService sFoodPornService;

    public static FoodPornService get() {
        if (sFoodPornService == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(FGFoodModel.FoodResponse.class, new FGFoodModel.FoodDeserializer())
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
                Callback<FGFoodModel.FoodResponse> callback);
    }
}
