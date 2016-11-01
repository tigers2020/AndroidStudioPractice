package com.example.tiger.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tiger.inventoryapp.data.InventortContract;

/**
 * Created by tiger on 2016-10-31.
 * CursorAdapter for update UI when database change or load.
 */

public class InvCursorAdapter extends CursorAdapter {
    public InvCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Set TextViews
        TextView productNameView = (TextView) view.findViewById(R.id.listProductName);
        TextView productQuantityView = (TextView) view.findViewById(R.id.listProductQuantity);
        TextView productPriceView = (TextView) view.findViewById(R.id.listProductPrice);

        //Get Column Index from data
        int nameColumnIndex = cursor.getColumnIndex(InventortContract.InvEntry.PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(InventortContract.InvEntry.PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventortContract.InvEntry.PRODUCT_PRICE);

        //convert column index to String
        String name = cursor.getString(nameColumnIndex);
        String quantity = Integer.toString(cursor.getInt(quantityColumnIndex));
        String price = Integer.toString(cursor.getInt(priceColumnIndex));

        //Set to TextView as String
        productNameView.setText(name);
        productQuantityView.setText(quantity);
        productPriceView.setText(price);
    }
}

