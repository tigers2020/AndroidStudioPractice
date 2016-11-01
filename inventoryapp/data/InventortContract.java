package com.example.tiger.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tiger on 2016-10-31.
 * contain every information for database. and table and columns
 */

public class InventortContract {

    private InventortContract(){}

    public static abstract class InvEntry implements BaseColumns{
        //content link for this app
        public static final String CONTENT_AUTHORITY = "com.example.tiger.inventoryapp";
        //base uri link for this app
        public static final Uri BASECONTENT_URL = Uri.parse("content://" + CONTENT_AUTHORITY);
        //path of this app
        public static final String PATH_INV = "inventory";
        //uri link for this app
        public static final Uri CONTENT_URL = Uri.parse(BASECONTENT_URL + "/" + PATH_INV);

        //get Cursor types
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INV;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INV;

        //Table name
        public static final String TABLE_NAME = "inventory";

        //Column names
        public static final String _ID = BaseColumns._ID;
        public static final String PRODUCT_NAME = "name";
        public static final String PRODUCT_QUANTITY = "quantity";
        public static final String PRODUCT_PRICE = "price";
        public static final String PRODUCT_DESCRIBES = "describes";
        public static final String PRODUCT_RATE = "rate";
        public static final String PRODUCT_CATEGORY = "category";


        //Category List
        public static final int CATEGORY_UNKOWN = 0;
        public static final int CATEGORY_ELECTRONICS = 1;
        public static final int CATEGORY_HOUSEHOLD = 2;
        public static final int CATEGORY_COMPUTER = 3;
        public static final int CATEGORY_BOOKS = 4;

        public static boolean isValidCategory(int category){
            if(category == CATEGORY_UNKOWN || category == CATEGORY_ELECTRONICS || category == CATEGORY_HOUSEHOLD || category == CATEGORY_COMPUTER || category == CATEGORY_BOOKS){
                return true;
            }
            return false;
        }

    }

}
