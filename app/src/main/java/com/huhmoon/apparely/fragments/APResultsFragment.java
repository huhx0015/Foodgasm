package com.huhmoon.apparely.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huhmoon.apparely.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/7/2015.
 */
public class APResultsFragment extends Fragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private Activity currentActivity;

    private String UPC_CODE = "";

    // LOGGING VARIABLES
    private static final String LOG_TAG = APResultsFragment.class.getSimpleName();

    // NARRATION VARIABLES
    private TextToSpeech speechInstance;

    // VIEW INJECTION VARIABLES
    @Bind(R.id.ap_results_upc_text) TextView ap_upc_text;

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    // APResultsFragment(): Default constructor for the APResultsFragment fragment class.
    private final static APResultsFragment results_fragment = new APResultsFragment();

    // APResultsFragment(): Deconstructor method for the APResultsFragment fragment class.
    public APResultsFragment() {}

    // getInstance(): Returns the results_fragment instance.
    public static APResultsFragment getInstance() { return results_fragment; }

    // initializeFragment(): Sets the initial values for the fragment.
    public void initializeFragment(String upc) {
        this.UPC_CODE = upc;
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.currentActivity = activity; // Sets the currentActivity to attached activity object.
    }

    // onCreateView(): Creates and returns the view hierarchy associated with the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View ap_results_view = (ViewGroup) inflater.inflate(R.layout.ap_results_fragment_layout, container, false);
        ButterKnife.bind(this, ap_results_view); // ButterKnife view injection initialization.

        setUpLayout(); // Sets up the layout for the fragment.

        return ap_results_view;
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
        ap_upc_text.setText(UPC_CODE); // Sets the UPC Code.
        startSpeech("Item scanned. The UPC found is " + UPC_CODE + ". Is this what you're looking for?");
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
