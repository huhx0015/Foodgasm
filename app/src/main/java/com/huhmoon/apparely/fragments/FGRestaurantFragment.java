package com.huhmoon.apparely.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.huhmoon.apparely.R;
import com.huhmoon.apparely.data.FGRestaurantModel;
import com.huhmoon.apparely.intent.FGNavIntent;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public class FGRestaurantFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // RESTAURANT VARIABLES
    private FGRestaurantModel currentRestaurant;

    // FRAGMENT VARIABLES
    private View restaurant_view; // References the layout for the fragment.

    // LOGGING VARIABLES
    private static final String TAG = FGRestaurantFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.fg_restaurant_navigation_button) Button navButton;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static FGRestaurantFragment restaurant_fragment = new FGRestaurantFragment();

    public FGRestaurantFragment() {}

    // getInstance(): Returns the restaurant_list_fragment instance.
    public static FGRestaurantFragment getInstance() { return restaurant_fragment; }

    // initializeFragment(): Initializes the fragment with the food properties.
    public void initializeFragment(FGRestaurantModel restaurant) {
        this.currentRestaurant = restaurant;
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
        restaurant_view = (ViewGroup) inflater.inflate(R.layout.fg_restaurant_fragment, container, false);
        ButterKnife.bind(this, restaurant_view); // ButterKnife view injection initialization.

        setUpLayout(); // Sets up the layout for the fragment.

        return restaurant_view;
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
        setUpButtons();
    }

    private void setUpButtons() {

        // NAVIGATION BUTTON:
        navButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO: Launches Google Maps in Navigation Mode.
                //FGNavIntent.launchNavigation(currentRestaurant.getAddress());
            }
        });
    }
}
