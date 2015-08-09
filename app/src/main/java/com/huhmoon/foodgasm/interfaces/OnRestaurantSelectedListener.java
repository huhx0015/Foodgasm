package com.huhmoon.foodgasm.interfaces;

import com.huhmoon.foodgasm.data.FGRestaurantModel;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public interface OnRestaurantSelectedListener {

    void displayRestaurant(FGRestaurantModel restaurant, Boolean isDisplay);
}
