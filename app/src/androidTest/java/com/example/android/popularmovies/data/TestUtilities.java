package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.android.popularmovies.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your WeatherContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {
    static final String TEST_LOCATION = "99705";
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }


    static ContentValues createUserValues(long movieRowId) {
        ContentValues userValues = new ContentValues();
        userValues.put(MovieContract.UserEntry.COLUMN_MOVIE_KEY, movieRowId);
        userValues.put(MovieContract.UserEntry.COLUMN_POPULARITY, 15.0);
        userValues.put(MovieContract.UserEntry.COLUMN_VIDEO, "TRUE");
        userValues.put(MovieContract.UserEntry.COLUMN_VOTE_AVG, 14.0);
        userValues.put(MovieContract.UserEntry.COLUMN_VOTE_COUNT, 1000);
        return userValues;
    }



    static ContentValues createKevLaiMovieValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_ADULT, "TRUE");
        testValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, "Backdrop path");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 31415);
        testValues.put(MovieContract.MovieEntry.COLUMN_ORG_LANG, "en");
        testValues.put(MovieContract.MovieEntry.COLUMN_ORG_TITLE, "Kev Lai Rocks!");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "Kev Lai Always Rocks. Always!");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "Poster Path");
        testValues.put(MovieContract.MovieEntry.COLUMN_REL_DATE, "1993-05-08");
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Kev Lai Rocks!");

        return testValues;
    }


    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
