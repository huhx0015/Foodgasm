package com.huhmoon.apparely.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huhmoon.apparely.R;
import com.huhmoon.apparely.location.FGLocation;
import com.huhmoon.apparely.location.FGLocationListener;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGRestaurantListFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FOOD VARIABLES
    private String foodName = ""; // References the name of the food item.
    private String currentAddress = ""; // References the user's current address.

    // FRAGMENT VARIABLES
    private View restaurant_list_view; // References the layout for the fragment.

    // LOGGING VARIABLES
    private static final String TAG = FGRestaurantListFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static FGRestaurantListFragment restaurant_list_fragment = new FGRestaurantListFragment();

    public FGRestaurantListFragment() {}

    // getInstance(): Returns the restaurant_list_fragment instance.
    public static FGRestaurantListFragment getInstance() { return restaurant_list_fragment; }

    // initializeFragment(): Initializes the fragment with the food properties.
    public void initializeFragment(String food) {
        this.foodName = food;
    }

    /** FRAGMENT LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.currentActivity = activity; // Sets the currentActivity to attached activity object.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Sets the view to the specified XML layout file.
        restaurant_list_view = (ViewGroup) inflater.inflate(R.layout.fg_restaurant_list_fragment, container, false);
        ButterKnife.bind(this, restaurant_list_view); // ButterKnife view injection initialization.

        setUpLayout(); // Sets up the layout for the fragment.

        return restaurant_list_view;
    }

    // onDestroyView(): This function runs when the screen is no longer visible and the view is
    // destroyed.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this); // Sets all injected views to null.
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void setUpLayout() {

        // Accesses the GPS and queries the GPS coordinates for a valid address in the
        // background, using an AsyncTask class.
        FGRetrieveLocation retrieveLocation = new FGRetrieveLocation();
        retrieveLocation.execute();
    }

    /** ASYNCTASK METHODS ______________________________________________________________________ **/

    public class FGRetrieveLocation extends AsyncTask<String, Integer, String> {

        // onPreExecute(): This method runs before the AsyncTask process begins.
        @Override
        protected void onPreExecute() {

            // Attempts to retrieve a valid address from the GPS coordinates.
            FGLocationListener gps = new FGLocationListener(currentActivity);
            currentAddress = FGLocation.retrieveCoordinates(gps, currentActivity);
        }

        // onCancelled(): This method runs if the AsyncTask is cancelled.
        @Override
        protected void onCancelled() { }

        // onPostExecute(): This method runs after the AsyncTask has finished running.
        @Override
        protected void onPostExecute(String result) {

            // Runs on the UI thread.
            currentActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO: Retrieve the address and update the list of restaurants here.
                    // getRestaurantList(foodName, currentAddress);
                }
            });
        }

        // doInBackground(): This method constantly runs in the background while AsyncTask is
        // running.
        @Override
        protected String doInBackground(String... params) {

            // Does nothing while the address object is null.
            while (currentAddress == null) { }
            return null;
        }
    }
}
