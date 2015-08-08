package com.huhmoon.apparely.location;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import com.huhmoon.apparely.ui.dialog.FGDialog;
import com.huhmoon.apparely.ui.toast.FGToast;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public class FGLocation {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String TAG = FGLocation.class.getSimpleName();

    /** LOCATION FUNCTIONALITY _________________________________________________________________ **/

    // resolveCoordinates(): A method which resolves a set of coordinates into the closest, readable
    // String street address.
    public static String resolveCoordinates(double latitude, double longitude, Context context) {

        // Initializes the Location and LocationManager objects.
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();

        if (null != locations && null != providerList && providerList.size() > 0) {

            Geocoder geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());

            try {

                // Retrieves a list of possible addresses from the coordinates.
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (null != listAddresses && listAddresses.size() > 0){

                    Address address = listAddresses.get(0); // Retrieves the first address result.
                    StringBuilder sbAddress = new StringBuilder();

                    // Reads and appends the full address, line by line.
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sbAddress.append(address.getAddressLine(i)).append("\n");
                    }

                    // Appends the locality, postal code, and country name to the StringBuilder.
                    //sbAddress.append(address.getLocality()).append("\n");
                    //sbAddress.append(address.getPostalCode()).append("\n");
                    //sbAddress.append(address.getCountryName());

                    // Assembles the fully appended StringBuilder into a String object.
                    String fullAddress = sbAddress.toString();

                    FGToast.toastyPopUp("The address at (" + latitude + ", " + longitude + ") is: " + fullAddress, context);

                    return fullAddress;
                }

            }

            // I/O exception handler.
            catch (IOException e) { e.printStackTrace(); }
        }

        // Outputs a Toast message, indicating that the coordinates could not be resolved into a
        // proper address.
        FGToast.toastyPopUp("The coordinates at (" + latitude + ", " + longitude + ") could not be resolved into an address.", context);
        return null;
    }

    // retrieveCoordinates(): A method which attempts to retrieve the current device coordinates.
    public static String retrieveCoordinates(FGLocationListener gps, Context context) {

        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Log.d(TAG, "GPS query was successful. The current coordinates are: (" + latitude + ", " + longitude + ").");
            String address = FGLocation.resolveCoordinates(latitude, longitude, context);

            // If address is null, this indicates that there was an error in resolving the
            // coordinates, and that the function should no longer continue.
            if (address == null) {
                FGToast.toastyPopUp("Current location could not be determined.", context);
                return null;
            }

            return address;
        }

        else {

            // Shows an AlertDialog, indicating that the GPS could not query the current position.
            FGDialog.showSettingsAlert(context);

            return null;
        }
    }
}
