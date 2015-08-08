package com.huhmoon.apparely.activities;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.huhmoon.apparely.R;
import com.huhmoon.apparely.apiclients.FGClient;
import com.huhmoon.apparely.data.FGFoodModel;
import com.huhmoon.apparely.data.FGFoodModel.FoodResponse;
import com.huhmoon.apparely.data.FGImageStore;
import com.huhmoon.apparely.data.FGRestaurantModel;
import com.huhmoon.apparely.fragments.FGFoodFragment;
import com.huhmoon.apparely.fragments.FGRestaurantFragment;
import com.huhmoon.apparely.fragments.FGRestaurantListFragment;
import com.huhmoon.apparely.intent.FGShareIntent;
import com.huhmoon.apparely.interfaces.OnFoodUpdateListener;
import com.huhmoon.apparely.interfaces.OnImageSaveListener;
import com.huhmoon.apparely.interfaces.OnRestaurantSelectedListener;
import com.huhmoon.apparely.preferences.FGPreferences;
import com.huhmoon.apparely.ui.layout.FGUnbind;
import com.huhmoon.apparely.ui.toast.FGToast;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FGMainActivity extends AppCompatActivity implements OnFoodUpdateListener, OnRestaurantSelectedListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SLIDER VARIABLES
    private ArrayList<FGFoodModel> models; // References the ArrayList of FGFoodModel objects.
    private int currentFoodNumber = 0; // Used to determine which food fragment is currently being displayed.
    private int numberOfFoods = 1; // Used to determine how many food fragments are to be displayed.
    private Boolean isFoodLoaded = false; // Used to determine if the food fragments have been fully loaded.
    private List<FGFoodModel> mFoodModelList;
    private List<Fragment> foodFragments = new Vector<Fragment>(); // Stores the list of food fragments.

    // LAYOUT VARIABLES
    private ActionBarDrawerToggle drawerToggle; // References the toolbar drawer toggle button.
    private Boolean showRestaurantList = false; // Used to determine if the restaurant list fragment is currently being shown or not.
    private Boolean showRestaurant = false; // Used to determine if the restaurant fragment is currently being shown or not.
    private Boolean isRemovingFragment = false; // Used to determine if the fragment is currently being removed.
    private ViewPager apViewPager; // Used to reference the ViewPager object.

    // LOGGING VARIABLES
    private static final String LOG_TAG = FGMainActivity.class.getSimpleName();

    // PREFERENCE VARIABLES
    private SharedPreferences FG_prefs; // Main SharedPreferences objects that store settings for the application.
    private static final String FG_OPTIONS = "fg_options"; // Used to reference the name of the preference XML file.
    private String currentImageFile = ""; // References the current image file name.

    // SYSTEM VARIABLES
    private static WeakReference<FGMainActivity> weakRefActivity = null; // Used to maintain a weak reference to the activity.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.fg_main_fragment_container) FrameLayout fragmentContainer; // Used to reference the FrameLayout object that contains the fragment.
    @Bind(R.id.fg_main_progress_bar) ProgressBar readyProgressBar; // References the progress bar that is shown until the food images are ready.
    @Bind(R.id.fg_main_toolbar) Toolbar mainToolbar; // Used for referencing the Toolbar object.
    
    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // INITIALIZATION:
        super.onCreate(savedInstanceState);
        weakRefActivity = new WeakReference<FGMainActivity>(this); // Creates a weak reference of this activity.
        setContentView(R.layout.fg_main_activity); // Assigns the layout for the activity.
        ButterKnife.bind(this); // ButterKnife view injection initialization.

        // LAYOUT:
        setUpLayout(); // Sets up the layout, buttons and advertising banner for the activity.
        setUpFoodCards(); // Sets up the food card fragments.
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    public void onDestroy() {
        recycleMemory(); // Recycles all View objects to free up memory resources.
        super.onDestroy();
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // onCreateOptionsMenu(): Inflates the menu when the menu key is pressed. This adds items to
    // the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflates the menu settings specified in menu.xml.
        getMenuInflater().inflate(R.menu.fg_main_activity_menu, menu);
        return true;
    }

    // onOptionsItemSelected(): Defines the action to take when the menu options are selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId(); // Retrieves the menu ID references from menu.xml.

        // SETTINGS:
        if (id == R.id.action_settings) {
            // TODO: Add functionality for SETTINGS later.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // onPostCreate(): Synchronizes the toggle state after onRestoreInstanceState() has occurred.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    // onShareAction(): Defines the action to take if the Share menu option is selected.
    public void onShareAction(MenuItem item) {

        if (isFoodLoaded) {

            saveCurrentFoodImage(); // Saves the current image to local storage.
            loadPreferences(); // Loads the preference values to retrieve the current image path.

            // Shares the data with external activities.
            FGShareIntent.shareIntent(currentImageFile, this);
        }

        // Displays a toast message.
        else {
            FGToast.toastyPopUp("There are no food images to be shared yet.", this);
        }
    }

    /** PHYSICAL BUTTON FUNCTIONALITY __________________________________________________________ **/

    // BACK KEY:
    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    @Override
    public void onBackPressed() { 

        // Removes the FGRestaurantListFragment fragment from the view.
        if (showRestaurantList) {
            removeFragment("RESTAURANT_LIST");
        }

        // Removes the FGRestaurantFragment fragment from the view.
        else if (showRestaurant) {
            removeFragment("RESTAURANT");
        }

        else {
            finish(); // Finishes the activity.
        }
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpButtons(): Sets up the button listeners for the activity.
    private void setUpButtons() {}

    // setUpLayout(): This function is responsible for setting up the layout for the application.
    private void setUpLayout() {
        setUpButtons(); // Sets up the button listeners for the activity.
        setUpToolbar(); // Sets up the toolbar for the activity.
    }

    /** DRAWER FUNCTIONALITY ___________________________________________________________________ **/

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //selectItem(position);
        }
    }

    /*
    // Swaps fragments in the main content view
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getActionBar().setTitle(mTitle);
    }
    */

    /** FRAGMENT FUNCTIONALITY _________________________________________________________________ **/

    // setUpFragment(): Sets up the fragment view and the fragment view animation.
    private void setUpFragment(Fragment fragment, final String fragType, Boolean isAnimated) {

        if ((weakRefActivity.get() != null) && (!weakRefActivity.get().isFinishing())) {

            // Initializes the manager and transaction objects for the fragments.
            android.support.v4.app.FragmentManager fragMan = weakRefActivity.get().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragTrans = fragMan.beginTransaction();
            fragTrans.replace(R.id.fg_main_fragment_container, fragment);

            // Makes the changes to the fragment manager and transaction objects.
            fragTrans.addToBackStack(null);
            fragTrans.commitAllowingStateLoss();

            // Sets up the transition animation.
            if (isAnimated) {

                int animationResource; // References the animation XML resource file.

                // Sets the animation XML resource file, based on the fragment type.
                if (fragType.equals("RESTAURANT_LIST")) { animationResource = R.anim.slide_up; } // RESTAURANT LIST
                else { animationResource = R.anim.slide_down; } // RESTAURANT

                final Animation fragmentAnimation = AnimationUtils.loadAnimation(this, animationResource);

                // Sets the AnimationListener for the animation.
                fragmentAnimation.setAnimationListener(new Animation.AnimationListener() {

                    // onAnimationStart(): Runs when the animation is started.
                    @Override
                    public void onAnimationStart(Animation animation) {
                        fragmentContainer.setVisibility(View.VISIBLE); // Displays the fragment.
                    }

                    // onAnimationEnd(): The fragment is removed after the animation ends.
                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    // onAnimationRepeat(): Runs when the animation is repeated.
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                // Runs on the UI thread.
                runOnUiThread(new Runnable() {

                    // Updates the layout view.
                    public void run() {
                        fragmentContainer.startAnimation(fragmentAnimation); // Starts the animation.
                    }
                });
            }

            // Displays the fragment view without any transition animations.
            else {

                // Runs on the UI thread.
                runOnUiThread(new Runnable() {

                    // Updates the layout view.
                    public void run() {
                        fragmentContainer.setVisibility(View.VISIBLE); // Displays the fragment.
                    }
                });
            }
        }
    }

    // removeFragment(): This method is responsible for displaying the remove fragment animation, as
    // well as removing the fragment view.
    private void removeFragment(final String fragType) {

        if ((weakRefActivity.get() != null) && (!weakRefActivity.get().isFinishing())) {

            int animationResource; // References the animation XML resource file.

            // RESTAURANT LIST:
            if (fragType.equals("RESTAURANT_LIST")) {
                animationResource = R.anim.slide_down; // Sets the animation XML resource file.
            }

            // RESTAURANT:
            else { animationResource = R.anim.slide_up; } // Sets the animation XML resource file.

            Animation fragmentAnimation = AnimationUtils.loadAnimation(this, animationResource);

            // Sets the AnimationListener for the animation.
            fragmentAnimation.setAnimationListener(new Animation.AnimationListener() {

                // onAnimationStart(): Runs when the animation is started.
                @Override
                public void onAnimationStart(Animation animation) {}

                // onAnimationEnd(): The fragment is removed after the animation ends.
                @Override
                public void onAnimationEnd(Animation animation) {

                    // Initializes the manager and transaction objects for the fragments.
                    FragmentManager fragMan = getSupportFragmentManager();
                    fragMan.popBackStack(); // Pops the fragment from the stack.
                    fragmentContainer.removeAllViews(); // Removes all views in the layout.

                    // Indicates that the FGRestaurantListFragment is no longer active.
                    if (fragType.equals("RESTAURANT_LIST")) {
                        showRestaurantList = false;
                    }

                    // Indicates that the FGRestaurantFragment is no longer active.
                    else if (fragType.equals("RESTAURANT")) {
                        showRestaurant = false;
                    }

                    fragmentContainer.setVisibility(View.INVISIBLE); // Hides the fragment.
                    isRemovingFragment = false; // Indicates that the fragment is no longer being removed.
                }

                // onAnimationRepeat(): Runs when the animation is repeated.
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            fragmentContainer.startAnimation(fragmentAnimation); // Starts the animation.
        }
    }

    /** SLIDER FUNCTIONALITY ___________________________________________________________________ **/

    // setUpFoodCards(): Sets up the food cards for the slider fragments. Queries the server to
    // retrieve the list of food.
    private void setUpFoodCards() {

        FGClient client = new FGClient();
        client.get().listFood("top", "all", "100", new Callback<FoodResponse>() {

            @Override
            public void success(FoodResponse model, Response response) {

                readyProgressBar.setVisibility(View.GONE); // Hides the progress bar.
                isFoodLoaded = true; // Indicates that the food images have been loaded.

                mFoodModelList = FGFoodModel.parseFoodModel(model);
                numberOfFoods = mFoodModelList.size();
                setUpSlider(false); // Initializes the fragment slides for the PagerAdapter.
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("FAILURE", "error", error);
                //Log.e("FAILURE", error.getStackTrace().toString());
                //Toast.makeText(this, error.getStackTrace().toString(), Toast.LENGTH_LONG).show();
            }
        });
        /*
        // Attempts to retrieve the JSON data from the server.
        client.getJsonData(new JsonHttpResponseHandler() {

            // onSuccess(): Run when JSON request was successful for JSONArray.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Log.d(TAG, "Fly SFO API Handshake Success (JSONArray Response)" + response.toString()); // Logging.

                models = SFOEventModel.fromJson(response); // Builds an ArrayList of WWEventModel objects from the JSONArray.
                numberOfCards = models.size(); // Retrieves the number of card fragments to build.

                setUpSlider(false); // Initializes the fragment slides for the PagerAdapter.
            }

            // onSuccess(): Run when JSON request was successful for JSONObject.
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "Fly SFO API Handshake Success (JSONObject Response) | Status Code: " + statusCode); // Logging.
            }

            // onFailure(): Run when JSON request was a failure.
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Fly SFO API Handshake failure! | Status Code: " + statusCode); // Logging.
            }
        });
        */
    }

    // createSlideFragments(): Sets up the slide fragments for the PagerAdapter object.
    private List<Fragment> createSlideFragments(int numberOfSlides) {

        // List of fragments in which the fragments is stored.
        foodFragments = new Vector<Fragment>();

        // Creates the card deck for the slider.
        for (int i = 0; i < numberOfSlides; i++) {

            // Initializes the food card fragment and adds it to the deck.
            FGFoodFragment cardFragment = new FGFoodFragment();
            cardFragment.initializeFragment(i, mFoodModelList.get(i));
            foodFragments.add(cardFragment);
        }

        return foodFragments;
    }

    // setPageListener(): Sets up the listener for the Pager Adapter object.
    private void setPageListener(ViewPager page) {

        // Defines the action to take when the page is changed.
        page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // onPageScrollStateChanged(): Called the page scroll state is changed.
            public void onPageScrollStateChanged(int state) { }

            // onPageScrolled(): Called when the pages are scrolled.
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            // onPageSelected(): Called when a new page is selected.
            public void onPageSelected(int position) {
                currentFoodNumber = position; // Sets the current food card ID value.
            }
        });
    }

    // setUpSlider(): Initializes the slides for the PagerAdapter object.
    private void setUpSlider(Boolean isChanged) {

        // Resets the ViewPager object if the Page Adapter object has experienced a screen change.
        if (isChanged == true) { apViewPager.setAdapter(null); }

        // Initializes and creates a new FragmentListPagerAdapter objects using the List of slides
        // created from createSlideFragments.
        PagerAdapter dgPageAdapter = new FragmentListPagerAdapter(getSupportFragmentManager(), createSlideFragments(numberOfFoods));

        apViewPager = (ViewPager) super.findViewById(R.id.ap_main_activity_fragment_pager);
        apViewPager.setAdapter(dgPageAdapter); // Sets the PagerAdapter object for the activity.

        setPageListener(apViewPager); // Sets up the listener for the pager object.
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

    /** TOOLBAR FUNCTIONALITY __________________________________________________________________ **/

    // setUpToolbar(): Sets up the Material Design style toolbar for the activity.
    private void setUpToolbar() {

        // Sets the references for the Drawer-related objects.
        String[] apDrawerOptions = getResources().getStringArray(R.array.drawer_options);
        DrawerLayout apDrawerLayout = (DrawerLayout) findViewById(R.id.fg_drawer_layout);
        ListView apDrawerList = (ListView) findViewById(R.id.fg_main_left_drawer_list);

        // Initializes the Material Design style Toolbar object for the activity.
        if (mainToolbar != null) {
            mainToolbar.setTitle(R.string.app_name);
            setSupportActionBar(mainToolbar);
        }

        // Sets the Drawer toggle button for the Toolbar object.
        drawerToggle = new ActionBarDrawerToggle(this, apDrawerLayout, mainToolbar, R.string.drawer_open, R.string.drawer_close) {

            // onDrawerClosed(): Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // Creates a call to onPrepareOptionsMenu() method.
            }

            // onDrawerOpened(): Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // Creates a call to onPrepareOptionsMenu() method.
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true); // Draws the toggle button indicator.
        apDrawerLayout.setDrawerListener(drawerToggle); // Sets the listener for the toggle button.

        // Sets the adapter for the drawer list view.
        apDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.fg_drawer_list, apDrawerOptions));

        // Retrieves the DrawerLayout to set the status bar color. This only takes effect on Lollipop,
        // or when using translucentStatusBar on KitKat.
        apDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.toolbarDarkColor));
    }

    /** PREFERENCES METHODS ____________________________________________________________________ **/

    // loadPreferences(): Loads the SharedPreference values from the stored SharedPreferences object.
    private void loadPreferences() {

        // Initializes the SharedPreferences object.
        FG_prefs = FGPreferences.initializePreferences(FG_OPTIONS, this);

        // Retrieves the current image file name.
        currentImageFile = FGPreferences.getCurrentImage(FG_prefs);
    }

    /** MEMORY FUNCTIONALITY ___________________________________________________________________ **/

    // recycleMemory(): Recycles ImageView and View objects to clear up memory resources prior to
    // Activity destruction.
    private void recycleMemory() {

        // Unbinds all Drawable objects attached to the current layout.
        try { FGUnbind.unbindDrawables(findViewById(R.id.fg_main_activity_layout)); }
        catch (NullPointerException e) { e.printStackTrace(); } // Prints error message.
    }

    /** INTERFACE METHODS ______________________________________________________________________ **/

    // Signals FGFoodFragment to save the current food image to local storage.
    public void saveCurrentFoodImage() {

        Log.d(LOG_TAG, "saveCurrentFoodImage(): Current food number is: " + currentFoodNumber);

        Fragment currentFragment = foodFragments.get(currentFoodNumber);

        try { ((OnImageSaveListener) currentFragment).saveCurrentImage(currentFoodNumber); }
        catch (ClassCastException cce) { } // Catch for class cast exception errors.
    }

    @Override
    public void displayRestaurantListFragment(String foodName, Boolean isDisplay) {

        // Displays the FGRestaurantListFragment view.
        if (isDisplay) {
            showRestaurantList = true;
            FGRestaurantListFragment restaurantListFragment = new FGRestaurantListFragment();
            restaurantListFragment.initializeFragment(foodName);
            setUpFragment(restaurantListFragment, "RESTAURANT_LIST", true);
        }

        // Removes the FGRestaurantListFragment view.
        else {
            removeFragment("RESTAURANT_LIST");
        }
    }

    @Override
    public void displayRestaurant(FGRestaurantModel restaurant, Boolean isDisplay) {

        // Displays the FGRestaurantFragment view
        if (isDisplay) {
            showRestaurantList = false;
            showRestaurant = true;
            FGRestaurantFragment restaurantFragment = new FGRestaurantFragment();
            restaurantFragment.initializeFragment(restaurant);
            setUpFragment(restaurantFragment, "RESTAURANT", true);
        }

        // Removes the FGResturantFragment view.
        else {
            removeFragment("RESTAURANT");
        }
    }
}