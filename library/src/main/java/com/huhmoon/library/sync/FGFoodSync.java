package com.huhmoon.library.sync;

import android.util.Log;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.gson.Gson;
import com.huhmoon.library.data.FGFoodModel;

import java.util.LinkedList;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGFoodSync {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // GSON VARIABLES
    public static Gson gson = new Gson();

    // LOGGING VARIABLES
    public static final String LOG_TAG = FGFoodSync.class.getSimpleName();

    /** SYNCHRONIZATION FUNCTIONALITY __________________________________________________________ **/

    // send(): Prepares the data to be sent to the paired Android device.
    public static void send(final PutDataMapRequest wearDataMap, LinkedList<FGFoodModel> foods) {

        Log.d(LOG_TAG, "Sending food data list to wear.");

        int numberOfFoods = foods.size(); // The number of foods to send.

        // Sends the number of foods that are being sent. This is needed for the Android Wear
        // device to know how many foods it should expect to receive.
        wearDataMap.getDataMap().putInt("fg_number_of_foods", numberOfFoods);

        // Prepares each shortcut for data transmission to the Android Wear device.
        for (int i = 0; i < numberOfFoods; i++) {

            // Converts FGFoodModel data into a String using Gson to Json functionality.
            String currentFood = gson.toJson(foods.get(i));

            // Places the data into a data sync form.
            Log.d(LOG_TAG, "Sending food data to wear: " + i);
            wearDataMap.getDataMap().putString("fg_shortcut_" + i, currentFood);
        }
    }

    // receive(): Retrieves the individual data parameters that were sent from the paired Android
    // device.
    public static LinkedList<FGFoodModel> receive(final DataItem item) {

        Log.d(LOG_TAG, "receive(): Item URI: " + item.getUri());

        // Retrieves the data sync path from the item.
        final String path = item.getUri() != null ? item.getUri().getPath() : null;

        // If the data sync path does not match the expected path, the function returns null.
        if (path != null) {

            if (!path.endsWith("/fgdata")) {
                Log.d(LOG_TAG, "ERROR: receive(): Ignoring item not ending in /fgdata: " + item.getUri());
                return null;
            }
        }

        // Assembles the DataMapItem from the DataItem object.
        DataMapItem dataMapItem = DataMapItem.fromDataItem(item);
        Log.d(LOG_TAG, "receive(): dataMapItem: " + dataMapItem);

        // Retrieves the number of foods being received from the paired Android device.
        final int numberOfShortcuts = dataMapItem.getDataMap().getInt("gtn_number_of_shortcuts");

        // Prepares a new LinkedList object containing FGFoodModel received from the mobile device.
        LinkedList<FGFoodModel> foods = new LinkedList<>();

        // Processes each received shortcut and adds it into the LinkedList object.
        for (int i = 0; i < numberOfShortcuts; i++) {

            String foodJson = dataMapItem.getDataMap().getString("fg_food_" + i);

            // Converts the Json string into a FGFoodModel object.
            FGFoodModel recievedFoods = gson.fromJson(foodJson, FGFoodModel.class);

            // Checks to see if the object is null. If so, the function is terminated.
            if (null == recievedFoods)  {
                Log.d(LOG_TAG, "ERROR: receive(): Json string was null.");
                return null;
            }

            foods.add(recievedFoods); // Adds the received shortcut into the FGFoodModel list.
        }

        return foods; // Returns the LinkedList object.
    }
}