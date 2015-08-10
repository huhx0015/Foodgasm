package com.huhmoon.library.sync;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.huhmoon.library.application.FGApplication;
import com.huhmoon.library.ui.FGToast;
import com.huhmoon.library.data.FGFoodModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGWearableListener extends WearableListenerService {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String LOG_TAG = FGWearableListener.class.getSimpleName();

    // SYNC VARIABLES
    private static Handler backgroundHandler;
    private static final String DATA_ITEM_RECEIVED_PATH = "/data-item-received";
    private static final String LAUNCH_NAVIGATION_PATH = "/launch-navigation-";

    /** OVERRIDDEN METHODS _____________________________________________________________________ **/

    // onDataChanged(): Called when data item objects are created, changed, or deleted. An event on
    // one side of a connection triggers this callback on both sides.
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        Log.d(LOG_TAG, "onDataChanged(): Data Event: " + dataEvents);

        // Retrieves the List of DataEvents exactly when the data change event occurs.
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        // Sets up a new Google API data connection to communicate with the wearable device.
        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        // Listens for the connection callback events on the Google API client.
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            // onConnected(): Called when a successful Google API client connection has occurred.
            @Override
            public void onConnected(Bundle bundle) {

                Log.d(LOG_TAG, "onConnected() event.");

                for (final DataEvent event : events) {
                    onDataItem(googleApiClient, event.getDataItem());
                }
            }

            // onConnectionSuspended(): Called when the Google API client connection has been
            // suspended.
            @Override
            public void onConnectionSuspended(int i) {
                Log.d(LOG_TAG, "onConnectionSuspended() event.");
            }
        });

        googleApiClient.connect(); // Connects the Google API client.
    }

    // onMessageReceived(): A message sent from one side of a connection triggers this callback on
    // the other side of the connection.
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.d(LOG_TAG, "onMessageReceived(): Message Event: " + messageEvent);

        super.onMessageReceived(messageEvent);

        // If messageEvent matches the path value of LAUNCH_NAVIGATION_PATH, an intent to Google
        // Maps' Navigation Mode will be activated, based on the byte[] data passed in with the event.
        if (messageEvent.getPath().startsWith(LAUNCH_NAVIGATION_PATH)) {

            // Converts the messageEvent data byte[] into a String.
            byte[] messageData = messageEvent.getData();
            String addressData = new String(messageData);
            String[] addressMessage = addressData.split("&mode="); // Splits the String.

            // Outputs the received food data to the log.
            Log.d(LOG_TAG, "onMessageReceived(): Received address: " + addressMessage[0]);
            Log.d(LOG_TAG, "onMessageReceived(): Received type: " + addressMessage[1]);

            // Launches Google Maps in Navigation Mode with the address and type. Checks to
            // see if Google Maps is installed first.
            if (checkForAppInstalled(FGApplication.instance)) {

                // Launches an Intent to Google Maps with the inputted address.
                FGApplication.instance.prepareNavigationIntent(addressMessage[0], addressMessage[1]);
            }
        }
    }

    // onPeerConnected(): Called when connection with the handheld or wearable is connected. Changes
    // in connection state on one side of the connection triggers these callbacks on both sides of
    // the connection.
    @Override
    public void onPeerConnected(Node peer) {
        Log.d(LOG_TAG, "onPeerConnected(): Peer: " + peer);
        super.onPeerConnected(peer);
    }

    // onPeerDisconnected(): Called when connection with the handheld or wearable is disconnected.
    // Changes in connection state on one side of the connection triggers these callbacks on both
    // sides of the connection.
    @Override
    public void onPeerDisconnected(Node peer) {
        Log.d(LOG_TAG, "onPeerDisconnected(): Peer: " + peer);
        super.onPeerDisconnected(peer);
    }

    /** SYNC METHODS ___________________________________________________________________________ **/

    // getBackgroundHandler(): Retrieves the Handler object in the running background.
    private static Handler getBackgroundHandler() {

        // If the background Handler object is not null, it is returned.
        if (null != backgroundHandler) { return backgroundHandler; }

        // Initializes and sets up a new background Handler object.
        final HandlerThread backgroundThread = new HandlerThread("FGWearableListenerThread");
        backgroundThread.start(); // Begins the thread.
        backgroundHandler = new Handler(backgroundThread.getLooper());

        return backgroundHandler;
    }

    // onDataItem(): This method is used for determining the proper course of action for each
    // received item during a synchronization event.
    public static void onDataItem(final GoogleApiClient googleApiClient, final DataItem item) {

        // Determines the current connected node.
        final PendingResult<NodeApi.GetLocalNodeResult> getLocalNodeResult =
                Wearable.NodeApi.getLocalNode(googleApiClient);

        // Sets the callback result listener.
        getLocalNodeResult.setResultCallback(new ResultCallback<NodeApi.GetLocalNodeResult>() {

            // onResult: Invoked when the result callback is successful.
            @Override
            public void onResult(NodeApi.GetLocalNodeResult getLocalNodeResult) {

                // Retrieves the result of the node.
                final Node localNode = getLocalNodeResult.getNode();

                Log.d(LOG_TAG, "onDataItem(): localNode: " + localNode
                        + "\nlocalNode.getDisplayName: " + localNode.getDisplayName()
                        + "\nlocalNode.getId: " + localNode.getId());

                final Uri uri = item.getUri();

                // Ignores events from the same node ID as the present node.
                final String nodeId = uri.getHost();
                if (nodeId.equals(localNode.getId())) {
                    Log.d(LOG_TAG, "onDataItem(): Ignoring event from self. | NodeId: " + nodeId);
                    return;
                }

                // Handling non local data events may fail if handled on the main application
                // thread, so these are handled on a background thread.
                getBackgroundHandler().post(new Runnable() {

                    @Override
                    public void run() {
                        onNonLocalDataItem(googleApiClient, item);
                    }
                });
            }
        });
    }

    // onNonLocalDataItem(): This method handles non local data item during synchronization events.
    public static void onNonLocalDataItem(final GoogleApiClient googleApiClient, final DataItem item) {

        final Uri uri = item.getUri(); // Sends the message that was received as a data item.
        final String nodeId = uri.getHost(); // Gets the node ID from the host value of the URI.

        // Set the data of the message to be the bytes of the URI.
        final byte[] payload = uri.toString().getBytes();

        // The RPC is sent.
        Wearable.MessageApi.sendMessage(googleApiClient, nodeId, DATA_ITEM_RECEIVED_PATH, payload);

        // Reads the data from the path.
        final String path = uri != null ? uri.getPath() : null;
        Log.d(LOG_TAG, "onNonLocalDataItem(): item: " + item + "| uri: " + uri + "| path: " + path);

        LinkedList<FGFoodModel> foods = FGFoodSync.receive(item); // Handles the received data.
        FGFoodImageSync.receive(googleApiClient, item); // Receives the images.
        //FGApplication.instance.recreateDatabase(foods); // Recreates the database.
    }

    // sendOutgoingData(): Sends outgoing data from the mobile/wear device.
    public static void sendOutgoingData(final Context context, final GoogleApiClient wearGoogleApiClient, LinkedList<FGFoodModel> foods) {

        Log.d(LOG_TAG, "sendOutgoingData(): wearGoogleApiClient: " + wearGoogleApiClient);

        String path = context.getFilesDir() + "/fgdata"; // Stores the path location to the data.

        // Sync Data with a Data Map:
        PutDataMapRequest wearDataMap = PutDataMapRequest.create(path); // Setting the path of the data item.

        FGFoodSync.send(wearDataMap, foods); // Processes the FGFoodModel into String objects.

        // BUGFIX: This addresses an issue where data synchronization on some data objects to
        // Android Wear devices would occasionally stall, with Wear devices assuming that no data
        // change has occurred.
        // (http://stackoverflow.com/questions/24676165/unable-to-push-data-to-android-wear-emulator)
        wearDataMap.getDataMap().putLong("data_timestamp", new Date().getTime());

        // Call PutDataMapRequest.asPutDataRequest() to obtain a PutDataRequest object.
        PutDataRequest request = wearDataMap.asPutDataRequest();

        // Call DataApi.putDataItem() to request the system to create the data item.
        // Note: If the handset and wearable devices are disconnected, the data is buffered and
        // synced when the connection is re-established.
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(wearGoogleApiClient, request);
    }

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/

    // checkForAppInstall(): Checks to see if the application is installed on the device. Displays
    // a Toast message if the application is not found.
    public static Boolean checkForAppInstalled(Context context) {

        // Sets up the PackageManager class object to check the device's installed applications.
        PackageManager pm = context.getPackageManager();
        String packName = "com.google.android.apps.maps"; // "GOOGLE MAPS" package name.
        Boolean isInstalled;

        // Checks to see if the package is installed or not.
        try {
            pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        }

        // NameNotFoundException handler.
        catch (PackageManager.NameNotFoundException e) { isInstalled = false; }

        // Displays a Toast message, notifying the user that Google Maps needs to be installed.
        if (!isInstalled) {
            FGToast.toastyPopUp("GOOGLE MAPS is not installed. Please install GOOGLE MAPS.", context);
        }

        return isInstalled;
    }
}
