package com.huhmoon.apparely.macysapihandler;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huhmoon.apparely.R;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class ApiMainActivityFragment extends Fragment {

    public ApiMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SimpleService service = new SimpleService(getActivity());
        try {
            service.runApi();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.fragment_api_main, container, false);
    }
}
