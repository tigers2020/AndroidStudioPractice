package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.android.pets.data.PetContract.*;


/**
 * Created by tiger on 2016-10-30.
 */

public class PetProvider extends ContentProvider {
    private static final String LOG_TAG = PetProvider.class.getSimpleName();

    //URI matcher codes for the pet table
    private static final int PETS = 100;
    private static final int PET_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        sUriMatcher.addURI(PetsEntry.CONTENT_AUTHORITY, PetsEntry.PATH_PETS, PETS);
        sUriMatcher.addURI(PetsEntry.CONTENT_AUTHORITY, PetsEntry.PATH_PETS + "/#", PET_ID);
    }
    //Database helper objects;
     private PetDbHelper mDbHelper;


    @Override
    public boolean onCreate()
    {
        mDbHelper = new PetDbHelper(getContext());
    return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.e(LOG_TAG, "1. uri = " + uri);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){
            case PETS:
                cursor = db.query(PetsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PET_ID:
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can not query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetsEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + "with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return insertPet(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues values) {

        String name = values.getAsString(PetsEntry.COLUMN_NAME);
        Integer gender = values.getAsInteger(PetsEntry.COLUMN_GENDER);
        Integer weight = values.getAsInteger(PetsEntry.COLUMN_WEIGHT);
        if(name == null){
            throw new IllegalArgumentException("Pet requires a name");
        }
        if(gender == null || !PetsEntry.isValiGender(gender)){
            throw new IllegalArgumentException("Pet requires valid gender");
        }
        if(weight != null && weight < 0){
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(PetsEntry.TABLE_NAME, null, values);
        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowDeleted;
        switch (match) {
            case PETS:
                rowDeleted = db.delete(PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PET_ID:
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = db.delete(PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delection is not supported for " + uri);
        }
                if(rowDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
        return rowDeleted;


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case PETS:
                selection = PetsEntry._ID;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);
            case PET_ID:
            {
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                return updatePet(uri, values, selection, selectionArgs);

            }
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(PetsEntry.COLUMN_NAME)){
            String name = values.getAsString(PetsEntry.COLUMN_NAME);
            if(name == null){
                throw new IllegalArgumentException("Pet requires name");
            }
        }
        if (values.containsKey(PetsEntry.COLUMN_GENDER)){
            Integer gender = values.getAsInteger(PetsEntry.COLUMN_GENDER);
            if(gender == null || !PetsEntry.isValiGender(gender)){
                throw new IllegalArgumentException("Pet requires valid gender");
            }

        }
        if(values.containsKey(PetsEntry.COLUMN_WEIGHT)){
            Integer weight = values.getAsInteger(PetsEntry.COLUMN_WEIGHT);
            if(weight != null && weight < 0){
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }
        if(values.size() == 0){
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri, null);
        return db.update(PetsEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
