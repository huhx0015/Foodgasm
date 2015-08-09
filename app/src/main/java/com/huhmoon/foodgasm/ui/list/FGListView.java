package com.huhmoon.foodgasm.ui.list;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huhmoon.foodgasm.R;
import com.huhmoon.foodgasm.data.FGRestaurantModel;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGListView extends RelativeLayout {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LAYOUT VARIABLES
    private ImageView restaurantImage; // ImageView object that displays the restaurant image.
    private TextView restaurantName; // TextView object that displays the restaurant name.
    private TextView restaurantAddress; // TextView object that displays the restaurant address.

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // FGListView(): Constructor method for the FGListView class.
    public FGListView(Context context) {
        this(context, null);
    }

    // FGListView(): Constructor method which sets the AttributeSet properties.
    public FGListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // FGListView(): Constructor method which inflates the ListView children.
    public FGListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.fg_list_view_children, this, true);
        setupChildren();
    }

    /** LISTVIEW FUNCTIONALITY _________________________________________________________________ **/

    // inflate(): This method inflates the FGListView object.
    public static FGListView inflate(ViewGroup parent) {

        // Inflates the ListView object from the XML layout file.
        FGListView itemView = (FGListView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fg_list_view, parent, false);
        return itemView;
    }

    // setItem(): Sets the shortcut details for the individual shortcuts in the FGListView object.
    public void setItem(FGRestaurantModel restaurant, Context con) {

        // Sets the shortcut name and address for the shortcut row item.
        String imageRes = restaurant.getImage();
        restaurantName.setText(restaurant.getName());
        restaurantAddress.setText(restaurant.getAddress());

        // Sets up the rounded shape attributes for the ImageView object.
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.LTGRAY)
                .borderWidthDp(1)
                .cornerRadiusDp(90)
                .oval(false)
                .build();


        // Loads the type image into the ImageView object.
        Picasso.with(con)
                .load(imageRes)
                .transform(transformation)
                .into(restaurantImage);

    }

    // setupChildren(): This method references the layout objects in the children view for the
    // FGListView object.
    private void setupChildren() {
        restaurantName = (TextView) findViewById(R.id.fg_restaurant_name);
        restaurantAddress = (TextView) findViewById(R.id.fg_address_text);
        restaurantImage = (ImageView) findViewById(R.id.fg_restaurant_image);
    }
}