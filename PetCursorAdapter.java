package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * Created by tiger on 2016-10-31.
 */

public class PetCursorAdapter extends CursorAdapter{
    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameView = (TextView) view.findViewById(R.id.pet_name);
        TextView breedView = (TextView) view.findViewById(R.id.pet_breed);

        int nameColumnIndex = cursor.getColumnIndex(PetContract.PetsEntry.COLUMN_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetContract.PetsEntry.COLUMN_BREED);

        String petName = cursor.getString(nameColumnIndex);
        String petBreed = cursor.getString(breedColumnIndex);

        nameView.setText(petName);
        breedView.setText(petBreed);

    }
}
