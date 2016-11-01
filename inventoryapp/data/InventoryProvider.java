package com.example.tiger.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.tiger.inventoryapp.data.InventortContract.InvEntry;

/**
 * Created by tiger on 2016-10-31.
 * inventoryProvider for help save and load database
 */

public class InventoryProvider extends ContentProvider{
    private static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    //URL matcher codes for the inv Table
    private static final int INVS = 100;
    private static final int INV_ID = 101;

    //set UriMatch
    private static final UriMatcher sUriMatch = new UriMatcher(UriMatcher.NO_MATCH);

    //set static Uris

    static {
        sUriMatch.addURI(InvEntry.CONTENT_AUTHORITY, InvEntry.PATH_INV, INVS);
        sUriMatch.addURI(InvEntry.CONTENT_AUTHORITY, InvEntry.PATH_INV + "/#", INV_ID);
    }

    //set InventoryDbHelper
    private InventoryDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        //initialize mDbHelper
        mDbHelper = new InventoryDbHelper(getContext());

        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArg, String sortOrder) {
        //loading data from databse
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;
        int match = sUriMatch.match(uri);

        switch (match){
            case INVS:
                cursor = db.query(InvEntry.TABLE_NAME, projection, selection, selectionArg, null, null, sortOrder);
                break;
            case INV_ID:
                selection = InvEntry._ID + "=?";
                selectionArg = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(InvEntry.TABLE_NAME, projection, selection, selectionArg, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can not query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatch.match(uri);
        switch (match){
            case INVS:
                return insertProduction(uri, contentValues);
                default:
                    throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduction(Uri uri, ContentValues values) {
        String name = values.getAsString(InvEntry.PRODUCT_NAME);
        Integer quantity = values.getAsInteger(InvEntry.PRODUCT_QUANTITY);
        Integer price = values.getAsInteger(InvEntry.PRODUCT_PRICE);
        Integer category = values.getAsInteger(InvEntry.PRODUCT_CATEGORY);
        Integer rate = values.getAsInteger(InvEntry.PRODUCT_RATE);

        //check valid values.

        if (name == null){
            throw new IllegalArgumentException("Product requires a name");
        }else if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Product quantity must be positive.");
        }else if (price == null || price < 0) {
            throw new IllegalArgumentException("Product requires a valid price");
        }else if (category == null || !InvEntry.isValidCategory(category)){
            throw new IllegalArgumentException("Product requires a valid category");
        }
        if (rate == null || rate < 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(InvEntry.TABLE_NAME, null, values);
        if(id == -1){
            Log.e(LOG_TAG, "Failed insert row for " +uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
