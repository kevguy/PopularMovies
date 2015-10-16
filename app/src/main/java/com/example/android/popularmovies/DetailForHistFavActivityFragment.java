package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailForHistFavActivityFragment extends Fragment implements LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailForHistFavActivityFragment.class.getSimpleName();

    private static final int MOVIE_LOADER = 0;

    private ShareActionProvider mShareActionProvider;

    static final String MOVIE_URI = "URI";
    private Uri mUri;

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

    private String mMovieId;
    private String mAdult;
    private String mOrgLang;
    private String mOrgTitle;
    private String mOverviewStr;
    private String mRelDate;
    private String mVoteAvg;
    private String mImagePath;
    private String mReviewStr;

    private ArrayAdapter<String> mReviewAdapter;
    ArrayList<String> mReviewArray = new ArrayList<String>();
    private ArrayAdapter<String> mYouTubeAdapter;
    ArrayList<String> mYouTubeArray = new ArrayList<String>();
    String mYouTubeLink = "Nothing";

    public DetailForHistFavActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
            if (arguments != null) {
            mUri = arguments.getParcelable(DetailForHistFavActivityFragment.MOVIE_URI);
        }
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
        //shareIntent.putExtra(Intent.EXTRA_TEXT,
        //        "I like the movie !");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mYouTubeLink);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null || intent.getData() == null) {
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
        );*/
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
                    );
            }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) { return; }

        String dataString = data.getString(COL_ORG_TITLE);

        mMovieId = data.getString(COL_MOVIE_ID);
        mAdult = data.getString(COL_ADULT);
        mOrgLang = data.getString(COL_ORG_LANG);
        mOrgTitle = data.getString(COL_ORG_TITLE);
        mOverviewStr = data.getString(COL_OVERVIEW);
        mRelDate = data.getString(COL_REL_DATE);
        mVoteAvg = data.getString(COL_VOTE_AVG);
        mImagePath = data.getString(COL_POSTER_PATH);
        String youStr = data.getString(COL_YOUTUBE);
        mReviewStr = data.getString(COL_REVIEW);


        Log.v(LOG_TAG, mMovieId);
        Log.v(LOG_TAG, mAdult);
        Log.v(LOG_TAG, mOrgLang);
        Log.v(LOG_TAG, mOrgTitle);
        Log.v(LOG_TAG, mOverviewStr);
        Log.v(LOG_TAG, mRelDate);
        Log.v(LOG_TAG, mVoteAvg);
        Log.v(LOG_TAG, mImagePath);
        Log.v(LOG_TAG, youStr);
        Log.v(LOG_TAG, mReviewStr);

//        mImagePath = movieDetailArray.get(11);
//        Picasso.with(getActivity())
//                .load("http://image.tmdb.org/t/p/w185/" + mImagePath)
//                .into((ImageView) rootView.findViewById(R.id.detail_image));
//        mMovieId = movieDetailArray.get(14);

        //mReviewArray.add(0, movieDetailArray.get(12));
        mYouTubeArray = new ArrayList<String>(Arrays.asList(youStr.split("\\s*,\\s*")));
        mYouTubeLink = mYouTubeArray.get(0);

        String reviewStr = "";
        if (!(mReviewStr.equals("Nothing"))) {
            while (mReviewStr != null) {
                if (mReviewStr.indexOf("<author>") >= 0 && mReviewStr.indexOf("</author>") >= 0
                        && mReviewStr.indexOf("<content>") >= 0 && mReviewStr.indexOf("</content>") >= 0) {
                    mReviewArray.add(mReviewStr.substring(mReviewStr.indexOf("<author>") + 8, mReviewStr.indexOf("</author>")) + ": \n\n" +
                            mReviewStr.substring(mReviewStr.indexOf("<content>") + 9, mReviewStr.indexOf("</content>") - 1));

                    reviewStr = reviewStr + "\n\n\n\n\"" + mReviewStr.substring(mReviewStr.indexOf("<content>") + 9, mReviewStr.indexOf("</content>") - 1) +
                            "\"\n- by " +
                            mReviewStr.substring(mReviewStr.indexOf("<author>") + 8, mReviewStr.indexOf("</author>"));

                    if (mReviewStr.indexOf("</content>") + 10 < mReviewStr.length()) {
                        mReviewStr = mReviewStr.substring(mReviewStr.indexOf("</content>") + 10, mReviewStr.length() - 1);
                    } else
                        mReviewStr = null;
                } else
                    mReviewStr = null;


            }
            for (String revieww: mReviewArray){
                Log.v(LOG_TAG, "review is" + revieww);
            }
        }

        for (String youtube: mYouTubeArray){
            Log.v(LOG_TAG, "youTube " + youtube);
        }

        ((TextView) getView().findViewById(R.id.fav_hist_detail_overview))
                .setText(mOverviewStr);
        ((TextView) getView().findViewById(R.id.fav_hist_detail_adult))
                .setText("Adult: " + mAdult);
        ((TextView) getView().findViewById(R.id.fav_hist_detail_org_lang))
                .setText("Language: " + mOrgLang);
        ((TextView) getView().findViewById(R.id.fav_hist_detail_org_title))
                .setText(mOrgTitle);
        ((TextView) getView().findViewById(R.id.fav_hist_detail_rel_date))
                .setText(mRelDate);
        ((TextView) getView().findViewById(R.id.fav_hist_detail_user_rating))
                .setText("Average rating: " + mVoteAvg);
        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w185/" + mImagePath)
                .into((ImageView) getView().findViewById(R.id.fav_hist_detail_image));

        TextView tv = (TextView) getView().findViewById(R.id.fav_hist_review_textview);
        if (reviewStr.equals("")) {
            reviewStr = "No reviews available.";
        }
        tv.setText(reviewStr);

        mYouTubeAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.video_item_movie,
                        R.id.video_item_movie_textview,
                        mYouTubeArray);

        ListView youtubeListView = (ListView) getView().findViewById(R.id.fav_hist_listview_youtube);
        youtubeListView.setAdapter(mYouTubeAdapter);
        setListViewHeightBasedOnChildren(youtubeListView);

        youtubeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //String locationSetting = Utility.getPreferredLocation(getActivity());
                //Intent intent = new Intent(getActivity(), DetailActivity.class)
                //        .setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                //                locationSetting, cursor.getLong(COL_WEATHER_DATE)
                //        ));

                String link = mYouTubeAdapter.getItem(position);

                //Toast.makeText(getActivity(), link, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
            }
        });






        //TextView detailTextView = (TextView)getView().findViewById(R.id.abcd);
        //detailTextView.setText(mMovieId + " " + dataString);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
