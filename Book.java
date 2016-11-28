package com.example.pomkim.booklist;

import android.net.Uri;

import java.util.UUID;

/**
 * Created by pomkim on 11/25/16.
 */

public class Book {
    private UUID mBookId;
    private String mBookTitle;
    private String mBookSubTitle;
    private String mBookDescription;
    private String mBookAuthors;
    private Uri mBookUri;
    private String mBookPublisher;
    private String mBookPublishedDate;
    private String mBookCount;
    private float mBookRate;
    private String mBookCoverImage;

    public Book(String bookTitle, String bookSubTitle, String bookDescription, String bookAuthors, Uri bookUri, String bookPublisher, String bookPublishedDate, String bookCount, float bookRate, String bookCoverImage) {
        mBookTitle = bookTitle;
        mBookSubTitle = bookSubTitle;
        mBookDescription = bookDescription;
        mBookAuthors = bookAuthors;
        mBookUri = bookUri;
        mBookPublisher = bookPublisher;
        mBookPublishedDate = bookPublishedDate;
        mBookCount = bookCount;
        mBookRate = bookRate;
        mBookCoverImage = bookCoverImage;
        mBookId = UUID.randomUUID();

    }

    public UUID getBookId() {
        return mBookId;
    }

    public String getBookTitle() {
        return mBookTitle;
    }


    public String getBookSubTitle() {
        return mBookSubTitle;
    }


    public String getBookDescription() {
        return mBookDescription;
    }


    public String getBookAuthors() {
        return mBookAuthors;
    }


    public Uri getBookUri() {
        return mBookUri;
    }

    public String getBookPublisher() {
        return mBookPublisher;
    }

    public String getBookPublishedDate() {
        return mBookPublishedDate;
    }

    public String getBookCount() {
        return mBookCount;
    }

    public float getBookRate() {
        return mBookRate;
    }

    public String  getBookCoverImage() {
        return mBookCoverImage;
    }
}
