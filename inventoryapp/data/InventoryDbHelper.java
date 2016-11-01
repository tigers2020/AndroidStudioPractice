package com.example.tiger.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.tiger.inventoryapp.data.InventortContract.InvEntry;

/**
 * Created by tiger on 2016-10-31.
 * Database helper for inventory app Manages database creation and version management.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    //Name of the database file
    public static final String DATABASE_NAME = "inventory.db";
    //Database version
    public static final int DATABASE_VERSION = 1;
    private static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();


    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a SQL Statement for inventory table.
        String SQL_CREATE_INV_TABLE = "CREATE TABLE " + InvEntry.TABLE_NAME + "("
                + InvEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InvEntry.PRODUCT_NAME + " TEXT NOT NULL, "
                + InvEntry.PRODUCT_QUANTITY + " INTEGER DEFAULT 0, "
                + InvEntry.PRODUCT_PRICE + " INTEGER, "
                + InvEntry.PRODUCT_CATEGORY + " INTEGER DEFAULT 0, "
                + InvEntry.PRODUCT_RATE + " INTEGER DEFAULT 0, "
                + InvEntry.PRODUCT_DESCRIBES + " TEXT);";
        Log.e(LOG_TAG, SQL_CREATE_INV_TABLE);
        //execute the SQL statement
        db.execSQL(SQL_CREATE_INV_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
