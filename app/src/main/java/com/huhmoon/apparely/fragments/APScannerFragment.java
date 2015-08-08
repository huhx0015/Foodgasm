package com.huhmoon.apparely.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huhmoon.apparely.R;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 8/7/2015.
 */
public class APScannerFragment extends Fragment {

    Activity currentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        currentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Sets the view to the specified XML layout file.
        View ap_scanner_view = (ViewGroup) inflater.inflate(R.layout.ap_scanner_fragment_layout, container, false);
        ButterKnife.bind(this, ap_scanner_view); // ButterKnife view injection initialization.
        return ap_scanner_view;
    }
}
