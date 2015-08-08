package com.huhmoon.apparely.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.huhmoon.apparely.R;
import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKCode;
import com.mirasense.scanditsdk.interfaces.ScanditSDKOnScanListener;
import com.mirasense.scanditsdk.interfaces.ScanditSDKScanSession;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/7/2015.
 */
public class APScannerFragment extends Fragment implements ScanditSDKOnScanListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private Activity currentActivity;

    // The main object for recognizing a displaying barcodes.
    private ScanditSDK mBarcodePicker;

    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    private String SCANDIT_SDK_KEY;

    Toast mToast = null;

    ScanditSDKAutoAdjustingBarcodePicker picker;

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        currentActivity = activity; // References the attached activity.

        // Retrieves the Scandit SDK key from the resources.
        SCANDIT_SDK_KEY = currentActivity.getResources().getString(R.string.scandit_key);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Sets the view to the specified XML layout file.
        //View ap_scanner_view = (ViewGroup) inflater.inflate(R.layout.ap_dummy_fragment_layout, container, false);
        //ButterKnife.bind(this, ap_scanner_view); // ButterKnife view injection initialization.

        // Initialize and start the bar code recognition.
        initializeAndStartBarcodeScanning();

        return picker;
    }

    @Override
    public void onResume() {

        // Once the activity is in the foreground again, restart scanning.
        mBarcodePicker.startScanning();
        super.onResume();
    }

    @Override
    public void onPause() {

        // When the activity is in the background immediately stop the
        // scanning to save resources and free the camera.
        mBarcodePicker.stopScanning();

        super.onPause();
    }

    /** SCANDIT METHODS ________________________________________________________________________ **/

    /**
     * Initializes and starts the bar code scanning.
     */
    public void initializeAndStartBarcodeScanning() {

        // We instantiate the automatically adjusting barcode picker that will
        // choose the correct picker to instantiate. Be aware that this picker
        // should only be instantiated if the picker is shown full screen as the
        // legacy picker will rotate the orientation and not properly work in
        // non-fullscreen.
        picker = new ScanditSDKAutoAdjustingBarcodePicker(
                currentActivity, SCANDIT_SDK_KEY, ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);

        // Add both views to activity, with the scan GUI on top.
        mBarcodePicker = picker;

        // Register listener, in order to be notified about relevant events
        // (e.g. a successfully scanned bar code).
        mBarcodePicker.addOnScanListener(this);
    }

    /**
     *  Called when a barcode has been decoded successfully.
     */
    public void didScan(ScanditSDKScanSession session) {
        String message = "";
        for (ScanditSDKCode code : session.getNewlyDecodedCodes()) {
            String data = code.getData();
            // truncate code to certain length
            String cleanData = data;
            if (data.length() > 30) {
                cleanData = data.substring(0, 25) + "[...]";
            }
            if (message.length() > 0) {
                message += "\n\n\n";
            }
            message += cleanData;
            message += "\n\n(" + code.getSymbologyString() + ")";
        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(currentActivity, message, Toast.LENGTH_LONG);
        mToast.show();
    }
}
