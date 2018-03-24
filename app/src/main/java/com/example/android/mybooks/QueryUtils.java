package com.example.android.mybooks;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by ANGA KOKO on 7/29/2017.
 */

public class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return an {@link Books} object to represent a single earthquake.
     */
    public static ArrayList<Books> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Books> books = extractBooks(jsonResponse);

        // Return the {@link Event}
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Books} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Books> extractBooks(String bookJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Books to
        ArrayList<Books> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject root = new JSONObject(bookJSON);
            JSONArray bookArray = root.getJSONArray("items");
            if(bookArray.length() > 0) {
                for (int i = 0; i < bookArray.length(); i++) {
                    JSONObject currentBook = bookArray.getJSONObject(i);
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                    //Check if Json has key "title"
                    // Then Extract the value for the key called "Title"
                    String title = "";
                    if(volumeInfo.has("title")){
                        title = volumeInfo.getString("title");
                    }

                    //Check if Json has key "subtitle"
                    //and Extract the value of the key called "Sub Title"
                    String subTitle = "";
                    if(volumeInfo.has("subtitle")) {
                        subTitle = volumeInfo.getString("subtitle");
                    }

                    //Check if Json has key "authors"
                    //Then Extract the value of the key called "Authors'
                    String authors = "";
                    if(volumeInfo.has("authors")){
                        authors = volumeInfo.getString("authors");
                    }

                    //Check if Json has key "description"
                    //Then Extract the value of the key called "description"
                    String description = "No available description";
                    if(volumeInfo.has("description")) {
                        description = volumeInfo.getString("description");
                    }

                    //Check if Json has key "publishedDate"
                    //Then Extract the value of the key called "publishDate"
                    String date = "";
                    if(volumeInfo.has("publishedDate")){
                        date = volumeInfo.getString("publishedDate");
                    }

                    //Check if Json has key "smallThumbnail"
                    //Then Extract the value of the key called "smallThumbnail"
                    String imageLinkSmall = "";
                    if(imageLinks.has("smallThumbnail")){
                        imageLinkSmall = imageLinks.getString("smallThumbnail");
                    }

                    //Check if Json has key "thumbnail"
                    //Then Extract the value of the key called "thumbnail"
                    String imageLinkLarge = "";
                    if(imageLinks.has("thumbnail")){
                        imageLinkLarge = imageLinks.getString("thumbnail");
                    }

                    //Check if Json has key "selfLink"
                    //Then Extract the value of the key called "selfLink"
                    String url = "";
                    if(volumeInfo.has("previewLink")){
                        url = volumeInfo.getString("previewLink");
                    }

                    Books book = new Books(title, subTitle, description, authors, date, url, imageLinkSmall, imageLinkLarge);
                    books.add(book);
                }
                // Return the list of books
                return books;
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return null;
    }
}
