package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * {@link FavMovieCursorAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class FavMovieCursorAdapter extends CursorAdapter {

    //private ViewHolder viewHolder;

    public FavMovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_image_item_movie, parent, false);

        return view;
    }

    /*
    private static class ViewHolder {
        private TextView itemView;
    }
    */

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        //TextView tv = (TextView)view;
        //tv.setText(cursor.getString((FavoriteActivityFragment.COL_YOUTUBE)));

        ImageView im = (ImageView) view.findViewById(R.id.fav_image_item_movie_imageview);

        String imagePath = cursor.getString(FavoriteActivityFragment.COL_POSTER_PATH);
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/" + imagePath)
                .into(im );
    }
}