package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    /*
     movieDetailArray.add(0, Boolean.toString(mMovieDataArray.get(position).getAdult()));
    movieDetailArray.add(1, Long.toString(mMovieDataArray.get(position).getId()));
    movieDetailArray.add(2, mMovieDataArray.get(position).getOriginalLanguage());
    movieDetailArray.add(3, mMovieDataArray.get(position).getOriginalTitle());
    movieDetailArray.add(4, mMovieDataArray.get(position).getOverview());
    movieDetailArray.add(5, mMovieDataArray.get(position).getReleaseDate());
    movieDetailArray.add(6, Double.toString(mMovieDataArray.get(position).getPopularity()));
    movieDetailArray.add(7, mMovieDataArray.get(position).getTitle());
    movieDetailArray.add(8, Boolean.toString(mMovieDataArray.get(position).getVideo()));
    movieDetailArray.add(9, Double.toString(mMovieDataArray.get(position).getVoteAvg()));
    movieDetailArray.add(10, Integer.toString(mMovieDataArray.get(position).getVoteCount()));
    */

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private String mAdult;
    private String mOrgLang;
    private String mOrgTitle;
    private String mOverviewStr;
    private String mRelDate;
    private String mVoteAvg;
    private String mImagePath;
    private static final String MOVIE_SHARE_HASHTAG = " #PopularMovie";

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            ArrayList<String> movieDetailArray = new ArrayList<String>();
            movieDetailArray = intent.getStringArrayListExtra(Intent.EXTRA_TEXT);

            mOverviewStr = movieDetailArray.get(4);
            ((TextView) rootView.findViewById(R.id.detail_overview))
                    .setText("Plot:" + mOverviewStr);
            mAdult = movieDetailArray.get(0);
            ((TextView) rootView.findViewById(R.id.detail_adult))
                    .setText("Adult: " + mAdult);
            mOrgLang = movieDetailArray.get(2);
            ((TextView) rootView.findViewById(R.id.detail_org_lang))
                    .setText("Lang: " + mOrgLang);
            mOrgTitle = movieDetailArray.get(3);
            ((TextView) rootView.findViewById(R.id.detail_org_title))
                    .setText("TItle: " + mOrgTitle);
            mRelDate = movieDetailArray.get(5);
            ((TextView) rootView.findViewById(R.id.detail_rel_date))
                    .setText("Release Date: " + mRelDate);
            mVoteAvg = movieDetailArray.get(9);
            ((TextView) rootView.findViewById(R.id.detail_user_rating))
                    .setText("User rating: " + mVoteAvg);
            mImagePath = movieDetailArray.get(11);
            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w185/" + mImagePath)
                    .into((ImageView) rootView.findViewById(R.id.detail_image));
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "I like the movie" + mOrgTitle + "!" + MOVIE_SHARE_HASHTAG);
        return shareIntent;
    }
}
