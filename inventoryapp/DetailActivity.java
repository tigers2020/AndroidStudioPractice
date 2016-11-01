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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tiger.inventoryapp.data.InventoryDbHelper;

import static com.example.tiger.inventoryapp.data.InventortContract.InvEntry;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final static  String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final int EXISTING_INV_LOADER = 1;

    //Dbhelper for callback data
    InventoryDbHelper mDbHelper;

    //Details fields(editable)

    private TextView mNameEditText;
    private TextView mQuantityEditText;
    private TextView mPriceEditText;
    private RatingBar mRatingBar;
    private TextView mDescribeEditText;
    private Spinner mCategorySpinner;

    private int mCategory = 0;

    private Uri mCurrentInvUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        mCurrentInvUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_INV_LOADER, null, this);

        mNameEditText = (TextView) findViewById(R.id.detail_editor_product_name);
        mQuantityEditText = (TextView) findViewById(R.id.detail_editor_product_quantity);
        mPriceEditText = (TextView) findViewById(R.id.detail_editor_product_price);
        mRatingBar = (RatingBar) findViewById(R.id.detail_rateBar_rate);
        mDescribeEditText = (TextView) findViewById(R.id.detail_editor_product_describes);
        mCategorySpinner = (Spinner) findViewById(R.id.detail_spinner_category);

        setupSpinner();

    }

    private void setupSpinner() {
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this, R.array.array_catalog_options, android.R.layout.simple_spinner_item);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mCategorySpinner.setAdapter(categoryAdapter);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String) adapterView.getItemAtPosition(i);
                /*
                      public static final int CATEGORY_UNKOWN = 0;
        public static final int CATEGORY_ELECTRONICS = 1;
        public static final int CATEGORY_HOUSEHOLD = 2;
        public static final int CATEGORY_COMPUTER = 3;
        public static final int CATEGORY_BOOKS = 4;
                 */
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InvEntry._ID,
                InvEntry.PRODUCT_NAME,
                InvEntry.PRODUCT_QUANTITY,
                InvEntry.PRODUCT_PRICE,
                InvEntry.PRODUCT_RATE,
                InvEntry.PRODUCT_DESCRIBES,
                InvEntry.PRODUCT_CATEGORY};
        return new CursorLoader(this, mCurrentInvUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor == null || cursor.getCount() < 1){
            return;
        }
if(cursor.moveToFirst()) {
    int nameColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_NAME);
    int quantityColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_QUANTITY);
    int priceColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_PRICE);
    int rateColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_RATE);
    int describeColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_DESCRIBES);
    int catalogColumnIndex = cursor.getColumnIndex(InvEntry.PRODUCT_CATEGORY);

    //set values from column index

    String name = cursor.getString(nameColumnIndex);

    setTitle("Detail - " + name);

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
    switch (mCategory) {
            /*
                  public static final int CATEGORY_UNKOWN = 0;
        public static final int CATEGORY_ELECTRONICS = 1;
        public static final int CATEGORY_HOUSEHOLD = 2;
        public static final int CATEGORY_COMPUTER = 3;
        public static final int CATEGORY_BOOKS = 4;
             */
        case InvEntry.CATEGORY_ELECTRONICS:
            mCategorySpinner.setSelection(1);
            break;
        case InvEntry.CATEGORY_HOUSEHOLD:
            mCategorySpinner.setSelection(2);
            break;
        case InvEntry.CATEGORY_COMPUTER:
            mCategorySpinner.setSelection(3);
            break;
        case InvEntry.CATEGORY_BOOKS:
            mCategorySpinner.setSelection(4);
            break;
        default:
            mCategorySpinner.setSelection(0);
            break;

    }
}
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mQuantityEditText.setText("");
        mPriceEditText.setText("");
        mRatingBar.setRating(0);
        mDescribeEditText.setText("");
        mCategorySpinner.setSelection(0);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_edit_production:
                editProduction();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editProduction() {
        Intent intent = new Intent(DetailActivity.this, EditorActivity.class);

        intent.setData(mCurrentInvUri);
        Log.v(LOG_TAG, "send intent mCurrentInvUri = " + mCurrentInvUri);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        return true;
    }
}
