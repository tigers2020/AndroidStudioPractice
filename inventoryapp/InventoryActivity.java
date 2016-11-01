package com.example.tiger.inventoryapp;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tiger.inventoryapp.data.InventortContract.InvEntry;

public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    //Identifier for inv pet data loader
    private static final int INV_LOADER = 0;
    //Adapter for the ListView
    InvCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        //setup floating action button
        FloatingActionButton fab =(FloatingActionButton) findViewById(R.id.fab);
        //set OnclickListener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setIntent
                Intent intent = new Intent(InventoryActivity.this, EditorActivity.class);
                //open new activity
                startActivity(intent);
            }
        });



        //set the ListView
        ListView invListView = (ListView) findViewById(R.id.list);

        invListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(InventoryActivity.this, DetailActivity.class);

                Uri currentUri = ContentUris.withAppendedId(InvEntry.CONTENT_URL, id);

                intent.setData(currentUri);
//                getActionBar().setDisplayHomeAsUpEnabled(true);

                startActivity(intent);
            }

        });
        //set emptyView
        View emptyView = findViewById(R.id.empty_view);
        invListView.setEmptyView(emptyView);

        //initialize mCursorAdapter
        mCursorAdapter = new InvCursorAdapter(this, null);
        invListView.setAdapter(mCursorAdapter);


        //Kick off the loader
        getLoaderManager().initLoader(INV_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //set the menu with insert dummy data
        switch (item.getItemId())
        {
            case R.id.action_insert_dummy_data:
                insertDummy();
                return true;
            case R.id.action_delete_all:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertDummy() {
        ContentValues values = new ContentValues();
        values.put(InvEntry.PRODUCT_NAME, "Headphone");
        values.put(InvEntry.PRODUCT_QUANTITY, 5);
        values.put(InvEntry.PRODUCT_PRICE, 150);
        values.put(InvEntry.PRODUCT_CATEGORY, 1);
        values.put(InvEntry.PRODUCT_RATE, 3);
        values.put(InvEntry.PRODUCT_DESCRIBES, "veryGood!!");

        Uri newUri = getContentResolver().insert(InvEntry.CONTENT_URL, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InvEntry._ID,
                InvEntry.PRODUCT_NAME,
                InvEntry.PRODUCT_QUANTITY,
                InvEntry.PRODUCT_PRICE        };

        return new CursorLoader(this, InvEntry.CONTENT_URL, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
