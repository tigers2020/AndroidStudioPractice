package com.example.tiger.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import static com.example.tiger.inventoryapp.data.InventortContract.InvEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = EditorActivity.class.getSimpleName();
    private static final int EXISTING_PRODUCT_LOADER = 1;
    private Uri mCurrentUri;
    private int mCategory;
    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;
    private RatingBar mRatingBar;
    private EditText mDescribeEditText;
    private Spinner mCategorySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();

        mCurrentUri = intent.getData();
        Log.v(LOG_TAG, "Receive mCurrentUri = " + mCurrentUri);
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);

        if(mCurrentUri == null){
            setTitle("Inert Product");
        }else{
            setTitle("Edit Product");
        }
        mNameEditText = (EditText) findViewById(R.id.editor_product_name);
        mQuantityEditText = (EditText) findViewById(R.id.editor_product_quantity);
        mPriceEditText = (EditText) findViewById(R.id.editor_product_price);
        mRatingBar = (RatingBar) findViewById(R.id.editor_rateBar_rate);
        mDescribeEditText = (EditText) findViewById(R.id.editor_product_describes);
        mCategorySpinner = (Spinner) findViewById(R.id.editor_spinner_category);


        Log.d(LOG_TAG, "onCreate done");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG_TAG, "OnCreateLoader");
        String[] projection = {
                InvEntry._ID,
                InvEntry.PRODUCT_NAME,
                InvEntry.PRODUCT_QUANTITY,
                InvEntry.PRODUCT_PRICE,
                InvEntry.PRODUCT_RATE,
                InvEntry.PRODUCT_DESCRIBES,
                InvEntry.PRODUCT_CATEGORY
        };
        Log.v(LOG_TAG, "Sending current URI: " + mCurrentUri + " and projection: " + projection);
        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");

        if(cursor == null || cursor.getCount() < 1){
            Log.e(LOG_TAG, "cursor is null or Count with: " + cursor.getCount());
            return;
        }
        if(cursor.moveToFirst()) {
            Log.v(LOG_TAG, "Cursor data: " + cursor);
            int nameColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_PRICE);
            int rateColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_RATE);
            int describeColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_DESCRIBES);
            int catalogColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_CATEGORY);

            //set values from column index

            String name = cursor.getString(nameColumnIndex);

            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            float rate = cursor.getFloat(rateColumnIndex);
            String describes = cursor.getString(describeColumnIndex);
            mCategory = cursor.getInt(catalogColumnIndex);

            //set values into View

            mNameEditText.setText(name);
            mQuantityEditText.setText(Integer.toString(quantity));
            mPriceEditText.setText(Integer.toString(price));
            mRatingBar.setRating(rate);
            mDescribeEditText.setText(describes);
            mCategorySpinner.setSelection(mCategory);

            setupSpinner();
        }

    }

    private void setupSpinner() {
            ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this, R.array.array_catalog_options, android.R.layout.simple_spinner_item);

            categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

            mCategorySpinner.setAdapter(categoryAdapter);

            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selection = (String) adapterView.getItemAtPosition(i);

                    if(!TextUtils.isEmpty(selection)){
                        if(selection.equals("Electronic")){
                            mCategory = 1;
                        }else if(selection.equals("House Hold")){
                            mCategory = 2;
                        }else if (selection.equals("Computer")){
                            mCategory = 3;
                        }else if (selection.equals("Books")){
                            mCategory = 4;
                        }else {
                            mCategory = 0;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    mCategory = 0;

                }
            });
        }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(LOG_TAG, "OnLoaderReset");
        mNameEditText.setText("");
        mQuantityEditText.setText("");
        mPriceEditText.setText("");
        mRatingBar.setRating(0);
        mDescribeEditText.setText("");
        mCategorySpinner.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_edit_cancel:
                return true;
            case R.id.action_edit_save:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
