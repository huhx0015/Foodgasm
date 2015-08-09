package com.huhmoon.library.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGImages {

    /** CLASS FUNCTIONALITY ____________________________________________________________________ **/

    // SYSTEM VARIABLES
    private static final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    /** IMAGE FUNCTIONALITY ____________________________________________________________________ **/

    // setBitmapOptions(): setBitmapOptions is a function that sets the default Bitmap options for
    // the image objects. It sets the color profile to RGB_565, as well as allowing the image object
    // to be purged for memory optimization. This function is used in conjunction with Picasso's
    // .withOptions function.
    public static BitmapFactory.Options setBitmapOptions() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = false; // Disables the dithering option.

        // NOTE: The inPurgeable option is only applied for Android versions less than API 21.
        // LOLLIPOP has depreciated inPurgeable and inInputSharable and ignores these options.
        if (api_level < 20) {
            options.inPurgeable = true; // Allocates Bitmap's pixels so that they may be purged if system needs to reclaim memory.
            options.inInputShareable = true; // Determines whether the bitmap can share a reference to the input data or if it must make a deep copy.
        }

        return options;
    }
}
