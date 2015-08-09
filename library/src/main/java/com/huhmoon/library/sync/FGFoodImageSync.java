package com.huhmoon.library.sync;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.huhmoon.library.application.FGApplication;
import com.huhmoon.library.data.FGFoodModel;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;

import it.sephiroth.android.library.picasso.Picasso;
import it.sephiroth.android.library.picasso.Target;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */
public class FGFoodImageSync {

    private static int MAX_IMAGES_TO_SEND = 5; // Maximum number of images to process.

    public static final String TAG = FGFoodImageSync.class.getSimpleName();
    
    // sendFoodImages(): Sends food images to the Wear device.
    public static void sendFoodImages(final GoogleApiClient googleApiClient, LinkedList<FGFoodModel> foods) {
        
        // Sends images up to the MAX_IMAGES_TO_SEND.
        for (int i = 0; i < MAX_IMAGES_TO_SEND; i++) {

            // Retrieves the image URL.
            final String imageUrl = foods.get(i).getFoodUrl();

            if (null != imageUrl) {

                // Creates a new Target to create a circular image and set it into the ImageView object.
                final int finalI = i;
                Target target = new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        Log.d(TAG, "Bitmap loaded from the FGFoodModel");
                        
                        // Transmit bitmap to Wearable device.
                        Asset preparedAsset = createAssetFromBitmap(bitmap);
                        sendBitmapToWear(googleApiClient, preparedAsset, "foodImage_" + finalI);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.d(TAG, "Bitmap load failed from the FGFoodModel.");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                };

                Picasso.with(FGApplication.instance).load(imageUrl).into(target);
            }
        }
    }

    public static void receive(final GoogleApiClient googleApiClient, final DataItem item) {

        if (null == item || null == item.getUri()) {
            return;
        }

        if (!item.getUri().getPath().endsWith("/foodgasm")) {
            return;
        }

        Log.d(TAG, "Receive food image detected.");

        DataMapItem dataMapItem = DataMapItem.fromDataItem(item);

        // FOOD IMAGE:
        Asset profileAsset = dataMapItem.getDataMap().getAsset("foodImage");

        try {
            Bitmap bitmap = loadBitmapFromAsset(profileAsset, googleApiClient);
            saveBitmapToFile(bitmap, FGApplication.instance, "foodImage.png"); // Attempts to save the image.
        }

        catch (IllegalArgumentException e) {
            Log.d(TAG, "WEAR: Asset was null.");
        }

        // FOOD IMAGES: Process food images from the wear.
        for (int foodImages = 0; foodImages <= MAX_IMAGES_TO_SEND; foodImages++) {

            Asset friendsAsset = dataMapItem.getDataMap().getAsset("foodImage_" + foodImages);

            try {
                Bitmap bitmap = loadBitmapFromAsset(friendsAsset, googleApiClient);
                saveBitmapToFile(bitmap, FGApplication.instance, "foodImage_" + foodImages + ".png"); // Attempts to save the image.
            }

            catch (IllegalArgumentException e) {
                Log.d(TAG, "WEAR: Asset was null.");
            }
        }
    }

    // loadBitmapFromAsset(): Loads the bitmap object from the asset object.
    public static Bitmap loadBitmapFromAsset(Asset asset, GoogleApiClient wearGoogleApiClient) {

        if (asset == null) { throw new IllegalArgumentException("Asset must be non-null"); }

        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(wearGoogleApiClient, asset).await().getInputStream();

        if (assetInputStream == null) {
            return null;
        }

        // Decodes the stream into a bitmap.
        return BitmapFactory.decodeStream(assetInputStream);
    }

    // saveBitmapToFile(): Saves the bitmap to a PNG image file.
    public static void saveBitmapToFile(Bitmap bitmap, Context context, String filename) {

        try {

            String filepath = context.getFilesDir() + "/";

            Log.d(TAG, "Saving File As: " + filename + " | Directory: " + filepath);

            FileOutputStream fOut = new FileOutputStream(filepath + filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        }

        catch (Exception e) {

            e.printStackTrace();
            Log.i(null, "ERROR: Save file error.");
            return;
        }
    }

    /** ASSETS TRANSFER FUNCTIONALITY __________________________________________________________ **/

    // createAssetFromBitmap(): Creates an Asset from a Bitmap object.
    public static Asset createAssetFromBitmap(Bitmap bitmap) {
        Log.d(TAG, "createAssetFromBitmap");

        ByteArrayOutputStream byteStream = null;

        try {
            byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            return Asset.createFromBytes(byteStream.toByteArray());
        }

        finally {
            if (null != byteStream) {

                try {
                    byteStream.close();
                }

                catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    // sendBitmapToWear(): Sends a Bitmap to the wear device.
    public static void sendBitmapToWear(GoogleApiClient client, Asset asset, String identifier) {

        Log.d(TAG, "sendBitmapToWear");

        PutDataMapRequest dataMap = PutDataMapRequest.create("/foodgasm");
        dataMap.getDataMap().putAsset(identifier, asset);

        // Fix from StackOverflow (http://stackoverflow.com/questions/24676165/unable-to-push-data-to-android-wear-emulator)
        dataMap.getDataMap().putLong("avatar_timestamp", new Date().getTime()); // MOST IMPORTANT LINE FOR TIMESTAMP

        PutDataRequest request = dataMap.asPutDataRequest();

        Wearable.DataApi.putDataItem(client, request)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        Log.d(TAG, "Sending image was successful: " + dataItemResult.getStatus()
                                .isSuccess());
                    }
                });
    }
}
