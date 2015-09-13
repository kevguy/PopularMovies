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

    ArrayAdapter<String> mMovieAdapter;

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

        String[] movieArray = {
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
                "http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg",
        };

        ArrayList<String> awesomeMovie = new ArrayList<String>(Arrays.asList(movieArray));

        mMovieAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.image_item_movie,
                R.id.image_item_movie_imageview,
                awesomeMovie);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movie);
        //gridView.setAdapter(mMovieAdapter);
        gridView.setAdapter(new ImageAdapter(getActivity(), movieArray));
        ImageView imageView = (ImageView) rootView.findViewById(R.id.sosad);
        //Picasso.with(getActivity()).load("http://www.billboard.com/files/styles/promo_650/public/media/linkin-park-billboard-650.jpg").into(imageView);

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

    private class FetchMovieTask extends AsyncTask<Void, Void, Void> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {
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
                /*
                String format = "json";
                String units = "metric";
                int numDays = 7;
                 */
                /*
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath("daily")
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays));
                URL url = new URL(builder.build().toString());
                Log.v(LOG_TAG, url.toString());

                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                 */

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

                // Read the input strem into a String
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
                //String[] result = getWeatherDataFromJson(forecastJsonStr, numDays);
                //for (String item: result){
                //    Log.v(LOG_TAG, item);
                //}
                //return result;
                getMovieDataFromJson(movieJsonStr);
            }
            catch (JSONException e){
                Log.e(LOG_TAG, "Error getting weather data from json", e);
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private void getMovieDataFromJson(String movieJsonStr)
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
            JSONObject itemMovie = movieArray.getJSONObject(0);

            ArrayList<MovieData> movieDataArray = new ArrayList<MovieData>();

            MovieData movieData = new MovieData();

            movieData.setAdult(itemMovie.getBoolean(OWM_ADULT));
            movieData.setBackdropPath(itemMovie.getString(OWM_BACKDROP_PATH));
            // Genre Ids
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


            /*
            Boolean adult = itemMovie.getBoolean(OWM_ADULT);
            String backdrop_path = itemMovie.getString(OWM_BACKDROP_PATH);
            // Genre Ids
            long id = itemMovie.getLong(OWM_ID);
            String original_language = itemMovie.getString(OWM_ORG_LANG);
            String original_title = itemMovie.getString(OWM_ORG_TITLE);
            String overview = itemMovie.getString(OWM_OVERVIEW);
            String release_date = itemMovie.getString(OWM_REL_DATE);
            String poster_path = itemMovie.getString(OWM_POSTER_PATH);
            double popularity = itemMovie.getDouble(OWM_POPULARITY);
            String title = itemMovie.getString(OWM_TITLE);
            String video = itemMovie.getString(OWM_VIDEO);
            String vote_average = itemMovie.getString(OWM_VOTE_AVG);
            String vote_count = itemMovie.getString(OWM_VOTE_COUNT);


            Log.v(LOG_TAG, Boolean.toString(adult));
            Log.v(LOG_TAG, backdrop_path);
            Log.v(LOG_TAG, Long.toString(id));
            Log.v(LOG_TAG, original_language);
            Log.v(LOG_TAG, original_title);
            Log.v(LOG_TAG, overview);
            Log.v(LOG_TAG, release_date);
            Log.v(LOG_TAG, poster_path);
            Log.v(LOG_TAG, Double.toString(popularity));
            Log.v(LOG_TAG, title);
            Log.v(LOG_TAG, video);
            Log.v(LOG_TAG, vote_average);
            Log.v(LOG_TAG, vote_count);
            */

            Log.v(LOG_TAG, "Adult " + Boolean.toString(movieData.getAdult()));
            Log.v(LOG_TAG, "Backdrop path " + movieData.getBackdropPath());
            Log.v(LOG_TAG, "Id " + Long.toString(movieData.getId()));
            Log.v(LOG_TAG, "Org lang " + movieData.getOriginalLanguage());
            Log.v(LOG_TAG, "Org title " + movieData.getOriginalTitle());
            Log.v(LOG_TAG, "Overview " + movieData.getOverview());
            Log.v(LOG_TAG, "Rel Data " + movieData.getReleaseDate());
            Log.v(LOG_TAG, "Poster Path " + movieData.getPosterPath());
            Log.v(LOG_TAG, "Popularity " + Double.toString(movieData.getPopularity()));
            Log.v(LOG_TAG, "Title " + movieData.getTitle());
            Log.v(LOG_TAG, "Video " + Boolean.toString(movieData.getVideo()));
            Log.v(LOG_TAG, "Vote Avg " + movieData.getVoteAvg());
            Log.v(LOG_TAG, "Vote Count " + movieData.getVoteCount());


            movieDataArray.add(0, movieData);
            //movieDataArray.add(movieData);

            Log.v(LOG_TAG, "Adult " + Boolean.toString(movieDataArray.get(0).getAdult()));
            Log.v(LOG_TAG, "Backdrop path " + movieDataArray.get(0).getBackdropPath());
            Log.v(LOG_TAG, "Id " + Long.toString(movieDataArray.get(0).getId()));
            Log.v(LOG_TAG, "Org lang " + movieDataArray.get(0).getOriginalLanguage());
            Log.v(LOG_TAG, "Org title " + movieDataArray.get(0).getOriginalTitle());
            Log.v(LOG_TAG, "Overview " + movieDataArray.get(0).getOverview());
            Log.v(LOG_TAG, "Rel Data " + movieDataArray.get(0).getReleaseDate());
            Log.v(LOG_TAG, "Poster Path " + movieDataArray.get(0).getPosterPath());
            Log.v(LOG_TAG, "Popularity " + Double.toString(movieDataArray.get(0).getPopularity()));
            Log.v(LOG_TAG, "Title " + movieDataArray.get(0).getTitle());
            Log.v(LOG_TAG, "Video " + Boolean.toString(movieDataArray.get(0).getVideo()));
            Log.v(LOG_TAG, "Vote Avg " + movieDataArray.get(0).getVoteAvg());
            Log.v(LOG_TAG, "Vote Count " + movieDataArray.get(0).getVoteCount());




        }

    }
}
