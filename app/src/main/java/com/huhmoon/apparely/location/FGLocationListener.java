package com.huhmoon.apparely.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGLocationListener extends Service implements LocationListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LOCATION VARIABLES
    private boolean isGPSEnabled = false; // Used to determine if the GPS sensor is enabled or not.
    private boolean isNetworkEnabled = false; // Used to determine if the network connection is enabled or not.
    private boolean canGetLocation = false; // Used to determine if Location settings are accessible.
    private double latitude; // References the current location's latitude value.
    private double longitude; // References the current location's longitude value.
    private Location location;
    protected LocationManager locationManager;

    // LOGGING VARIABLES
    private static final String LOG_TAG = FGLocationListener.class.getSimpleName();

    // SYSTEM VARIABLES
    private final Context context;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // FGLocationListener(): Constructor method for the FGLocationListener class.
    public FGLocationListener(Context context) {
        this.context = context;
        getLocation();
    }

    /** OVERRIDE FUNCTIONALITY _________________________________________________________________ **/

    // onBind(): This method runs during bind events.
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind(): Bind event.");
        return null;
    }

    // onLocationChanged(): This method runs when the Location parameters change.
    @Override
    public void onLocationChanged(Location arg0) {
        Log.d(LOG_TAG, "onLocationChanged(): Current location has changed.");
    }

    // onProviderDisabled(): This method runs when the provider has been disabled.
    @Override
    public void onProviderDisabled(String arg0) {
        Log.d(LOG_TAG, "onProviderDisabled(): Provider has been disabled.");
    }

    // onProviderEnabled(): This method runs when the provider has been enabled.
    @Override
    public void onProviderEnabled(String arg0) {
        Log.d(LOG_TAG, "onProviderEnabled(): Provider has been enabled.");
    }

    // onStatusChanged(): This method runs during status change events.
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        Log.d(LOG_TAG, "onStatusChanged(): Location status has changed.");
    }

    /** LOCATION FUNCTIONALITY _________________________________________________________________ **/

    // canGetLocation(): Used to determine if the current location can be detected or not.
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    // getLocation(): This method retrieves the current location coordinates by tapping into the
    // GPS sensor of the device.
    public Location getLocation() {

        // Attempts to retrieve the current location coordinates.
        try {

            // Initializes the LocationManager object and checks to see if the GPS and network
            // settings have been enabled.
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // Checks to see if GPS and network settings have been enabled.
            if(!isGPSEnabled && !isNetworkEnabled) {
                Log.d(LOG_TAG, "getLocation(): ERROR: GPS settings and/or network settings have been disabled.");
            }

            // Attempts to query the GPS sensor to determine the coordinates of the current
            // location.
            else {

                this.canGetLocation = true;

                if (isNetworkEnabled) {

                    // Requests a location update from the network.
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {

                        // Retrieves the last known reported location.
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        // Retrieves the latitude and longitude of the current reported location.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {

                    if (location == null) {

                        // Requests a location update from the network.
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {

                            // Retrieves the last known reported location.
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            // Retrieves the latitude and longitude of the current reported location.
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        }

        // Error exception handler.
        catch (Exception e) { e.printStackTrace(); }

        return location;
    }

    // getLatitude(): Returns the latitude of the current reported location.
    public double getLatitude() {

        // Retrieves the latitude from the current location.
        if (location != null) { latitude = location.getLatitude();  }
        return latitude;
    }

    // getLongitude(): Returns the longitude from the current location.
    public double getLongitude() {

        // Retrieves the longitude from the current location.
        if (location != null) { longitude = location.getLongitude(); }
        return longitude;
    }

    // stopUsingGPS(): This method signals the device that the GPS sensor is no longer needed.
    public void stopUsingGPS() {
        if (locationManager != null) { locationManager.removeUpdates(FGLocationListener.this); }
    }
}