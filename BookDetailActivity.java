package com.example.pomkim.booklist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class BookDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_BOOK_ID = "com.example.pomkim.booklist.book_id";

    public static Intent newIntent(Context packageContext, UUID bookId) {
        Intent intent = new Intent(packageContext, BookDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_ID, bookId);
        return intent;

    }
    @Override
    protected Fragment createFragment() {
        UUID bookId = (UUID) getIntent().getSerializableExtra(EXTRA_BOOK_ID);
        return BookDetailFragment.newInstance(bookId);
    }


}
