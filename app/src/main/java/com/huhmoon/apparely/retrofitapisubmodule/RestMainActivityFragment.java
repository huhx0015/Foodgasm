package com.huhmoon.apparely.retrofitapisubmodule;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huhmoon.apparely.R;
import com.huhmoon.apparely.retrofitapisubmodule.apiclients.ApiClient;
import com.huhmoon.apparely.retrofitapisubmodule.models.FoodModel;
import com.huhmoon.apparely.retrofitapisubmodule.models.FoodResponse;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class RestMainActivityFragment extends Fragment {

    private ImageView mFoodThumbNail;
    private TextView mFoodTitle;
    private int mImageIndex;
    private List<FoodModel> mFoodModelList;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    public RestMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AnimationDrawable animation = new AnimationDrawable();
        View rootView = inflater.inflate(R.layout.fragment_rest_main, container, false);
        mFoodThumbNail = (ImageView) rootView.findViewById(R.id.foodImageView);
        mFoodTitle = (TextView) rootView.findViewById(R.id.foodTitle);
        mFoodTitle.setText("Food gasm!");
        ApiClient.get().listFood("top", "all", "100", new Callback<FoodResponse>() {
            @Override
            public void success(FoodResponse model, Response response) {
                mFoodModelList = parseFoodModel(model);
                mFoodTitle.setText("Food gasm");
                Toast.makeText(getActivity(), "done", Toast.LENGTH_LONG).show();
                Log.e("URL", mFoodModelList.get(0).preview);
                Picasso.with(getActivity())
                        .load(mFoodModelList.get(0).preview)
                        .into(mFoodThumbNail);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("FAILURE", "error", error);
                //Log.e("FAILURE", error.getStackTrace().toString());
                Toast.makeText(getActivity(), error.getStackTrace().toString(), Toast.LENGTH_LONG).show();
            }
        });


        
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                    break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                    float deltaX = x2 - x1;
                    if (Math.abs(deltaX) > MIN_DISTANCE)
                    {
                        mFoodTitle.setText(mFoodModelList.get(mImageIndex).title);
                        Picasso.with(getActivity())
                                .load(mFoodModelList.get(mImageIndex).preview)
                                .into(mFoodThumbNail);
                        mImageIndex += 1;
                        Toast.makeText(getActivity(), "left2right swipe", Toast.LENGTH_SHORT).show ();
                    }
                    else
                    {
                        // consider as something else - a screen tap for example
                    }
                    break;
                }
                return true;
            }
        });

        return rootView;
    }

    public List<FoodModel> parseFoodModel(FoodResponse model){
        List<FoodModel> foodModelList = new ArrayList<FoodModel>();
        String title;
        String url;
        List<FoodResponse.ResponseChildData> children = model.getResponseData().getResponseChildData();
        for(FoodResponse.ResponseChildData child: children){
            title = child.getChildData().getTitle();
            List<FoodResponse.Image> images = child.getChildData().getPreview().getImages();
            FoodResponse.Image image = images.get(0);
            url = image.getResolutions().get(1).getUrl().replaceAll("amp;", "");

            foodModelList.add(new FoodModel(title, url));
        }
        return foodModelList;
    };


}
