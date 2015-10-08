package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.ShareActionProvider;
import android.support.v4.content.CursorLoader;
import android.widget.TextView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import com.example.android.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailForHistFavActivityFragment extends Fragment implements LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailForHistFavActivityFragment.class.getSimpleName();

    private static final int MOVIE_LOADER = 0;

    private ShareActionProvider mShareActionProvider;

    HistMovieCursorAdapter mImageMovieAdapter;

    private String mMovieId;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.UserEntry.TABLE_NAME + "." + MovieContract.UserEntry._ID,
            MovieContract.UserEntry.COLUMN_MOVIE_KEY,
            MovieContract.UserEntry.COLUMN_YOUTUBE,
            MovieContract.UserEntry.COLUMN_FAVORITE,
            MovieContract.UserEntry.COLUMN_POPULARITY,
            MovieContract.UserEntry.COLUMN_REVIEW,
            MovieContract.UserEntry.COLUMN_VIDEO,
            MovieContract.UserEntry.COLUMN_VOTE_AVG,
            MovieContract.UserEntry.COLUMN_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_REL_DATE,
            MovieContract.MovieEntry.COLUMN_ADULT,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_ORG_LANG,
            MovieContract.MovieEntry.COLUMN_ORG_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW
    };

    static final int COL_USER_ID = 0;
    static final int COL_MOVIE_KEY = 1;
    static final int COL_YOUTUBE = 2;
    static final int COL_FAVORITE = 3;
    static final int COL_POPULARITY = 4;
    static final int COL_REVIEW = 5;
    static final int COL_VIDEO = 6;
    static final int COL_VOTE_AVG = 7;
    static final int COL_VOTE_COUNT = 8;
    static final int COL_MOVIE_ID = 9;
    static final int COL_POSTER_PATH = 10;
    static final int COL_REL_DATE = 11;
    static final int COL_ADULT = 12;
    static final int COL_BACKDROP_PATH = 13;
    static final int COL_ORG_LANG = 14;
    static final int COL_ORG_TITLE = 15;
    static final int COL_OVERVIEW = 16;

    public DetailForHistFavActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_for_hist_fav, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mMovieId != null ) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        //shareIntent.setType("text/plain");
        shareIntent.setType("text/html");
//        shareIntent.putExtra(Intent.EXTRA_TEXT,
//                "I like the movie" + mOrgTitle + "!" + MOVIE_SHARE_HASHTAG);
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                //mYouTubeLink);
                mMovieId);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) { return; }

        String dataString = data.getString(COL_ORG_TITLE);


        TextView detailTextView = (TextView)getView().findViewById(R.id.abcd);
        detailTextView.setText(mMovieId + " " + dataString);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
