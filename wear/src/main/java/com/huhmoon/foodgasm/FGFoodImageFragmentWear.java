package com.huhmoon.foodgasm;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.io.FileInputStream;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public class FGFoodImageFragmentWear extends Fragment {

    /** FRAGMENT VARIABLES _____________________________________________________________________ **/

    // FOOD VARIABLES
    private static int MAX_IMAGES_TO_LOAD = 5; // Maximum number of images to process.
    private int foodNumber = 0; // References the fragment number.

    // FRAGMENT VARIABLES
    private View food_view; // References the layout for the fragment.

    // LOGGING VARIABLES
    private static final String TAG = FGFoodImageFragmentWear.class.getSimpleName(); // Retrieves the simple name of the class.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to determine the activity class this fragment is currently attached to.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.fg_food_fragment_image) ImageView foodImageView;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    private final static FGFoodImageFragmentWear food_fragment = new FGFoodImageFragmentWear();

    public FGFoodImageFragmentWear() {}

    // getInstance(): Returns the food_fragment instance.
    public static FGFoodImageFragmentWear getInstance() { return food_fragment; }

    // initializeFragment(): Initializes the fragment with the food properties.
    public void initializeFragment(int cardID) {
        foodNumber = cardID;
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
        food_view = (ViewGroup) inflater.inflate(R.layout.fg_food_fragment_wear, container, false);
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

        loadFoodImages();
    }

    /** IMAGE METHODS __________________________________________________________________________ **/

    // loadFoodImages(): Loads the food images sent from the mobile device.
    private void loadFoodImages() {

        try {

            String filepath = currentActivity.getFilesDir() + "/";
            String filename = "foodImage_" + foodNumber +".png";

            FileInputStream inputStream = new FileInputStream(filepath + filename);
            generateFragments(inputStream); // Updates the friend indicators on the map.
        }

        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void generateFragments(FileInputStream input) {

        if (input == null) {
            return;
        }

        else {

            // Sets the bitmap resource from the loaded file.
            if (input != null) {
                try {
                    Bitmap foodBitmap = BitmapFactory.decodeStream(input); // Retrieves the bitmap image from the input.
                    foodImageView.setImageBitmap(foodBitmap); // Sets the bitmap.
                }
                catch (Exception e) {
                    Log.i(TAG, "File could not be set!");
                }
            }

        }
    }
}

