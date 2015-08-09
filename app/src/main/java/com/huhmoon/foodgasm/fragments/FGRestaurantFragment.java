package com.huhmoon.foodgasm.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huhmoon.foodgasm.R;
import com.huhmoon.foodgasm.data.FGRestaurantModel;
import com.huhmoon.foodgasm.intent.FGNavIntent;
import com.huhmoon.library.graphics.FGImages;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;

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

    // TTS VARIABLES
    private TextToSpeech speechInstance;

    // VIEW INJECTION VARIABLES
    @Bind(R.id.fg_restaurant_navigation_button) Button navButton;
    @Bind(R.id.fg_restaurant_image) ImageView restaurantImage;
    @Bind(R.id.fg_restaurant_address) TextView restaurantAddress;
    @Bind(R.id.fg_restaurant_time) TextView restaurantTime;
    @Bind(R.id.fg_restaurant_name) TextView restaurantName;

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
        setUpText();
        setUpImage();
    }

    private void setUpButtons() {

        // NAVIGATION BUTTON:
        navButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO: Launches Google Maps in Navigation Mode.
                startSpeech("Delicious! Taking to your food at " + currentRestaurant.getName() + " now!");
                FGNavIntent.launchNavigation(currentRestaurant.getAddress(), currentActivity);
                //FGNavIntent.launchNavigation("Pizza Hut", currentActivity);
            }
        });
    }

    private void setUpText() {

        restaurantName.setText(currentRestaurant.getName());
        restaurantAddress.setText(currentRestaurant.getAddress());
    }

    private void setUpImage() {

        // Sets the event image for the ImageView object.
        Picasso.with(currentActivity)
                .load(currentRestaurant.getImage())
                .withOptions(FGImages.setBitmapOptions())
                .fit()
                .centerCrop()
                .into(restaurantImage);
    }

    /** NARRATION FUNCTIONALITY ________________________________________________________________ **/

    // startSpeech(): Activates voice functionality to say something.
    private void startSpeech(final String script) {

        // Creates a new instance to begin TextToSpeech functionality.
        speechInstance = new TextToSpeech(currentActivity, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                speechInstance.speak(script, TextToSpeech.QUEUE_FLUSH, null); // The script is spoken by the TTS system.
            }
        });
    }
}
