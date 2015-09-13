package com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

            try{
                // Construct the URL for the query
                URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[Developer_Key]");

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
            return null;
        }

    }
}
