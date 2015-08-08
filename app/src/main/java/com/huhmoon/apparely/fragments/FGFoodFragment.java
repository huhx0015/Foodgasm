package com.huhmoon.apparely.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huhmoon.apparely.R;
import com.huhmoon.apparely.data.FGFoodModel;
import com.huhmoon.apparely.ui.graphics.FGImages;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGFoodFragment extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FOOD VARIABLES
    private int foodNumber = 0; // References the fragment number.
    private FGFoodModel foodModel; // References the food model object for this fragment.

    // FRAGMENT VARIABLES
    private View food_view; // References the layout for the fragment.

    // LAYOUT VARIABLES
    private ImageView card_background_image, main_card_image; // References the ImageView objects in the fragment.
    private int main_card_image_resource; // Used to reference the main card image.

    // LOGGING VARIABLES
    private static final String TAG = FGFoodFragment.class.getSimpleName(); // Retrieves the simple name of the class.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.fg_food_fragment_background_image) ImageView foodImage;
    @Bind(R.id.fg_food_name_text) TextView foodNameText;
    @Bind(R.id.fg_food_order_button) Button foodOrderButton;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static FGFoodFragment food_fragment = new FGFoodFragment();

    public FGFoodFragment() {}

    // getInstance(): Returns the food_fragment instance.
    public static FGFoodFragment getInstance() { return food_fragment; }

    // initializeFragment(): Initializes the fragment with the food properties.
    public void initializeFragment(int cardID, FGFoodModel food) {
        foodNumber = cardID;
        foodModel = food;
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
        food_view = (ViewGroup) inflater.inflate(R.layout.fg_food_fragment_layout, container, false);
        ButterKnife.bind(this, food_view); // ButterKnife view injection initialization.

        setUpLayout(); // Sets up the layout for the fragment.

        return food_view;
    }

    // onDestroyView(): This function runs when the screen is no longer visible and the view is
    // destroyed.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this); // Sets all injected views to null.
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpLayout(): Sets up the layout for the fragment.
    private void setUpLayout() {

        setUpFoodDetails(); // Sets up the card fragment layout objects.
        setUpButtons(); // Sets up the buttons for the fragment.
    }

    // setUpButtons(): Sets up the buttons for the fragment.
    private void setUpButtons() {

        // FOOD ORDER BUTTON:
        foodOrderButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: Define action for ordering food here.
            }
        });
    }

    // setUpFoodDetails(): Sets up the food image and name for the fragment.
    private void setUpFoodDetails() {

        /*
        // Retrieves the food details from the foodModel.
        //String food_name = foodModel.getFoodName(); // Gets the food name from the JSON string.
        //String food_image_url = foodModel.getFoodUrl(); // Gets the image URL from the JSON string.

        Log.d(TAG, "Food: " + foodNumber + " | Image URL: " + food_image_url); // Logging.

        // Sets the event image for the ImageView object.
        Picasso.with(currentActivity)
                .load(food_image_url)
                .withOptions(FGImages.setBitmapOptions())
                .fit()
                .centerCrop()
                .into(card_background_image);

        // Sets the food text for the TextView objects.
        foodNameText.setText(food_name);
        */
    }
}