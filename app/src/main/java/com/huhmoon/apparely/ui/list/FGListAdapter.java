package com.huhmoon.apparely.ui.list;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.huhmoon.apparely.R;
import com.huhmoon.apparely.data.FGRestaurantModel;
import com.huhmoon.apparely.interfaces.OnRestaurantSelectedListener;

import java.util.List;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGListAdapter extends ArrayAdapter<FGRestaurantModel> {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SYSTEM VARIABLES
    public Activity currentActivity;
    public Context currentContext;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // FGListAdapter(): Constructor method for the FGListAdapter class.
    public FGListAdapter(Activity act, Context con, List<FGRestaurantModel> items) {
        super(con, 0, items);
        currentActivity = act;
        currentContext = con;
    }

    /** ADAPTER FUNCTIONALITY __________________________________________________________________ **/

    // getView(): Retrieves the individual inflated shortcut ListView item.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        FGListView itemView = (FGListView) convertView; // References the shortcut container object.
        if (null == itemView) { itemView = FGListView.inflate(parent); }  // Inflates the view.

        itemView.setItem(getItem(position), currentContext); // Sets the ListView position.

        // References the view objects.
        LinearLayout shortcutContainer = (LinearLayout) itemView.findViewById(R.id.fg_list_view_restaurant_container);

        final ArrayAdapter<FGRestaurantModel> adapter = this;

        // RESTAURANT Container: Launches the FGRestaurantFragment view.
        shortcutContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Retrieves the restaurant at the position.
                FGRestaurantModel restaurant = (FGRestaurantModel) adapter.getItem(position);

                // Signals attached activity to display the FGRestaurantFragment.
                displayRestaurantFragment(restaurant);
            }
        });

        return itemView;
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    private void displayRestaurantFragment(FGRestaurantModel restaurant) {
        try { ((OnRestaurantSelectedListener) currentActivity).displayRestaurant(restaurant, true); }
        catch (ClassCastException cce) {} // Catch for class cast exception errors.
    }

}