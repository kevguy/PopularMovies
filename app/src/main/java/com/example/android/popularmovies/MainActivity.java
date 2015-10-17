package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback{
    private boolean mTwoPane;
    private String mChoice = "";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mChoice = prefs.getString(getString(R.string.pref_sorts_key),
                getString(R.string.pref_sorts_default));

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(ArrayList<String> abc) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putStringArrayList(DetailActivityFragment.DETAIL_URI, abc);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                .putStringArrayListExtra(Intent.EXTRA_TEXT, abc);
        startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onStart() in Activity here");
        Log.d(LOG_TAG, "mChoice" + mChoice);
        //updateRecommendation();
        String choice = "";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        choice = prefs.getString(getString(R.string.pref_sorts_key),
                getString(R.string.pref_sorts_default));

        // update the location in our second pane using the fragment manager
        //if (location != null && !location.equals(mLocation)) {
        //ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
        if (choice != null && !choice.equals(mChoice)) {
            if (mTwoPane){
                MainActivityFragment fff = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
                if (null != fff) {
                    fff.updateRecommendation();
                }
                mChoice = choice;
            } else {
                MainActivityFragment ff = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                if (null != ff) {
                    ff.updateRecommendation();
                }
                mChoice = choice;
            }
        }
    }

}
