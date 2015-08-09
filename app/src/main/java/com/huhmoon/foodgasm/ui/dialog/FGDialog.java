package com.huhmoon.foodgasm.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public class FGDialog {

    // showSettingsAlert(): Used to display an alert window indicating that the GPS settings have
    // been disabled and allows the user to go to Settings to enable Location services.
    public static void showSettingsAlert(final Context context) {

        // Builds the AlertDialog window.
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // SETTINGS: Sets the button listener for the "Settings" button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Sets up an intent to launch the system settings.
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // CANCEL: Sets the button listener for the "Cancel" button.
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel(); // The AlertDialog window is dismissed.
            }
        });

        alertDialog.show(); // Displays the AlertDialog window.
    }
}
