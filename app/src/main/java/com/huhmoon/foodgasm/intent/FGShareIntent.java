package com.huhmoon.foodgasm.intent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class FGShareIntent {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String LOG_TAG = FGShareIntent.class.getSimpleName();

    /** INTENT FUNCTIONALITY ___________________________________________________________________ **/

    // shareIntent(): Prepares an Intent to share image data with external activities.
    public static void shareIntent(String fileName, Context context) {

        // References the directory path where the image is stored.
        final String uploadFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/";
        String fullFilePath = uploadFilePath + "" + fileName; // Sets the full file path.
        Bitmap foodBitmap; // References the food bitmap.

        // Retrieves the bitmap data from the file
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            foodBitmap = BitmapFactory.decodeFile(fullFilePath, options);
        }

        // Exception handler.
        catch (Exception e) {
            Log.e(LOG_TAG, "shareIntent(): ERROR: File could not be found.");
            return;
        }

        // Checks to see if the foodBitmap is null first.
        if (foodBitmap != null) {

            // Prepares the food bitmap to be shared via an Intent.
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            foodBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    foodBitmap, "Foodgasm", null);
            Uri imageUri = Uri.parse(path);

            // Sets up an Intent to share the shortcut data with external activities.
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("image/jpg"); // Specifies that this is a image type.
            sendIntent.putExtra(Intent.EXTRA_TEXT, "FOODGASM: This looks delicious!");
            sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(sendIntent, "Share my Food with:"));
        }
    }
}