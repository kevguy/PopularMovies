package com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    NetworkImageAdapter mImageMovieAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateRecommendation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> movieBackdropPath = new ArrayList<String>();

        mImageMovieAdapter = new NetworkImageAdapter(getActivity(),
                R.id.image_item_movie_imageview,
                movieBackdropPath);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movie);
        gridView.setAdapter(mImageMovieAdapter);

        return rootView;
    }

    private void updateRecommendation() {
        FetchMovieTask movieTask = new FetchMovieTask();
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //String location = prefs.getString(getString(R.string.pref_location_key),
                //getString(R.string.pref_location_default));

        //movieTask.execute(location);
        movieTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRecommendation();
    }

    private class FetchMovieTask extends AsyncTask<Void, Void, ArrayList<MovieData>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected ArrayList<MovieData> doInBackground(Void... params) {
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
                final String API_KEY = "";

                // Construct the URL for the query
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("discover")
                        .appendPath("movie")
                        .appendQueryParameter(SORT_BY_PARAM, "popularity.desc")
                        .appendQueryParameter(API_KEY_PARAM, API_KEY);
                URL url = new URL(builder.build().toString());
                Log.v(LOG_TAG, url.toString());

                //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=1f81622504d93fd944c771a94fefecfb");

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



            }
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

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULT);

            ArrayList<MovieData> movieDataArray = new ArrayList<MovieData>();

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
                movieData.setVoteAvg(itemMovie.getString(OWM_VOTE_AVG));
                movieData.setVoteCount(itemMovie.getString(OWM_VOTE_COUNT));

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
                Log.v(LOG_TAG, "Vote Avg " + movieDataArray.get(i).getVoteAvg());
                Log.v(LOG_TAG, "Vote Count " + movieDataArray.get(i).getVoteCount());
            }


            return movieDataArray;
        }

    }
}
