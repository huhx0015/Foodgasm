package com.huhmoon.apparely.intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.huhmoon.apparely.ui.toast.FGToast;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGNavIntent {

    /** INTENT FUNCTIONALITY ___________________________________________________________________ **/

    // launchNavigation(): This method sets up an Intent to launch Google Maps in Navigation mode.
    public static void launchNavigation(String address, Activity activity) {

        // Displays a Toast message, notifying the user that navigation mode is being launched.
        FGToast.toastyPopUp("Taking to the restaurant:\n" + address, activity);

        // Holds the URL request string that is processed by Google Maps.
        String url = "google.navigation:q=" + address + "&mode=d";

        // Launches the customized Google Maps intent directly in navigation mode.
        Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i); // Launches the intent to Google Maps.
    }
}