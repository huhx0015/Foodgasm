package com.huhmoon.foodgasm.interfaces;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public interface OnImageSaveListener {

    // Signals FGFoodFragment to save the current image to local storage.
    void saveCurrentImage(int foodId);
}
