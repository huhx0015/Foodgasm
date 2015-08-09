package com.huhmoon.foodgasm.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.huhmoon.library.ui.FGToast;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGNavIntent {

    /** INTENT FUNCTIONALITY ___________________________________________________________________ **/

    // checkForAppInstall(): Checks to see if the application is installed on the device. Displays
    // a Toast message.
    public static Boolean checkForAppInstalled(Context context) {

        // Sets up the PackageManager class object to check the device's installed applications.
        PackageManager pm = context.getPackageManager();
        String packName = "com.google.android.apps.maps"; // "GOOGLE MAPS" package name.
        Boolean isInstalled;

        // Checks to see if the package is installed or not.
        try {
            pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        }

        // NameNotFoundException handler.
        catch (PackageManager.NameNotFoundException e) { isInstalled = false; }

        // Displays a Toast message, notifying the user that Google Maps needs to be installed.
        if (!isInstalled) {
            FGToast.toastyPopUp("GOOGLE MAPS is not installed. Please install GOOGLE MAPS.", context);
        }

        return isInstalled;
    }

    // launchNavigation(): This method sets up an Intent to launch Google Maps in Navigation mode.
    public static void launchNavigation(String address, Activity activity) {

        // Check if Google Maps is installed.
        if (checkForAppInstalled(activity)) {

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
}