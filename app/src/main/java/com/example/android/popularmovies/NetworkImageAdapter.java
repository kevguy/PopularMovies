package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by kev on 9/13/15.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: Popular Movies
 * Created by muhammad on 01/07/15.
 */

public class NetworkImageAdapter extends ArrayAdapter<String> {

    private ViewHolder viewHolder;
    private Context mContext;

    private static class ViewHolder {
        private TextView itemView;
    }

    public NetworkImageAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
        super(context, textViewResourceId, items);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.image_item_movie, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(R.id.image_item_movie_imageview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(item);
            //Picasso.with(mContext)
             //       .load(item)
              //      .into((ImageView) convertView);
        }

        return convertView;
    }
}