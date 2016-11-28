package com.example.pomkim.booklist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by pomkim on 11/25/16.
 */
public class BookListFragment extends Fragment {
    private static final String LOG_TAG = BookListFragment.class.getSimpleName();
    private ListView mBookListView;
    private BookAdapter mAdapter;
    private List<Book> mBooks;

    public static BookListFragment newInstance() {
        return new BookListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_list_view, container, false);

        mBookListView = (ListView) view.findViewById(R.id.book_list_view);
        mBooks = BookUtil.extractBooks();
        mAdapter = new BookAdapter(this.getContext(), mBooks);
        mBookListView.setAdapter(mAdapter);
        mBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = mAdapter.getItem(i);
                UUID bookId = book.getBookId();


                Intent intent = BookDetailActivity.newIntent(getActivity(), bookId);

                startActivity(intent);
            }
        });
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mBooks = new ArrayList<>();
    }

    private static class BookHolder {
        public TextView bookTitle;
        public TextView bookSubTitle;
        public TextView bookDescription;
        public TextView bookAuthors;

    }

    private class BookAdapter extends ArrayAdapter<Book> {


        public BookAdapter(Context context, List<Book> books) {
            super(context, 0, books);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Book book = getItem(position);
            BookHolder bookHolder = new BookHolder();

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);

                bookHolder.bookTitle = (TextView) view.findViewById(R.id.book_list_item_book_title);
                bookHolder.bookSubTitle = (TextView) view.findViewById(R.id.book_list_item_book_subtitle);
                bookHolder.bookDescription = (TextView) view.findViewById(R.id.book_list_item_book_description);
                bookHolder.bookAuthors = (TextView) view.findViewById(R.id.book_list_item_book_authors);

                view.setTag(bookHolder);
            } else {
                bookHolder = (BookHolder) view.getTag();

            }


            bookHolder.bookTitle.setText(book.getBookTitle());
            bookHolder.bookSubTitle.setText(book.getBookSubTitle());
            bookHolder.bookDescription.setText(book.getBookDescription());
            bookHolder.bookAuthors.setText(book.getBookAuthors());
            return view;
        }
    }
}
