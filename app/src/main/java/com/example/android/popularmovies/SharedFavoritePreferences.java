package com.example.android.popularmovies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedFavoritePreferences {

    public static final String PREFS_NAME = "FAVORITE_APP";
    public static final String FAVORITES = "Movie_Favorite";
    private static final String LOG_TAG = SharedFavoritePreferences.class.getSimpleName();

    public SharedFavoritePreferences() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, String favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(FAVORITES, favorites);

        editor.commit();
    }

    public void addFavorite(Context context, String movieId) {
        String favorites = getFavorites(context);
        if (favorites != null) {
            if (favorites.indexOf("," + movieId) < 0)
                favorites = favorites + "," + movieId;
        }
        else
            favorites = "," + movieId;

        Log.v(LOG_TAG, favorites);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, String movieId) {
        String favorites = getFavorites(context);
        if (favorites != null) {
            int pos = favorites.indexOf("," + movieId);
            favorites = favorites.substring(0, pos -1) + favorites.substring(pos + movieId.length() + 1, favorites.length() - 1);
            saveFavorites(context, favorites);
        }
    }

    public String getFavorites(Context context) {
        SharedPreferences settings;
        String favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String data = settings.getString(FAVORITES, null);
            favorites = data;
        } else
            return null;

        return favorites;
    }
}