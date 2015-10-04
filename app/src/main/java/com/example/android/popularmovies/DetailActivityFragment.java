package com.example.android.popularmovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {


    SharedFavoritePreferences sharedFavoritePreferences  = new SharedFavoritePreferences();

    private ArrayAdapter<String> mReviewAdapter;
    ArrayList<String> mReviewArray;
    private ArrayAdapter<String> mYouTubeAdapter;
    ArrayList<String> mYouTubeArray;
    ArrayList<String> mFavoriteList;
    boolean mLike;


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
    private String mMovieId;
    private static final String MOVIE_SHARE_HASHTAG = " #PopularMovie";

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ArrayList<String> mReviewArray = new ArrayList<String>();
        ArrayList<String> mYouTubeArray = new ArrayList<String>();


        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            ArrayList<String> movieDetailArray = new ArrayList<String>();
            movieDetailArray = intent.getStringArrayListExtra(Intent.EXTRA_TEXT);

            mOverviewStr = movieDetailArray.get(4);
            ((TextView) rootView.findViewById(R.id.detail_overview))
                    .setText(mOverviewStr);
            mAdult = movieDetailArray.get(0);
            ((TextView) rootView.findViewById(R.id.detail_adult))
                    .setText("Adult: " + mAdult);
            mOrgLang = movieDetailArray.get(2);
            ((TextView) rootView.findViewById(R.id.detail_org_lang))
                    .setText("Language: " + mOrgLang);
            mOrgTitle = movieDetailArray.get(3);
            ((TextView) rootView.findViewById(R.id.detail_org_title))
                    .setText(mOrgTitle);
            mRelDate = movieDetailArray.get(5);
            ((TextView) rootView.findViewById(R.id.detail_rel_date))
                    .setText(mRelDate);
            mVoteAvg = movieDetailArray.get(9);
            ((TextView) rootView.findViewById(R.id.detail_user_rating))
                    .setText("Average rating: " + mVoteAvg);
            mImagePath = movieDetailArray.get(11);
            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w185/" + mImagePath)
                    .into((ImageView) rootView.findViewById(R.id.detail_image));
            mMovieId = movieDetailArray.get(14);

            //mReviewArray.add(0, movieDetailArray.get(12));
            mYouTubeArray = new ArrayList<String>(Arrays.asList(movieDetailArray.get(13).split("\\s*,\\s*")));
            //mYouTubeArray.add(movieDetailArray.get(13));

            String review = movieDetailArray.get(12);
            TextView tv = (TextView) rootView.findViewById(R.id.review_textview);

            String reviewStr = "";
            if (!(review.equals("Nothing"))) {
                while (review != null) {

                    if (review.indexOf("<author>") >= 0 && review.indexOf("</author>") >= 0
                            && review.indexOf("<content>") >= 0 && review.indexOf("</content>") >= 0) {
                        mReviewArray.add(review.substring(review.indexOf("<author>") + 8, review.indexOf("</author>")) + ": \n\n" +
                                review.substring(review.indexOf("<content>") + 9, review.indexOf("</content>") - 1));

                        reviewStr = reviewStr + "\n\n\n\n\"" + review.substring(review.indexOf("<content>") + 9, review.indexOf("</content>") - 1) +
                                "\"\n- by " +
                                review.substring(review.indexOf("<author>") + 8, review.indexOf("</author>"));

                        if (review.indexOf("</content>") + 10 < review.length()) {
                            review = review.substring(review.indexOf("</content>") + 10, review.length() - 1);
                        } else
                            review = null;
                    } else
                        review = null;


                }
            }

            tv.setText(reviewStr);




        }

//        mReviewAdapter =
//                new ArrayAdapter<String>(
//                        getActivity(),
//                        R.layout.review_item_movie,
//                        R.id.review_item_movie_textview,
//                        mReviewArray);
//
//        ListView reviewListView = (ListView) rootView.findViewById(R.id.listview_review);
//        reviewListView.setAdapter(mReviewAdapter);
//        setListViewHeightBasedOnChildren(reviewListView);

        mYouTubeAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.video_item_movie,
                        R.id.video_item_movie_textview,
                        mYouTubeArray);

        ListView youtubeListView = (ListView) rootView.findViewById(R.id.listview_youtube);
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


        mFavoriteList = new ArrayList<String>(Arrays.asList(sharedFavoritePreferences.getFavorites(getContext()).split("\\s*,\\s*")));

        final Button fav = (Button) rootView.findViewById(R.id.favorite_button);

        if (mFavoriteList.contains(mMovieId)){
            fav.setText("Unlike");
            mLike = true;
        } else {
            fav.setText("Like");
            mLike = false;
        }

        fav.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){

                if (mLike == false){
                    SharedFavoritePreferences sharedFavoritePreferencess = new SharedFavoritePreferences();
                    sharedFavoritePreferencess.addFavorite(getContext(), mMovieId);
                    mLike = true;
                    fav.setText("Unlike");
                } else {
                    SharedFavoritePreferences sharedFavoritePreferencess = new SharedFavoritePreferences();
                    sharedFavoritePreferencess.removeFavorite(getContext(), mMovieId);
                    mLike = false;
                    fav.setText("Like");
                }

              }
        });

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
        //shareIntent.setType("text/plain");
        shareIntent.setType("text/html");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "I like the movie" + mOrgTitle + "!" + MOVIE_SHARE_HASHTAG);
        return shareIntent;
    }

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
