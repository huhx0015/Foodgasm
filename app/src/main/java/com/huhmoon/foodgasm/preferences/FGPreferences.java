package com.huhmoon.foodgasm.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.huhmoon.foodgasm.R;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGPreferences {

    /** SHARED PREFERENCES FUNCTIONALITY _______________________________________________________ **/

    // initializePreferences(): Initializes and returns the SharedPreferences object.
    public static SharedPreferences initializePreferences(String prefType, Context context) {
        return context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
    }

    // setDefaultPreferences(): Sets the shared preference values to default values.
    public static void setDefaultPreferences(String prefType, Boolean isReset, Context context) {

        // Determines the appropriate resource file to use.
        int prefResource = R.xml.fg_options;

        // Resets the preference values to default values.
        if (isReset) {
            SharedPreferences preferences = initializePreferences(prefType, context);
            preferences.edit().clear().apply();
        }

        // Sets the default values for the SharedPreferences object.
        PreferenceManager.setDefaultValues(context, prefType, Context.MODE_PRIVATE, prefResource, true);
    }

    /** GET PREFERENCES FUNCTIONALITY __________________________________________________________ **/

    // getCurrentImage(): Retrieves the current image value from preferences.
    public static String getCurrentImage(SharedPreferences preferences) {
        return preferences.getString("fg_current_image", ""); // Retrieves the current image setting.
    }

    /** SET PREFERENCES FUNCTIONALITY __________________________________________________________ **/

    // setCurrentImage(): Sets the "fg_current_image" value to preferences.
    public static void setCurrentImage(String fileName, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("fg_current_image", fileName); // Sets the current image setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }
}
