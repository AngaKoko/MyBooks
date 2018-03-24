package com.example.android.mybooks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Books>> {

    private BookAdapter bookAdapter;

    private String GOOGLE_BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q="+word+"&maxResults=40";

    private static String word = "php";

    private final int EARTHQUAKE_LOADER_ID = 1;

    ProgressBar progressBar;

    TextView emptyStateTextView;

    ListView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find reference to  the list view
        bookListView = (ListView) findViewById(R.id.list_view);

        //Find reference to the progressBar
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);

        ///Find reference to the Empty State Text View
        emptyStateTextView = (TextView) findViewById(R.id.empty_state_view);

        //Set the text view as the empty state view of the list view
        bookListView.setEmptyView(emptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Create a new adapter that takes an empty list of earthquakes as input
        bookAdapter = new BookAdapter(this, new ArrayList<Books>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(bookAdapter);

        //Find Reference to the search view
        SearchView searchView = (SearchView)findViewById(R.id.search_view);
        //setOnQueryTextListener or the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText) {
                // your text view here
                word = newText;
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                //new url
                GOOGLE_BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q="+query+"&maxResults=40";

                Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();

                //Restart our loader manager Already disposed: Module: 'app'
                getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, null, MainActivity.this);
                return false;
            }
        });

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Books currentBook = bookAdapter.getItem(position);

                String bookUrl = currentBook.getUrl();
                //Check if url is equal null or is empty
                if((bookUrl != null)||(!bookUrl.isEmpty())) {

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri url = Uri.parse(currentBook.getUrl());

                    // Create a new intent to view the earthquake URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, url);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            }
        });
    }

    @Override
    public Loader<ArrayList<Books>> onCreateLoader(int i, Bundle bundle){

        //Make the progressbar Visible when thread Starts
        progressBar.setVisibility(View.VISIBLE);

        //Calls the constructor in the BookLoader class
        return new BookLoader(this, GOOGLE_BOOK_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Books>> loader, ArrayList<Books> books){
        //Make the progressbar vanish when thread ends
        progressBar.setVisibility(View.GONE);

        //Make set the text of the Empty state view
        emptyStateTextView.setText(R.string.nothing_to_shot);

        //Clears the book adapter for new value
        bookAdapter.clear();

        // If there is a valid list of {@link Books}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            bookAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Books>> loader){
        //Remove all values from bookAdapter
        bookAdapter.clear();
    }

}
