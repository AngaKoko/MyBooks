package com.example.android.mybooks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by ANGA KOKO on 7/30/2017.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<Books>> {

    String mUrl;

    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;

    }

    @Override
    protected void onStartLoading(){forceLoad();}

    public ArrayList<Books> loadInBackground(){

        //Calls the fetchBookData method from the QueryUtils class if URL is not empty
        //Returns an array of the data
        if(mUrl != null || !mUrl.isEmpty()){
            ArrayList<Books> books = QueryUtils.fetchBookData(mUrl);
            return books;
        }

        //Returns null if URL is empty
        return null;
    }
}
