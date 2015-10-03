package com.example.android.popularmovies;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.android.popularmovies.data.MovieContract;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class HistoryActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int MOVIE_LOADER = 0;

    HistMovieCursorAdapter mImageMovieAdapter;

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


    public HistoryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        ArrayList<String> moviePosterPath = new ArrayList<String>();

        mImageMovieAdapter = new HistMovieCursorAdapter(getActivity(), null, 0);

//        mImageMovieAdapter = new NetworkImageAdapter(getActivity(),
//                R.id.fav_image_item_movie_imageview,
//                moviePosterPath);

        //mImageMovieAdapter.mSuckDickMovieDataArray = new ArrayList<MovieData>();
        //mMovieDataArray = new ArrayList<MovieData>();

//        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_favorite_movie);
//        gridView.setAdapter(mImageMovieAdapter);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_history_movie);
        gridView.setAdapter(mImageMovieAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    //private void updateMovie() {
    //    FetchCursorMovieTask weatherTask = new FetchCursorMovieTask(getActivity());
    //    String sorts = com.example.android.popularmovies.Utility.getPreferredSort(getActivity());
    //    weatherTask.execute(sorts);
    //}

    @Override
    public void onStart() {
        super.onStart();
        //updateMovie();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortSetting = Utility.getPreferredSort(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = MovieContract.UserEntry.COLUMN_POPULARITY + " ASC";
        //Uri weatherForLocationUri = MovieContract.UserEntry.buildUserMovieWithFavorite(
        //        sortSetting);

        return new CursorLoader(getActivity(),
                MovieContract.UserEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mImageMovieAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mImageMovieAdapter.swapCursor(null);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//
//        Intent intent = getActivity().getIntent();
//        if (intent != null && intent.hasExtra(Intent.EXTRA_PROCESS_TEXT)) {
//            ArrayList<MovieDataPar> movieDetailParArray = new ArrayList<MovieDataPar>();
//            movieDetailParArray = intent.getParcelableArrayListExtra(Intent.EXTRA_PROCESS_TEXT);
//
//
//            for (int i = 0; i < movieDetailParArray.size(); ++i) {
//                mImageMovieAdapter.add("http://image.tmdb.org/t/p/w185/" + movieDetailParArray.get(i).getPosterPath());
//            }
//
//        }
//
//
//    }


}
