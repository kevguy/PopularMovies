package com.example.android.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.MovieData;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieContract.UserEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieData>> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private NetworkImageAdapter mImageMovieAdapter;
    private ArrayList<MovieData> mMovieDataArray;
    private final Context mContext;

    public FetchMovieTask(Context context, NetworkImageAdapter ImageMovieAdapter, ArrayList<MovieData> MovieDataArray) {
        mContext = context;
        mImageMovieAdapter = ImageMovieAdapter;
        mMovieDataArray = MovieDataArray;
    }

    private boolean DEBUG = true;


    /**
     * Helper method to handle insertion of a new location in the weather database.
     *
     * @param movieId The location string used to request updates from the server.
     * @param title A human-readable city name, e.g "Kev Lai Rocks"
     * @return the row ID of the added location.
     */
    long addMovie(String movieId,
                  String title,
                  String backdropPath,
                  String adult,
                  String orgLang,
                  String orgTitle,
                  String overview,
                  String relDate,
                  String posterPath) {
        // Students: First, check if the movieId with this title exists in the db
        // If it exists, return the current ID
        // Otherwise, insert it using the content resolver and the base URI
        long locationId;

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry._ID},
                MovieContract.MovieEntry.COLUMN_ORG_TITLE + " = ?",
                new String[]{title},
                null);

        if (movieCursor.moveToFirst()) {
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            locationId = movieCursor.getLong(movieIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues movieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows object to hold the data you want to insert.
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
            movieValues.put(MovieContract.MovieEntry.COLUMN_ORG_TITLE, orgTitle);
            movieValues.put(MovieContract.MovieEntry.COLUMN_ORG_LANG, orgLang);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
            movieValues.put(MovieContract.MovieEntry.COLUMN_ADULT, adult);
            movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, backdropPath);
            movieValues.put(MovieContract.MovieEntry.COLUMN_REL_DATE, relDate);
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);

            // Finally, insert movie data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    movieValues
            );

            // The resulting URI contains the ID for the row. Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }

        movieCursor.close();
        // Wait, that worked? Yes!
        return locationId;
    }




    @Override
    protected ArrayList<MovieData> doInBackground(String... params) {

        if (params.length == 0){
            return null;
        }
        String locationQuery = params[0];


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        // Sort by popularity in descending order
        //String sort_by = "popularity.desc";
        // Sort by popularity in ascending order
        //String sort_by = "popularity.asc";
        // Sort by vote count in descending order
        //String sort_by = "vote_count.desc";
        // Sort by vote count in ascending order
        //String sort_by = "vote_count.asc";

        try{
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            final String API_KEY = "1f81622504d93fd944c771a94fefecfb";

            // Construct the URL for the query
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter(SORT_BY_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, API_KEY);
            URL url = new URL(builder.build().toString());
            Log.v(LOG_TAG, url.toString());

            //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=1f81622504d93fd944c771a94fefecfb");
            // http://api.themoviedb.org/3/movie/24428/videos?api_key=1f81622504d93fd944c771a94fefecfb
            // http://api.themoviedb.org/3/movie/24428/reviews?api_key=1f81622504d93fd944c771a94fefecfb
            // https://jsonformatter.curiousconcept.com/

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null){
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0){
                // Stream was empty. No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
            Log.v(LOG_TAG, movieJsonStr);


        }
        catch (IOException e){
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            ArrayList<MovieData> movieDataArray = new ArrayList<MovieData>();
            movieDataArray = getMovieDataFromJson(movieJsonStr);
            for (int k = 0; k < movieDataArray.size(); ++k) {
                String YouTubeLink = new String(getMovieYouTubeStuff(movieDataArray.get(k).getId()));
                movieDataArray.get(k).setYouTube(YouTubeLink);
                String ReviewLink = new String(getMovieReviewStuff(movieDataArray.get(k).getId()));
                movieDataArray.get(k).setReview(ReviewLink);
            }


            return movieDataArray;
        }
        catch (JSONException e){
            Log.e(LOG_TAG, "Error getting weather data from json", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieData> result) {
        if (result != null) {
            //mMovieAdapter.clear();
            mImageMovieAdapter.clear();
            for (MovieData movieDataItem : result){
                mImageMovieAdapter.add("http://image.tmdb.org/t/p/w185/" + movieDataItem.getPosterPath());
            }
            mImageMovieAdapter.mSuckDickMovieDataArray = result;
            mMovieDataArray = result;
        }
    }


    private String getMovieYouTubeStuff(long movieId) {

        //String locationQuery = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieYouTubeJsonStr = null;

        try {
            final String API_KEY_PARAM = "api_key";
            final String API_KEY = "1f81622504d93fd944c771a94fefecfb";

            // Construct the URL for the query
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(Long.toString(movieId))
                    .appendPath("videos")
                    .appendQueryParameter(API_KEY_PARAM, API_KEY);
            URL url = new URL(builder.build().toString());
            Log.v(LOG_TAG, url.toString());


            //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=1f81622504d93fd944c771a94fefecfb");
            // http://api.themoviedb.org/3/movie/24428/videos?api_key=1f81622504d93fd944c771a94fefecfb
            // http://api.themoviedb.org/3/movie/24428/reviews?api_key=1f81622504d93fd944c771a94fefecfb
            // https://jsonformatter.curiousconcept.com/

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            movieYouTubeJsonStr = buffer.toString();
            Log.v(LOG_TAG, movieYouTubeJsonStr);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            //ArrayList<MovieData> movieDataArray = new ArrayList<MovieData>();
            String link = new String(getMovieYouTubeFromJson(movieYouTubeJsonStr));
            return link;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error getting weather data from json", e);
            e.printStackTrace();
        }
        return null;
    }

    private String getMovieYouTubeFromJson(String movieYouTubeJsonStr)
            throws JSONException {
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULT = "results";
        final String OWM_SITE = "site";
        final String OWM_KEY = "key";

        //try {
        JSONObject movieJson = new JSONObject(movieYouTubeJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_RESULT);

        // Insert the new weather information into the database
        //Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

        StringBuilder link = new StringBuilder();

        for (int i = 0; i < movieArray.length(); ++i) {
            JSONObject itemMovie = movieArray.getJSONObject(i);

            // https://www.youtube.com/watch?v=kXYiU_JCYtU
            if (itemMovie.getString(OWM_SITE).equals("YouTube")){
                link.append(",http://www.youtube.com/watch?v=" + itemMovie.getString(OWM_KEY));
            }

            //ContentValues movieValues = new ContentValues();

            //long locationId = addMovie(Long.toString(movieData.getId()),
                   // movieData.getTitle(),
                   // movieData.getBackdropPath(),
                   // Boolean.toString(movieData.getAdult()),
                  //  movieData.getOriginalLanguage(),
                  //  movieData.getOriginalTitle(),
                  //  movieData.getOverview(),
                  //  movieData.getReleaseDate(),
                  //  movieData.getPosterPath());


            //movieValues.put(UserEntry.COLUMN_MOVIE_KEY, locationId);
            //movieValues.put(UserEntry.COLUMN_VOTE_AVG, Double.toString(movieDataArray.get(i).getVoteAvg()));
            //movieValues.put(UserEntry.COLUMN_VOTE_COUNT, Integer.toString(movieDataArray.get(i).getVoteCount()));
            //movieValues.put(UserEntry.COLUMN_VIDEO, Boolean.toString(movieDataArray.get(i).getVideo()));
            //movieValues.put(UserEntry.COLUMN_FAVORITE, Boolean.toString(true));
            //movieValues.put(UserEntry.COLUMN_POPULARITY, Double.toString(movieDataArray.get(i).getPopularity()));

            //cVVector.add(movieValues);
        }

        if (!(link.toString().equals(""))){
            link.deleteCharAt(0);
        }
        else
            return "Nothing";

        Log.v(LOG_TAG, "YouTube " + link.toString());

        // add to database
        //if (cVVector.size() > 0) {
        //    // Student: call bulkInsert to add the movieEntries to the database here
         //   ContentValues[] cvArray = new ContentValues[cVVector.size()];
        //    cVVector.toArray(cvArray);
        //    mContext.getContentResolver().bulkInsert(UserEntry.CONTENT_URI, cvArray);
        //}

        //Cursor cur = mContext.getContentResolver().query(
        //        UserEntry.CONTENT_URI,
        //        null,
        //        null,
        //        null,
        //        null
       // );

        //cVVector = new Vector<ContentValues>(cur.getCount());
        //if (cur.moveToFirst()) {
        //    do {
       //         ContentValues cv = new ContentValues();
       //         DatabaseUtils.cursorRowToContentValues(cur, cv);
       //         cVVector.add(cv);
        //    } while (cur.moveToNext());
        //}

        //Log.d(LOG_TAG, "FetchWeatherTask Complete. " + cVVector.size() + " Inserted");

        return link.toString();
        //} catch (JSONException e) {
        //Log.e(LOG_TAG, e.getMessage(), e);
        //e.printStackTrace();
        //}
        //return null;
    }

    private String getMovieReviewStuff(long movieId) {

        //String locationQuery = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieYouTubeJsonStr = null;

        try {
            final String API_KEY_PARAM = "api_key";
            final String API_KEY = "1f81622504d93fd944c771a94fefecfb";

            // Construct the URL for the query
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(Long.toString(movieId))
                    .appendPath("reviews")
                    .appendQueryParameter(API_KEY_PARAM, API_KEY);
            URL url = new URL(builder.build().toString());
            Log.v(LOG_TAG, url.toString());


            //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=1f81622504d93fd944c771a94fefecfb");
            // http://api.themoviedb.org/3/movie/24428/videos?api_key=1f81622504d93fd944c771a94fefecfb
            // http://api.themoviedb.org/3/movie/24428/reviews?api_key=1f81622504d93fd944c771a94fefecfb
            // https://jsonformatter.curiousconcept.com/

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            movieYouTubeJsonStr = buffer.toString();
            Log.v(LOG_TAG, movieYouTubeJsonStr);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            //ArrayList<MovieData> movieDataArray = new ArrayList<MovieData>();
            String link = new String(getMovieReviewFromJson(movieYouTubeJsonStr));
            return link;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error getting weather data from json", e);
            e.printStackTrace();
        }
        return null;
    }

    private String getMovieReviewFromJson(String movieYouTubeJsonStr)
            throws JSONException {
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULT = "results";
        final String OWM_AUTHOR = "author";
        final String OWM_CONTENT = "content";

        //try {
        JSONObject movieJson = new JSONObject(movieYouTubeJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_RESULT);

        // Insert the new weather information into the database
        //Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

        StringBuilder link = new StringBuilder();

        for (int i = 0; i < movieArray.length(); ++i) {
            JSONObject itemMovie = movieArray.getJSONObject(i);

            // <author>...</author><content></content>
            link.append("<author>" + itemMovie.getString(OWM_AUTHOR) + "</author>");
            link.append("<content>" + itemMovie.getString(OWM_CONTENT) + "</content>");


            //ContentValues movieValues = new ContentValues();

            //long locationId = addMovie(Long.toString(movieData.getId()),
            // movieData.getTitle(),
            // movieData.getBackdropPath(),
            // Boolean.toString(movieData.getAdult()),
            //  movieData.getOriginalLanguage(),
            //  movieData.getOriginalTitle(),
            //  movieData.getOverview(),
            //  movieData.getReleaseDate(),
            //  movieData.getPosterPath());


            //movieValues.put(UserEntry.COLUMN_MOVIE_KEY, locationId);
            //movieValues.put(UserEntry.COLUMN_VOTE_AVG, Double.toString(movieDataArray.get(i).getVoteAvg()));
            //movieValues.put(UserEntry.COLUMN_VOTE_COUNT, Integer.toString(movieDataArray.get(i).getVoteCount()));
            //movieValues.put(UserEntry.COLUMN_VIDEO, Boolean.toString(movieDataArray.get(i).getVideo()));
            //movieValues.put(UserEntry.COLUMN_FAVORITE, Boolean.toString(true));
            //movieValues.put(UserEntry.COLUMN_POPULARITY, Double.toString(movieDataArray.get(i).getPopularity()));

            //cVVector.add(movieValues);
        }

        if (link.toString().equals("")){
            return "Nothing";
        }

        Log.v(LOG_TAG, "Review " + link.toString());

        // add to database
        //if (cVVector.size() > 0) {
        //    // Student: call bulkInsert to add the movieEntries to the database here
        //   ContentValues[] cvArray = new ContentValues[cVVector.size()];
        //    cVVector.toArray(cvArray);
        //    mContext.getContentResolver().bulkInsert(UserEntry.CONTENT_URI, cvArray);
        //}

        //Cursor cur = mContext.getContentResolver().query(
        //        UserEntry.CONTENT_URI,
        //        null,
        //        null,
        //        null,
        //        null
        // );

        //cVVector = new Vector<ContentValues>(cur.getCount());
        //if (cur.moveToFirst()) {
        //    do {
        //         ContentValues cv = new ContentValues();
        //         DatabaseUtils.cursorRowToContentValues(cur, cv);
        //         cVVector.add(cv);
        //    } while (cur.moveToNext());
        //}

        //Log.d(LOG_TAG, "FetchWeatherTask Complete. " + cVVector.size() + " Inserted");

        return link.toString();
        //} catch (JSONException e) {
        //Log.e(LOG_TAG, e.getMessage(), e);
        //e.printStackTrace();
        //}
        //return null;
    }





    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private ArrayList<MovieData> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULT = "results";
        final String OWM_ADULT = "adult";
        final String OWM_BACKDROP_PATH = "backdrop_path";
        final String OWM_GENRE_IDS = "genre_ids";
        final String OWM_ID = "id";
        final String OWM_ORG_LANG = "original_language";
        final String OWM_ORG_TITLE = "original_title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_REL_DATE = "release_date";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_POPULARITY = "popularity";
        final String OWM_TITLE = "title";
        final String OWM_VIDEO = "video";
        final String OWM_VOTE_AVG = "vote_average";
        final String OWM_VOTE_COUNT = "vote_count";
        //try {
        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_RESULT);

        ArrayList<MovieData> movieDataArray = new ArrayList<MovieData>();


        // Insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

        for (int i = 0; i < movieArray.length(); ++i) {
            JSONObject itemMovie = movieArray.getJSONObject(i);


            MovieData movieData = new MovieData();

            movieData.setAdult(itemMovie.getBoolean(OWM_ADULT));
            movieData.setBackdropPath(itemMovie.getString(OWM_BACKDROP_PATH));
            JSONArray tmp = itemMovie.getJSONArray(OWM_GENRE_IDS);
            ArrayList<Integer> list = new ArrayList<Integer>();
            // Genre Ids
            if (tmp != null) {
                int len = tmp.length();
                for (int j = 0; j < len; j++) {
                    list.add(Integer.parseInt(tmp.get(j).toString()));
                }
            }
            movieData.setGenreIds(list);
            movieData.setId(itemMovie.getLong(OWM_ID));
            movieData.setOriginalLanguage(itemMovie.getString(OWM_ORG_LANG));
            movieData.setOriginalTitle(itemMovie.getString(OWM_ORG_TITLE));
            movieData.setOverview(itemMovie.getString(OWM_OVERVIEW));
            movieData.setReleaseDate(itemMovie.getString(OWM_REL_DATE));
            movieData.setPosterPath(itemMovie.getString(OWM_POSTER_PATH));
            movieData.setPopularity(itemMovie.getDouble(OWM_POPULARITY));
            movieData.setTitle(itemMovie.getString(OWM_TITLE));
            movieData.setVideo(itemMovie.getBoolean(OWM_VIDEO));
            movieData.setVoteAvg(itemMovie.getDouble(OWM_VOTE_AVG));
            movieData.setVoteCount(itemMovie.getInt(OWM_VOTE_COUNT));


            movieDataArray.add(i, movieData);
            movieDataArray.add(movieData);

            Log.v(LOG_TAG, "Adult " + Boolean.toString(movieDataArray.get(i).getAdult()));
            Log.v(LOG_TAG, "Backdrop path " + movieDataArray.get(i).getBackdropPath());
            for (int j = 0; j < movieDataArray.get(i).getGenreIds().size(); ++j) {
                Log.v(LOG_TAG, "Genre ID " + movieDataArray.get(i).getGenreIds().get(j));
            }
            Log.v(LOG_TAG, "Id " + Long.toString(movieDataArray.get(i).getId()));
            Log.v(LOG_TAG, "Org lang " + movieDataArray.get(i).getOriginalLanguage());
            Log.v(LOG_TAG, "Org title " + movieDataArray.get(i).getOriginalTitle());
            Log.v(LOG_TAG, "Overview " + movieDataArray.get(i).getOverview());
            Log.v(LOG_TAG, "Rel Data " + movieDataArray.get(i).getReleaseDate());
            Log.v(LOG_TAG, "Poster Path " + movieDataArray.get(i).getPosterPath());
            Log.v(LOG_TAG, "Popularity " + Double.toString(movieDataArray.get(i).getPopularity()));
            Log.v(LOG_TAG, "Title " + movieDataArray.get(i).getTitle());
            Log.v(LOG_TAG, "Video " + Boolean.toString(movieDataArray.get(i).getVideo()));
            Log.v(LOG_TAG, "Vote Avg " + Double.toString(movieDataArray.get(i).getVoteAvg()));
            Log.v(LOG_TAG, "Vote Count " + Integer.toString(movieDataArray.get(i).getVoteCount()));

            ContentValues movieValues = new ContentValues();

            long locationId = addMovie(Long.toString(movieData.getId()),
                    movieData.getTitle(),
                    movieData.getBackdropPath(),
                    Boolean.toString(movieData.getAdult()),
                    movieData.getOriginalLanguage(),
                    movieData.getOriginalTitle(),
                    movieData.getOverview(),
                    movieData.getReleaseDate(),
                    movieData.getPosterPath());


            movieValues.put(UserEntry.COLUMN_MOVIE_KEY, locationId);
            movieValues.put(UserEntry.COLUMN_VOTE_AVG, Double.toString(movieDataArray.get(i).getVoteAvg()));
            movieValues.put(UserEntry.COLUMN_VOTE_COUNT, Integer.toString(movieDataArray.get(i).getVoteCount()));
            movieValues.put(UserEntry.COLUMN_VIDEO, Boolean.toString(movieDataArray.get(i).getVideo()));
            movieValues.put(UserEntry.COLUMN_FAVORITE, Boolean.toString(true));
            movieValues.put(UserEntry.COLUMN_POPULARITY, Double.toString(movieDataArray.get(i).getPopularity()));

            cVVector.add(movieValues);
        }

        // add to database
        if (cVVector.size() > 0) {
            // Student: call bulkInsert to add the movieEntries to the database here
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(UserEntry.CONTENT_URI, cvArray);
        }

        Cursor cur = mContext.getContentResolver().query(
                UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        cVVector = new Vector<ContentValues>(cur.getCount());
        if (cur.moveToFirst()) {
            do {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cur, cv);
                cVVector.add(cv);
            } while (cur.moveToNext());
        }

        Log.d(LOG_TAG, "FetchWeatherTask Complete. " + cVVector.size() + " Inserted");

        return movieDataArray;
        //} catch (JSONException e) {
            //Log.e(LOG_TAG, e.getMessage(), e);
            //e.printStackTrace();
        //}
        //return null;
    }

}