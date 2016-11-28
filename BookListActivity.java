package com.example.pomkim.booklist;

import android.support.v4.app.Fragment;

public class BookListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return BookListFragment.newInstance();
    }
}
