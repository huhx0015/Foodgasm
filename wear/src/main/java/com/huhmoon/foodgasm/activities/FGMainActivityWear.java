package com.huhmoon.foodgasm.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import com.huhmoon.foodgasm.R;
import com.huhmoon.foodgasm.fragments.FGFoodImageFragmentWear;

import java.io.FileInputStream;
import java.util.List;
import java.util.Vector;

public class FGMainActivityWear extends FragmentActivity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private static int MAX_IMAGES_TO_LOAD = 5; // Maximum number of images to process.
    private Boolean imagesExist = false; // Indicates that the food images are available on wear.
    private int currentFoodNumber = 0; // Tracks the current food image position.
    private ViewPager fgViewPager; // Used to reference the ViewPager object.
    private WatchViewStub stub; // References the WatchViewStub view.

    /** ACTIVITY LIFECYCLE _____________________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_main_activity_wear);
        stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                isFoodThere(); // Attempts to load the food images.
            }
        });
    }

    /** SLIDER METHODS _________________________________________________________________________ **/

    // createSlideFragments(): Sets up the slide fragments for the PagerAdapter object.
    private List<Fragment> createSlideFragments(int numberOfSlides) {

        // List of fragments in which the fragments is stored.
        List<Fragment> foodFragments = new Vector<Fragment>();

        // Creates the card deck for the slider.
        for (int i = 0; i < numberOfSlides; i++) {

            // Initializes the food card fragment and adds it to the deck.
            FGFoodImageFragmentWear cardFragment = new FGFoodImageFragmentWear();
            cardFragment.initializeFragment(i);
            foodFragments.add(cardFragment);
        }

        return foodFragments;
    }

    // setPageListener(): Sets up the listener for the Pager Adapter object.
    private void setPageListener(ViewPager page) {

        // Defines the action to take when the page is changed.
        page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // onPageScrollStateChanged(): Called the page scroll state is changed.
            public void onPageScrollStateChanged(int state) {
            }

            // onPageScrolled(): Called when the pages are scrolled.
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // onPageSelected(): Called when a new page is selected.
            public void onPageSelected(int position) {
                currentFoodNumber = position; // Sets the current food card ID value.
            }
        });
    }

    // setUpSlider(): Initializes the slides for the PagerAdapter object.
    private void setUpSlider() {

        // Initializes and creates a new FragmentListPagerAdapter objects using the List of slides
        // created from createSlideFragments.
        PagerAdapter fgPageAdapter = new FragmentListPagerAdapter(getSupportFragmentManager(), createSlideFragments(MAX_IMAGES_TO_LOAD));

        fgViewPager = (ViewPager) super.findViewById(R.id.fg_main_activity_fragment_pager);
        fgViewPager.setAdapter(fgPageAdapter); // Sets the PagerAdapter object for the activity.

        setPageListener(fgViewPager); // Sets up the listener for the pager object.
    }

    // FragmentListPagerAdapter(): A subclass that extends upon the FragmentPagerAdapter class object,
    // granting the ability to load slides from a List of Fragments.
    class FragmentListPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments; // Used to store the List of Fragment objects.

        // FragmentListPagerAdapter(): Constructor method for the FragmentListPagerAdapter subclass.
        public FragmentListPagerAdapter(final android.support.v4.app.FragmentManager fragmentManager, final List<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
        }

        // getCount(): Returns the number of fragments in the PagerAdapter object.
        @Override
        public int getCount() {
            return fragments.size();
        }

        // getItem(): Returns the fragment position in the PagerAdapter object.
        @Override
        public Fragment getItem(final int position) {
            return fragments.get(position);
        }

        // getItemPosition(): Returns the item position in the PagerAdapter object.
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /** IMAGE METHODS __________________________________________________________________________ **/

    // isFoodThere(): Checks if any food images exist.
    private Boolean isFoodThere() {

        try {

            String filepath = getFilesDir() + "/";
            String filename = "foodImage_1.png";

            FileInputStream inputStream = new FileInputStream(filepath + filename);

            if (inputStream == null) {
                imagesExist = false;
                return false; // No food image exists.
            }

            // Creates the slider.
            else {
                imagesExist = true;
                setUpSlider(); // Sets up the slider.
                return true;
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            imagesExist = false;
            return false;
        }
    }
}
