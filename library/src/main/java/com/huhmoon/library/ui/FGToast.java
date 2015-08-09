package com.huhmoon.library.ui;

/**
 * Created by Michael Yoon Huh on 8/7/2015.
 */

import android.content.Context;
import android.widget.Toast;

public class FGToast {

    /** TOAST FUNCTIONALITY ____________________________________________________________________ **/

    // toastyPopUp(): Creates and displays a Toast popup.
    public static void toastyPopUp(String message, Context con) {
        Toast.makeText(con, message, Toast.LENGTH_SHORT).show();
    }
}
