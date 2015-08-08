package com.huhmoon.apparely.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.LinkedList;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGDatasource {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // DATABASE VARIABLES
    private FGDatabase dbHelper;
    private SQLiteDatabase database;

    // References all data columns in the MySQL database table.
    private String[] allColumns = {
            FGDatabase.COLUMN_ID,
            FGDatabase.COLUMN_FOOD,
            FGDatabase.COLUMN_IMAGE_ID
    };

    // LOGGING VARIABLES
    private static final String LOG_TAG = FGDatasource.class.getName();

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // FGDatasource(): Initialization method for the FGDatasource class.
    public FGDatasource(Context context) {
        dbHelper = new FGDatabase(context);
    }

    /** ACCESSOR FUNCTIONALITY _________________________________________________________________ **/

    // open(): Opens the database table with read/write permissions.
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // close(): Closes the database table.
    public void close() {
        dbHelper.close();
    }

    /** DATABASE FUNCTIONALITY _________________________________________________________________ **/

    // createFoodDatabase(): This method adds foods from an existing LinkedList<FGFoodModel>
    // object.
    public void createFoodDatabase(LinkedList<FGFoodModel> foods) {

        int numberOfFoods = foods.size(); // Size of the LinkedList<FGFoodModel> object.

        // Add foods into the table.
        for (int i = 0; i < numberOfFoods; i++) {

            FGFoodModel currentFood = foods.get(i); // Retrieves the food.

            /*
            // Creates the food and adds it to the database table.
            createFood(currentFood.getFoodName(),
                    currentFood.getFoodImageId());
                    */

            Log.d(LOG_TAG, "Creating shortcut database... | Current Shortcut: " + i);
        }
    }

    // createFood(): Creates a new favorite food in the database table.
    public FGFoodModel createFood(String foodName, String imageRes) {

        ContentValues values = new ContentValues(); // Retrieves the content values.
        values.put(FGDatabase.COLUMN_FOOD, foodName); // Adds the food name.
        values.put(FGDatabase.COLUMN_IMAGE_ID, imageRes); // Adds the food image resource.

        // Retrieves the row ID in which to insert the new food data.
        long insertId = database.insert(FGDatabase.TABLE_SHORTCUTS, null, values);

        // Sets up the SQL cursor for accessing the SQL database table and adds the shortcut.
        Cursor cursor = database.query(
                FGDatabase.TABLE_SHORTCUTS, allColumns,
                FGDatabase.COLUMN_ID + " = " + insertId, null,
                null, null, null, null);

        Log.d(LOG_TAG, "Food created with id: " + insertId);

        cursor.moveToFirst(); // Moves the cursor into position.

        // Retrieves the favorite food at the cursor position.
        FGFoodModel newFood = cursorToFood(cursor);
        cursor.close();

        return newFood;
    }

    // deleteFood(): Removes the favorite food from the SQL database table.
    public void deleteFood(FGFoodModel food) {

        /*
        long id = food.getId(); // Retrieves the ID from the FGFoodModel object.


        // Deletes the specified shortcut.
        database.delete(FGDatabase.TABLE_SHORTCUTS, FGDatabase.COLUMN_ID  + " = " + id, null);

        Log.d(LOG_TAG, "Favorite Food deleted at position: " + id);
                */
    }

    // editFood(): Edits the existing favorite food data.
    public void editFood(String foodName, long id, String imageRes) {

        // Prepares the updated shortcut data to be stored into the SQL database table.
        ContentValues newShortcutContents = new ContentValues();
        newShortcutContents.put(FGDatabase.COLUMN_FOOD, foodName);
        newShortcutContents.put(FGDatabase.COLUMN_ID, id);
        newShortcutContents.put(FGDatabase.COLUMN_IMAGE_ID, imageRes);

        // Updates the shortcut entry data with the new attributes.
        database.update(FGDatabase.TABLE_SHORTCUTS, newShortcutContents, "_id " + "=" + id, null);
    }

    // getAllFoods(): Retrieves all available favorite foods from the SQL database table.
    public LinkedList<FGFoodModel> getAllFoods() {

        // The LinkedList in which the retrieved FGFoodModel objects will be stored.
        LinkedList<FGFoodModel> foods = new LinkedList<FGFoodModel>();

        // Sets up the cursor for accessing the SQL database table.
        Cursor cursor = database.query(FGDatabase.TABLE_SHORTCUTS,
                allColumns, null, null, null, null, null, null);

        cursor.moveToFirst(); // Moves the cursor into position.

        // Adds all food until the cursor has reached the last position.
        while (!cursor.isAfterLast()) {

            // Retrieves the food at the cursor position.
            FGFoodModel food = cursorToFood(cursor);
            foods.add(food); // Adds the retrieved food to the LinkedList.
            cursor.moveToNext(); // Moves the food to the next food row.
        }

        cursor.close(); // Closes the cursor.

        return foods;
    }

    // cursorToFood(): This function is used to create a FGFoodModel object from the food
    // data read from the SQL database table.
    private FGFoodModel cursorToFood(Cursor cursor) {

        FGFoodModel food = new FGFoodModel("some", "some"); // Initializes the new FGFoodModel object.

        /*
        // Stores the food data read from the cursor into the new FGFoodModel object.
        food.setId(cursor.getLong(0)); // Sets the id.
        food.setFoodName(cursor.getString(1)); // Sets the food name.
        food.setImageId(cursor.getLong(2)); // Sets the image id.
        */

        return food;
    }
}