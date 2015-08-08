package com.huhmoon.apparely.data;

import android.content.Context;
import java.util.LinkedList;

/**
 * Created by Michael Yoon Huh on 8/8/2015.
 */

public class FGDataAccess {

    /** DATA ACCESS FUNCTIONALITY ______________________________________________________________ **/

    // getAllFood(): Retrieves all favorite foods from the database.
    public static LinkedList<FGFoodModel> getAllFood(Context con) {

        // Initializes and sets up the database.
        FGDatasource datasource = new FGDatasource(con);
        datasource.open(); // The database is opened.

        // Creates the LinkedList of shortcuts from the database.
        LinkedList<FGFoodModel> foods = datasource.getAllFoods();
        datasource.close(); // The database is closed.

        return foods;
    }
}
