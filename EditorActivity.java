/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.data.PetContract.PetsEntry;
import com.example.android.pets.data.PetDbHelper;


/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EditorActivity.class.getSimpleName();
    private static final int EXISTING_PET_LOADER = 0;

    PetDbHelper mPetDbHelper;

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = PetsEntry.GENDER_UNKNOWN;

    private Uri mCurrentPetUri;
    private PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();


        if (mCurrentPetUri == null) {
            setTitle(R.string.editor_activity_title_new_pet);

        }else {
            setTitle(getString(R.string.editor_activity_title_edit_pet));
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();


        mPetDbHelper = new PetDbHelper(this);
        mCursorAdapter = new PetCursorAdapter(this, null);
        getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);

    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = 1; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = 2; // Female
                    } else {
                        mGender = 0; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    private void savePet()    {
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();
        int weight = Integer.parseInt(weightString);

      //  SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetsEntry.COLUMN_NAME, nameString);
        values.put(PetsEntry.COLUMN_BREED, breedString);
        values.put(PetsEntry.COLUMN_GENDER, mGender);
        values.put(PetsEntry.COLUMN_WEIGHT, weight);

       // long rowId = db.insert(PetsEntry.TABLE_NAME, null, values);
if(mCurrentPetUri == null) {
    Uri newUri = getContentResolver().insert(PetsEntry.CONTENT_URI, values);

    if (newUri == null) {
        Toast.makeText(this, R.string.editor_insert_pet_failed, Toast.LENGTH_SHORT).show();

    } else {
        Toast.makeText(this, R.string.editor_insert_pet_successful, Toast.LENGTH_SHORT).show();
    }
    Log.v(LOG_TAG, "Insert DB Id: " + newUri);
}else{
    int rowAffected = getContentResolver().update(mCurrentPetUri, values, null, null);
    if (rowAffected == 0) {
        Toast.makeText(this, R.string.editor_update_pet_failed, Toast.LENGTH_SHORT).show();

    } else {
        Toast.makeText(this, R.string.editor_upate_pet_successful, Toast.LENGTH_SHORT).show();
    }
    Log.v(LOG_TAG, "Updated Id: " + rowAffected);

}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                savePet();
                finish();
                // Do nothing for now
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PetsEntry._ID,
                PetsEntry.COLUMN_NAME,
                PetsEntry.COLUMN_BREED,
        PetsEntry.COLUMN_GENDER,
        PetsEntry.COLUMN_WEIGHT};

        return new android.content.CursorLoader(this, mCurrentPetUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       if(data.moveToFirst()){
           int nameColumnIndex= data.getColumnIndex(PetsEntry.COLUMN_NAME);
           int breedColumnIndex= data.getColumnIndex(PetsEntry.COLUMN_BREED);
           int genderColumnIndex = data.getColumnIndex(PetsEntry.COLUMN_GENDER);
           int weightColumnIndex = data.getColumnIndex(PetsEntry.COLUMN_WEIGHT);

           String name = data.getColumnName(nameColumnIndex);
           String breed = data.getColumnName(breedColumnIndex);
           int gender = data.getInt(genderColumnIndex);
           int weight = data.getInt(weightColumnIndex);

           mNameEditText.setText(name);
           mBreedEditText.setText(breed);
           mWeightEditText.setText(weight);
           switch (gender)
           {
               case PetsEntry.GENDER_MALE:
                   mGenderSpinner.setSelection(1);
                   break;
               case PetsEntry.GENDER_FEMALE:
                   mGenderSpinner.setSelection(2);
                   break;
               case PetsEntry.GENDER_UNKNOWN:
                   mGenderSpinner.setSelection(3);
                   break;
           }
       }
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}