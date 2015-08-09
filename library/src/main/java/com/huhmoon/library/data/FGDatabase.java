package com.huhmoon.library.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGDatabase extends SQLiteOpenHelper {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LOGGING VARIABLES
    private static final String LOG_TAG = FGDatabase.class.getName();

    // TABLE VARIABLES
    private static final int DATABASE_VERSION = 1; // DATABASE VERSION
    private static final String DATABASE_NAME = "fg_favorite_foods.db"; // DATABASE FILE NAME
    public static final String TABLE_SHORTCUTS = "fg_favorite_foods"; // TABLE NAME
    public static final String COLUMN_ID = "_id"; // FOOD ID
    public static final String COLUMN_FOOD = "food"; // FOOD NAME
    public static final String COLUMN_IMAGE_ID = "image_id"; // FOOD IMAGE

    // SQL VARIABLES: SQL database creation statement.
    private static final String DATABASE_CREATE = "create table "+ TABLE_SHORTCUTS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_FOOD + " text not null,"
            + COLUMN_IMAGE_ID + " text not null);";

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // FGDatabase(): Initializes the FGDatabase class.
    public FGDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** SQL LITE OPEN HELPER FUNCTIONALITY _____________________________________________________ **/

    // onCreate(): This method is responsible for creating the database.
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE); // SQL: Creates a new database table.
    }

    // onUpgrade(): This method is responsible for upgrading the database version.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", "
                + "which will destroy all previous data.");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHORTCUTS); // SQL: Drops existing database table.
        onCreate(db); // Creates a new database table.
    }
}