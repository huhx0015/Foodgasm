package com.huhmoon.apparely.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import com.huhmoon.apparely.preferences.FGPreferences;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public class FGImageStore {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String LOG_TAG = FGImageStore.class.getSimpleName();

    // PREFERENCE VARIABLES
    private static final String FG_OPTIONS = "fg_options"; // Used to reference the name of the preference XML file.

    /** IMAGE FUNCTIONALITY ____________________________________________________________________ **/

    // saveImage(): Saves the current food image to the local storage in the device's default
    // Pictures folder.
    public static void saveImage(ImageView foodImageView, int imageCounter, final Context context) {

        // Retrieves the bitmap from the ImageView object.
        foodImageView.buildDrawingCache();
        Bitmap foodBitmap = foodImageView.getDrawingCache();

        // Sets up the save location and the file formatting.
        String saveLocationPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssZ", Locale.getDefault());
        String formatedDate = sdf.format(new Date());
        final String currentImageFile = "Foodgasm-" + formatedDate + "-" + imageCounter + ".jpg" ;

        Log.d(LOG_TAG, "saveImage(): Save environment has been prepared.");

        String lastSavedPath = saveLocationPath + "/" + currentImageFile;

        // Sets up the file I/O stream.
        OutputStream fileOutput = null;
        Uri outputFileUri;

        // Attempts to save the file to local storage.
        try {

            File saveImageDirectory = new File(saveLocationPath, currentImageFile); // Sets the save location path.
            outputFileUri = Uri.fromFile(saveImageDirectory);
            fileOutput = new FileOutputStream(saveImageDirectory);

            // Saves the current image file name into application preferences.
            SharedPreferences FG_prefs = FGPreferences.initializePreferences(FG_OPTIONS, context);
            FGPreferences.setCurrentImage(currentImageFile, FG_prefs);

            MediaScannerConnection.scanFile(context,
                    new String[]{saveLocationPath + "/" + currentImageFile}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {

                        // onScanCompleted(): Runs when media scan has concluded.
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                            Log.d(LOG_TAG, "saveImage(): Foodgasm image save has been successful. File has been saved as: " + currentImageFile);
                        }
                    });
        }

        // Exception handler.
        catch (Exception e){
            Log.e(LOG_TAG, "ERROR: saveImage(): Foodgasm image save has failed.");
            e.printStackTrace();
        }

        // Attempts to compress the bitmap image.
        try {
            foodBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutput);
            fileOutput.flush();
            fileOutput.close();
        }

        // Exception handler.
        catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: saveImage(): Compression has failed.");
        }
    }
}
