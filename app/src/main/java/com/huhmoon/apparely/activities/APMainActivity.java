package com.huhmoon.apparely.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huhmoon.apparely.R;
import com.huhmoon.apparely.fragments.APDummyFragment;
import com.huhmoon.apparely.fragments.APScannerFragment;
import com.huhmoon.apparely.ui.layout.APUnbind;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Vector;
import butterknife.Bind;
import butterknife.ButterKnife;

public class APMainActivity extends AppCompatActivity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LAYOUT VARIABLES
    private ActionBarDrawerToggle drawerToggle; // References the toolbar drawer toggle button.
    private Boolean showFragment = false; // Used to determine if the fragment is currently being shown or not.
    private Boolean isRemovingFragment = false; // Used to determine if the fragment is currently being removed.
    private ViewPager apViewPager; // Used to reference the ViewPager object.

    // SYSTEM VARIABLES
    private static WeakReference<APMainActivity> weakRefActivity = null; // Used to maintain a weak reference to the activity.

    // VIEW INJECTION VARIABLES
    @Bind(R.id.ap_main_fragment_container) FrameLayout fragmentContainer; // Used to reference the FrameLayout object that contains the fragment.
    @Bind(R.id.ap_secondary_toolbar) LinearLayout secondaryToolbar; // Used to reference the secondary toolbar LinearLayout container.
    @Bind(R.id.ap_main_toolbar) Toolbar mainToolbar; // Used for referencing the Toolbar object.

    // SEARCH
    @Bind(R.id.ap_search_bar_container) FrameLayout searchBarContainer;

    // TABS
    @Bind(R.id.ap_search_container) LinearLayout searchTab;
    @Bind(R.id.ap_scan_container) LinearLayout scanTab;
    @Bind(R.id.ap_cart_container) LinearLayout cartTab;
    
    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // INITIALIZATION:
        super.onCreate(savedInstanceState);
        weakRefActivity = new WeakReference<APMainActivity>(this); // Creates a weak reference of this activity.
        setContentView(R.layout.ap_main_activity_layout); // Assigns the layout for the activity.
        ButterKnife.bind(this); // ButterKnife view injection initialization.

        // LAYOUT:
        setUpLayout(); // Sets up the layout, buttons and advertising banner for the activity.
        setUpSlider(false); // Initializes the fragment slides for the PagerAdapter.
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
        getMenuInflater().inflate(R.menu.ap_main_activity_menu, menu);
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

    /** PHYSICAL BUTTON FUNCTIONALITY __________________________________________________________ **/

    // BACK KEY:
    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    @Override
    public void onBackPressed() { 
        finish(); // Finishes the activity.
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setUpButtons(): Sets up the button listeners for the activity.
    private void setUpButtons() {

        // SEARCH TAB:
        searchTab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                apViewPager.setCurrentItem(0); // Loads the selected slider page.
                searchBarContainer.setVisibility(View.VISIBLE);
            }
        });

        // SCAN TAB:
        scanTab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                apViewPager.setCurrentItem(1); // Loads the selected slider page.
                searchBarContainer.setVisibility(View.GONE);
            }
        });

        // CART TAB:
        cartTab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                apViewPager.setCurrentItem(2); // Loads the selected slider page.
                searchBarContainer.setVisibility(View.GONE);
            }
        });
    }

    // setUpLayout(): This function is responsible for setting up the layout for the application.
    private void setUpLayout() {
        setUpButtons(); // Sets up the button listeners for the activity.
        setUpToolbar(); // Sets up the toolbar for the activity.
        setUpSecondaryToolbar(); // Sets up the secondary toolbar for the activity.
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

    /** SLIDER FUNCTIONALITY ___________________________________________________________________ **/

    // createSlideFragments(): Sets up the slide fragments for the PagerAdapter object.
    private List<Fragment> createSlideFragments() {

        final List<Fragment> fragments = new Vector<Fragment>(); // List of fragments in which the fragments is stored.

        // Initializes the fragment objects for the slider.
        APDummyFragment firstFragment = new APDummyFragment();
        APScannerFragment secondFragment = new APScannerFragment();
        APDummyFragment thirdFragment = new APDummyFragment();

        // Sets up the fragment list for the PagerAdapter object.
        fragments.add(firstFragment); // Adds the firstFragment fragment to the List<Fragment> object.
        fragments.add(secondFragment); // Adds the secondFragment fragment to the List<Fragment> object.
        fragments.add(thirdFragment); // Adds the thirdFragment fragment to the List<Fragment> object.

        return fragments;
    }

    // setPageListener(): Sets up the listener for the Pager Adapter object.
    private void setPageListener(ViewPager page) {

        // Defines the action to take when the page is changed.
        page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // onPageScrollStateChanged(): Called the page scroll state is changed.
            public void onPageScrollStateChanged(int state) {}

            // onPageScrolled(): Called when the pages are scrolled.
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            // onPageSelected(): Called when a new page is selected.
            public void onPageSelected(int position) {

                switch (position) {

                    // SEARCH:
                    case 0:
                        searchBarContainer.setVisibility(View.VISIBLE);
                        break;

                    // SCAN:
                    case 1:
                        searchBarContainer.setVisibility(View.GONE);
                        break;

                    // CART:
                    case 2:
                        searchBarContainer.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    // setUpSlider(): Initializes the slides for the PagerAdapter object.
    private void setUpSlider(Boolean isChanged) {

        // Resets the ViewPager object if the Page Adapter object has experienced a screen change.
        if (isChanged == true) { apViewPager.setAdapter(null); }

        // Initializes and creates a new FragmentListPagerAdapter objects using the List of slides
        // created from createSlideFragments.
        PagerAdapter dgPageAdapter = new FragmentListPagerAdapter(getSupportFragmentManager(), createSlideFragments());

        apViewPager = (ViewPager) super.findViewById(R.id.ap_main_activity_fragment_pager);
        apViewPager.setAdapter(dgPageAdapter); // Sets the PagerAdapter object for the activity.
        apViewPager.setOffscreenPageLimit(3); // Sets the off page limit for three fragments.

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
        DrawerLayout apDrawerLayout = (DrawerLayout) findViewById(R.id.ap_drawer_layout);
        ListView apDrawerList = (ListView) findViewById(R.id.ap_main_left_drawer_list);

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
        apDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.ap_drawer_list, apDrawerOptions));

        // Retrieves the DrawerLayout to set the status bar color. This only takes effect on Lollipop,
        // or when using translucentStatusBar on KitKat.
        apDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.toolbarDarkColor));
    }

    // setUpSecondaryToolbar(): Sets up the secondary toolbar for the activity.
    private void setUpSecondaryToolbar() {

        // Retrieves the height value of the primary Toolbar object.
        int toolbarHeight = getSupportActionBar().getHeight();

        // If the Toolbar height is 0, the default height for a Toolbar object is set.
        if (toolbarHeight <= 0) {

            // Converts pixels into density pixels.
            float scale = getResources().getDisplayMetrics().density;
            toolbarHeight = (int) (56 * scale + 0.5f); // Converts 56dp into pixels.
        }

        // Sets the width and height parameters for the secondary Toolbar object.
        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, toolbarHeight);
        secondaryToolbar.setLayoutParams(parameters);
    }

    /** MEMORY FUNCTIONALITY ___________________________________________________________________ **/

    // recycleMemory(): Recycles ImageView and View objects to clear up memory resources prior to
    // Activity destruction.
    private void recycleMemory() {

        // Unbinds all Drawable objects attached to the current layout.
        try { APUnbind.unbindDrawables(findViewById(R.id.ap_main_activity_layout)); }
        catch (NullPointerException e) { e.printStackTrace(); } // Prints error message.
    }
}