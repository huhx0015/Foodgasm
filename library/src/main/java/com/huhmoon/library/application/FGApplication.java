package com.huhmoon.library.application;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import com.huhmoon.library.ui.FGToast;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGApplication extends Application {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // DATABASE VARIABLES
    private static final String DATABASE_NAME = "fg_shortcuts.db";

    /** INSTANCE VARIABLES _____________________________________________________________________ **/

    // GTNApplication(): Static variable for easy access to process global Application instance.
    public static FGApplication instance; { instance = this; }

    /** APPLICATION FUNCTIONALITY ______________________________________________________________ **/

    // prepareNavigationIntent(): Queries the shortcut from the database and launches an Intent
    // that starts Google Maps in navigation mode.
    public void prepareNavigationIntent(String address, String type) {

        // Displays a Toast message indicating that Navigation Mode is launching.
        FGToast.toastyPopUp("Launching Google Maps to direct you to your restaurant: " + "\n" + address, this);

        // Holds the URL request string that is processed by Google Maps.
        String url = "google.navigation:q=" + address + "&mode=d";

        // Launches the customized Google Maps intent directly in navigation mode.
        Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i); // Launches the intent to Google Maps.
    }
}