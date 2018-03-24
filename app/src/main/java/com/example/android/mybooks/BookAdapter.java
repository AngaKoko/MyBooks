package com.example.android.mybooks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ANGA KOKO on 7/28/2017.
 */

public class BookAdapter extends ArrayAdapter<Books> {
    Context context;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param books A List of book objects to display in a list
     */
    public BookAdapter(Activity context, ArrayList<Books> books) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, books);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Books currentBook = getItem(position);

        //Find the Title View in the list_items xml
        TextView tittleView = (TextView) listItemView.findViewById(R.id.title_of_book);
        //Set text of tittleTextView from the getTitle method in Books class
        tittleView.setText(currentBook.getTitle()+": ");

        //Find the SubTitle view in the list_items xml
        TextView subTittleView = (TextView) listItemView.findViewById(R.id.sut_title_of_book);
        //Set the text of the subtitle text view from the getSubTitle in Books class
        subTittleView.setText(currentBook.getSubTitle());

        //Find the Author/Year Text View in the list_items xml
        TextView authorYearTextView = (TextView) listItemView.findViewById(R.id.author_year);
        //Splitting the Date Text
        String date = currentBook.getYear();
        String parts[] = date.split("-");
        String part1 = "";
        if (date.contains("-")) {
            // Split it.
            part1 = "-"+parts[0];
        }
        //Set Text of Author/Year Text View using the getAuthor and getYear method in Book class
        authorYearTextView.setText(currentBook.getAuthor()+part1);

        //Find the Details text view in the list_items xml
        TextView detailsTextView = (TextView) listItemView.findViewById(R.id.book_description);
        //Reduce the string to be displayed
        String str = currentBook.getDescription();
        //if (str.length() > 46)
          //  str = str.substring(0, 43) + "...";
        //Set Text of Book Description text view using the getDescription method in Book class
        detailsTextView.setText(str);

        //Find the Image book image view
        ImageView smallImageView = (ImageView) listItemView.findViewById(R.id.book_image);

        String imageUrlSmall = currentBook.getImageLinkSmall();
        //Check if imageUrlSmall is empty or null
        if((!imageUrlSmall.isEmpty()) || (imageUrlSmall != null)) {
            //Convert String to url
            URL imgUrl = createUrl(imageUrlSmall);
            Glide.with(getContext())
                    .load(imgUrl)
                    .into(smallImageView);
        }

        // Return the whole list item layout (containing 3 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            //Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
}
